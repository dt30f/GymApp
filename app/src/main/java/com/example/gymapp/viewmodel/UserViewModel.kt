package com.example.gymapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.gymapp.data.User
import com.example.gymapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val uiState: StateFlow<UserUiState> = _uiState

    init {
        loadUser()
    }

    private fun loadUser() {
        viewModelScope.launch {
            val user = repository.getUser()

            _uiState.value =
                if (user == null) {
                    UserUiState.NoUser
                } else {
                    UserUiState.HasUser(user)
                }
        }
    }

    fun registerUser(
        name: String,
        squat1RM: Int,
        bench1RM: Int,
        deadlift1RM: Int,
        bodyWeight: Float
    ) {
        viewModelScope.launch {
            val user = User(
                id = UUID.randomUUID().toString(),
                name = name,
                squat1RM = squat1RM,
                bench1RM = bench1RM,
                deadlift1RM = deadlift1RM,
                bodyWeight = bodyWeight
            )

            repository.insertUser(user)

            _uiState.value = UserUiState.HasUser(user)
        }
    }

    fun changeBodyWeight(newBodyWeight: Float) {
        viewModelScope.launch {
            val user = repository.getUser() ?: return@launch
            repository.updateBodyWeight(user.id, newBodyWeight)
            refreshUser()
        }
    }

    fun refreshUser() {
        loadUser()
    }
    fun logout() {
        viewModelScope.launch {
            val current = repository.getUser()
            if (current != null) {
                repository.deleteUser(current)
            }
            _uiState.value = UserUiState.NoUser
        }
    }


}

sealed class UserUiState {
    object Loading : UserUiState()
    object NoUser : UserUiState()
    data class HasUser(val user: User) : UserUiState()
}
