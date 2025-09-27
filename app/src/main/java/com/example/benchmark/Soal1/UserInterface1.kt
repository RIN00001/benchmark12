package com.example.benchmark.Soal1

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.soal1.CardRes
import com.example.soal1.ReactSys
import kotlinx.coroutines.launch

enum class AppScreen { START_MENU, REACTION_GAME }

@Composable
fun MainAppUI() {
    var currentScreen by remember { mutableStateOf(AppScreen.START_MENU) }

    when (currentScreen) {
        AppScreen.START_MENU -> {
            StartMenuScreen(
                onPlay = { currentScreen = AppScreen.REACTION_GAME },
                onPreviousResult = { /* TODO nanti */ },
                onBackToMenu = { /* TODO nanti */ }
            )
        }

        AppScreen.REACTION_GAME -> {
            ReactionGameUI(
                onExit = { currentScreen = AppScreen.START_MENU }
            )
        }
    }
}



@Composable
fun StartMenuScreen(
    onPlay: () -> Unit,
    onPreviousResult: () -> Unit,
    onBackToMenu: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = onPlay, modifier = Modifier.fillMaxWidth()) {
                Text("Play Game")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onPreviousResult, modifier = Modifier.fillMaxWidth()) {
                Text("Previous Result")
            }
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBackToMenu, modifier = Modifier.fillMaxWidth()) {
                Text("Back to Menu")
            }
        }
    }
}

@Composable
fun ReactionGameUI(
    onExit: () -> Unit
) {
    val game = remember { ReactSys() }
    var lastReaction by remember { mutableStateOf(0L) }
    val scope = rememberCoroutineScope()

    when (game.gameState) {
        ReactSys.GameState.IDLE -> IdleScreen(
            trialResults = game.getResults(),
            onStart = { scope.launch { game.startTrial() } }
        )

        ReactSys.GameState.WAITING -> GamePlayScreen(
            isGo = false,
            onFail = { game.failTrial() },
            onSuccess = {}
        )

        ReactSys.GameState.GO -> GamePlayScreen(
            isGo = true,
            onFail = {},
            onSuccess = { lastReaction = game.recordReaction() }
        )

        ReactSys.GameState.RESULT -> ResultScreen(
            trialNumber = game.getResults().indexOfLast { it >= 0 } + 1,
            totalTrials = 3,
            reactionTime = lastReaction,
            trialResults = game.getResults(),
            onContinue = { game.prepFromResult() }
        )

        ReactSys.GameState.FINISHED -> {
            val eval = game.getEvaluation()
            FinalScreen(
                average = game.getAverage(),
                cardRes = eval,
                trialResults = game.getResults(),
                onRestart = { onExit() }
            )
        }
        ReactSys.GameState.FAIL -> FailScreen(
            trialResults = game.getResults(),
            onRetry = { scope.launch { game.startTrial() } }
        )
    }
}

@Composable
fun GamePlayScreen(
    isGo: Boolean,
    onFail: () -> Unit,
    onSuccess: () -> Unit
) {
    val bgColor = if (isGo) Color(0xFF199119) else Color.LightGray
    val message = if (isGo) "TAP NOW!" else "Wait for Green..."

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable {
                if (isGo) {
                    onSuccess()
                } else {
                    onFail()
                }

            },
        contentAlignment = Alignment.Center
    ) {
        Text(
            message,
            color = Color.White,
            fontSize = 28.sp
        )
    }
}

@Composable
fun IdleScreen(
    trialResults: List<Long>,
    onStart: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Cyan)
            .clickable { onStart() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Reaction", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(20.dp))
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "play icon",
                tint = Color.White,
                modifier = Modifier.size(150.dp)
            )
            Spacer(Modifier.height(20.dp))
            Text("Tap to start", color = Color.White, fontSize = 48.sp, fontWeight = FontWeight.Bold)


        }
    }
}

@Composable
fun ResultScreen(
    trialNumber: Int,
    totalTrials: Int,
    reactionTime: Long,
    trialResults: List<Long>,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF199119))
            .clickable { onContinue() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                "Trial $trialNumber of $totalTrials",
                color = Color.White,
                fontSize = 22.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Your Reaction: ${reactionTime} ms",
                color = Color.Yellow,
                fontSize = 28.sp
            )

            Spacer(Modifier.height(20.dp))

            Text(
                "Tap to continue",
                color = Color.White,
                fontSize = 18.sp
            )

            Spacer(Modifier.height(40.dp))

            Card (
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Trial Results", fontSize = 18.sp)
                    Spacer(Modifier.height(8.dp))
                    Row {
                        trialResults.forEachIndexed { index, score ->
                            val text = if (score >= 0) "${score}ms" else "-"
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("${index + 1}")
                                Text(text)
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun FailScreen(
    trialResults: List<Long>,
    onRetry: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
            .clickable { onRetry() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("FAIL!", color = Color.White, fontSize = 32.sp)

            Spacer(Modifier.height(16.dp))

            Text(
                "You clicked too early,\nTRY TO READ THE RULE BRO",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(24.dp))

            Text(
                "Tap to try again",
                color = Color.White,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(40.dp))

        }
    }
}

@Composable
fun FinalScreen(
    average: Long,
    cardRes: CardRes,
    trialResults: List<Long>,
    onRestart: () -> Unit
) {
    val bgColor = cardRes.color

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable { onRestart() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Final Result", color = Color.White, fontSize = 28.sp)

            Spacer(Modifier.height(20.dp))

            Text("Average: ${average} ms", color = Color.White, fontSize = 24.sp)

            Spacer(Modifier.height(20.dp))

            Image(
                painter = painterResource(id = cardRes.imageRes),
                contentDescription = "evaluation image",
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                cardRes.message,
                color = Color.White,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
                ,fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(30.dp))

            Text("Tap to Start New Test", color = Color.White, fontSize = 18.sp)

            Spacer(Modifier.height(40.dp))

            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Trial Results", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF03A9F4))
                    Spacer(Modifier.height(8.dp))
                    Row {
                        trialResults.forEachIndexed { index, score ->
                            val text = if (score >= 0) "${score}ms" else "-"
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("${index + 1}", fontWeight = FontWeight.Bold, color = Color(
                                    0xFF4CAF50
                                )
                                )
                                Text(text, fontWeight = FontWeight.Bold )
                            }
                        }
                    }
                    Text("Average Score", color = Color(0xFF03A9F4), fontSize = 20.sp)
                    Text("${average} ms", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFA500))

                }
            }
        }
    }
}




@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Soal1Preview() {
    MainAppUI()
}