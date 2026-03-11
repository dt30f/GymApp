package com.example.gymapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.gymapp.data.ProgramTemplates
import com.example.gymapp.progress.LiftProgressScreen
import com.example.gymapp.progress.ProgressScreen
import com.example.gymapp.screens.HomeScreen
import com.example.gymapp.screens.ProgramsScreen
import com.example.gymapp.screens.RegisterScreen
import com.example.gymapp.screens.StartScreen
import com.example.gymapp.screens.UserProfileScreen
import com.example.gymapp.screens.programs.ActiveProgramDetailScreen
import com.example.gymapp.screens.programs.ActiveProgramsScreen
import com.example.gymapp.screens.programs.ProgramDetailScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "start"
    ) {
        composable("start") {
            StartScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("profile") {
            UserProfileScreen(navController)
        }
        composable("progress") {
            ProgressScreen(navController)
        }
        composable("programs") {
            ProgramsScreen(navController)
        }
        composable("active_programs") {
            ActiveProgramsScreen(navController)
        }
        composable("progress/{liftType}") { backStackEntry ->
            val liftType = backStackEntry.arguments?.getString("liftType") ?: "Bench"
            LiftProgressScreen(liftType)
        }
        composable("program/{programId}") { backStackEntry ->
            val programName = backStackEntry.arguments?.getString("programId") ?: "Hybrid_Strength"

            val allPrograms = ProgramTemplates.fiveByFive() +
                ProgramTemplates.fiveThreeOne() +
                ProgramTemplates.hybrid()

            val program = allPrograms.find { it.id == programName } ?: allPrograms.first()

            ProgramDetailScreen(program, navController)
        }
        composable("active_program/{activeProgramId}") { backStackEntry ->
            val activeProgramId = backStackEntry.arguments?.getString("activeProgramId")?.toLongOrNull() ?: -1L
            ActiveProgramDetailScreen(
                programId = activeProgramId,
                navController = navController
            )
        }
    }
}
