package com.example.gymapp.navigation

import com.example.gymapp.screens.RegisterScreen
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.*
import com.example.gymapp.data.ProgramTemplates
import com.example.gymapp.progress.LiftProgressScreen
import com.example.gymapp.progress.ProgressScreen
import com.example.gymapp.screens.*
import com.example.gymapp.screens.programs.ProgramDetailScreen
import com.example.gymapp.viewmodel.UserViewModel

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
        composable("profile"){
            UserProfileScreen(navController)
        }
        composable("progress") {
            ProgressScreen(navController)
        }
        composable("programs") {
            ProgramsScreen(navController)
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

            val program = allPrograms.find { it.id == programName }
                ?: allPrograms.first()

            ProgramDetailScreen(program, navController)
        }

    }
}
