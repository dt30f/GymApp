package com.example.gymapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.ActiveProgram
import com.example.gymapp.data.WeekPlan
import com.example.gymapp.data.repository.ActiveProgramRepository
import com.example.gymapp.data.repository.StartProgramResult
import com.example.gymapp.data.timestampToLocalDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

private val StartDateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM", Locale.getDefault())

@HiltViewModel
class ActiveProgramsViewModel @Inject constructor(
    private val repository: ActiveProgramRepository
) : ViewModel() {

    val activePrograms: StateFlow<List<ActiveProgram>> = repository.observeActivePrograms()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    private val _startProgramState = MutableStateFlow(StartProgramUiState())
    val startProgramState: StateFlow<StartProgramUiState> = _startProgramState.asStateFlow()

    fun startProgram(
        programId: String,
        programName: String,
        selectedLift: String,
        oneRepMax: Double,
        generatedPlan: List<WeekPlan>
    ) {
        viewModelScope.launch {
            _startProgramState.value = StartProgramUiState(isSaving = true)

            _startProgramState.value = when (
                val result = repository.startProgram(
                    programId = programId,
                    programName = programName,
                    selectedLift = selectedLift,
                    oneRepMax = oneRepMax,
                    generatedPlan = generatedPlan,
                    startDate = System.currentTimeMillis()
                )
            ) {
                is StartProgramResult.Success -> StartProgramUiState(
                    isSuccess = true,
                    message = if (result.wasShifted) {
                        "$programName started for $selectedLift. First workout moved to ${timestampToLocalDate(result.resolvedStartDate).format(StartDateFormatter)} to avoid a calendar conflict."
                    } else {
                        "$programName started for $selectedLift."
                    },
                    startedProgramId = result.programRecordId
                )

                is StartProgramResult.LiftAlreadyActive -> StartProgramUiState(
                    message = "A program is already active for ${result.lift}."
                )

                StartProgramResult.NoUser -> StartProgramUiState(
                    message = "Create a profile before starting a program."
                )
            }
        }
    }

    fun clearStartProgramFeedback() {
        _startProgramState.value = StartProgramUiState()
    }

    fun activeProgram(programId: Long): Flow<ActiveProgram?> {
        return repository.observeActiveProgram(programId)
    }
}

data class StartProgramUiState(
    val isSaving: Boolean = false,
    val isSuccess: Boolean = false,
    val message: String? = null,
    val startedProgramId: Long? = null
)
