package com.example.benchmark.Soal4

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun RpsApp() {
    val game = remember { RockPaperSys() }

    var currentScreen by remember { mutableStateOf("Start") }
    var lastRound by remember { mutableStateOf<RoundResult?>(null) }

    when (currentScreen) {
        "Start" -> {
            game.startNewGame()
            StartScreen(
                playerScore = game.pScore,
                aiScore = game.aiScore,
                bestOfN = game.bestOfN,
                onStart = { currentScreen = "Pick" }
            )
        }

        "Pick" -> PickScreen(
            playerScore = game.pScore,
            aiScore = game.aiScore,
            bestOfN = game.bestOfN,
            onPick = { move ->
                val result = game.playRound(move)
                lastRound = result
                currentScreen = "Reveal"
            }
        )

        "Reveal" -> RevealScreen(
            playerScore = game.pScore,
            aiScore = game.aiScore,
            bestOfN = game.bestOfN,
            result = lastRound!!,
            onFinished = {
                if (game.isGameOver()) {
                    currentScreen = "GameOver"
                } else {
                    currentScreen = "Pick"
                }
            }
        )

        "GameOver" -> {
            val context = LocalContext.current
            val winner = game.getWinner() ?: "Draw"
            GameOverScreen(
                playerScore = game.pScore,
                aiScore = game.aiScore,
                bestOfN = game.bestOfN,
                bestScore = game.bestScore,
                winner = winner,
                onRestart = {
                    game.startNewGame()
                    currentScreen = "Start"
                },
                onExit = { (context as? Activity)?.finish() }
            )
        }

    }
}


@Composable
fun StartScreen(
    playerScore: Int,
    aiScore: Int,
    bestOfN: Int,
    onStart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "👨 $playerScore - $aiScore 🤖", fontSize = 18.sp)
            Text(text = "Best of $bestOfN", fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rock • Paper • Scissors",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = onStart,
                modifier = Modifier.width(160.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFD3D9E2),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(50)
            ) {
                Text("Start", fontSize = 18.sp)
            }
        }

        Spacer(modifier = Modifier.height(60.dp))
    }
}




@Composable
fun PickScreen(
    playerScore: Int,
    aiScore: Int,
    bestOfN: Int,
    onPick: (Move) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("👨 $playerScore - $aiScore 🤖", fontSize = 18.sp)
            Text("Best of $bestOfN", fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Pick your move!", fontSize = 20.sp)
            Spacer(Modifier.height(16.dp))
            Text("?  VS  ?", fontSize = 28.sp)
            Spacer(Modifier.height(32.dp))

            val moves = remember { Move.values().toList() }.shuffled()
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                moves.forEach { move ->
                    Button(
                        onClick = { onPick(move) },
                        modifier = Modifier.width(100.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFD3D9E2),
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = when (move) {
                                Move.ROCK -> "✊ Rock"
                                Move.PAPER -> "✋ Paper"
                                Move.SCISSORS -> "✌ Scissor"
                            }
                        )
                    }
                }
            }
        }
    }
}




@Composable
fun RevealScreen(
    playerScore: Int,
    aiScore: Int,
    bestOfN: Int,
    result: RoundResult,
    onFinished: () -> Unit
) {
    LaunchedEffect(result) {
        delay(1500)
        onFinished()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("👨 $playerScore - $aiScore 🤖", fontSize = 18.sp)
            Text("Best of $bestOfN", fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "${emojiFor(result.playerMove)}  VS  ${emojiFor(result.aiMove)}",
                fontSize = 36.sp
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = when (result.result) {
                    Result.WIN -> "You Win!"
                    Result.LOSE -> "You Lose!"
                    Result.DRAW -> "Draw"
                },
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(Modifier.height(60.dp))
    }
}


@Composable
fun GameOverScreen(
    playerScore: Int,
    aiScore: Int,
    bestOfN: Int,
    bestScore: Int,
    winner: String,
    onRestart: () -> Unit,
    onExit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("👨 $playerScore - $aiScore 🤖", fontSize = 18.sp)
            Text("Best of $bestOfN", fontSize = 18.sp)
        }

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (winner == "Player") "You Win the Match!" else "You Lose the Match!",
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(8.dp))
            Text("Best Score: $bestScore", fontSize = 20.sp)
            Spacer(Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = onRestart,
                    modifier = Modifier.width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD3D9E2),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Restart")
                }
                Button(
                    onClick = onExit,
                    modifier = Modifier.width(120.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD3D9E2),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Exit")
                }
            }
        }
    }
}




private fun emojiFor(move: Move): String {
    return when (move) {
        Move.ROCK -> "✊ "
        Move.PAPER -> "✋ "
        Move.SCISSORS -> "✌ "
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Soal4Preview(){
    RpsApp()
}