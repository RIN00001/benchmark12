package com.example.benchmark.Soal1

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch



@Composable
fun Soal1View() {
    val game = remember { ReactSys() }
    var state by remember { mutableStateOf(ReactSys.GameState.IDLE) }
    var reaction by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                when (state) {
                    ReactSys.GameState.WAITING -> Color.Red
                    ReactSys.GameState.GO -> Color.Green
                    else -> Color.Gray
                }
            )
            .clickable {
                when (state) {
                    ReactSys.GameState.IDLE -> {
                        state = ReactSys.GameState.WAITING
                        scope.launch {
                            game.waitRandomDelay()
                            state = ReactSys.GameState.GO
                        }
                    }
                    ReactSys.GameState.GO -> {
                        reaction = game.calculateReaction()
                        state = ReactSys.GameState.RESULT
                    }
                    ReactSys.GameState.RESULT -> {
                        state = ReactSys.GameState.IDLE
                    }
                    else -> {}
                }
            },
        contentAlignment = Alignment.Center
    ) {
        when (state) {
            ReactSys.GameState.IDLE -> Text("Tap to Start", fontSize = 24.sp)
            ReactSys.GameState.WAITING -> Text("Wait...", fontSize = 24.sp)
            ReactSys.GameState.GO -> Text("TAP NOW!", fontSize = 24.sp)
            ReactSys.GameState.RESULT -> Text("Your reaction: $reaction ms\nTap to retry", fontSize = 20.sp)
        }
    }
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Soal1Preview() {
}