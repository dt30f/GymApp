package com.example.gymapp


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color
import com.example.gymapp.navigation.Navigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color.Red,
                    background = Color(0xFF07162e), // tvoja globalna boja pozadine
                    surface = Color.White,
                    onPrimary = Color.White
                )
            ) {
                Navigation()
            }
        }
    }
}


