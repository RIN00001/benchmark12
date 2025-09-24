package com.example.benchmark

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.benchmark.Soal1.ReactionGameUI
import com.example.benchmark.Soal1.Soal1Preview
import com.example.benchmark.ui.theme.BenchmarkTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BenchmarkTheme {
                Soal1Preview()
            }
            }
        }
    }

