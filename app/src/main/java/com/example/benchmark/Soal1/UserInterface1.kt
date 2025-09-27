package com.example.benchmark.Soal1

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.Warning
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
fun ReactionApp(){
    ReactionGameUI()
}
@Composable
fun ReactionGameUI() {
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
                onRestart = { game.resetAll() }
            )
        }
        ReactSys.GameState.FAIL -> FailScreen(
            trialResults = game.getResults(),
            onRetry = { game.resetAfterFail() }
        )
    }
}



@Composable
fun GamePlayScreen(
    isGo: Boolean,
    onFail: () -> Unit,
    onSuccess: () -> Unit
) {
    val bgColor = if (isGo) Color(0xFF4CAF50) else Color(0xFFE0E0E0)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .clickable { if (isGo) onSuccess() else onFail() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (isGo) {
                Text("GO!", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.DirectionsRun,
                    contentDescription = "Run",
                    tint = Color.White,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(Modifier.height(24.dp))
                Text("CLICK NOW!", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("TAP AS FAST AS YOU CAN!", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
            } else {
                Text("Get Ready", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(16.dp))
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Warning",
                    tint = Color.White,
                    modifier = Modifier.size(96.dp)
                )
                Spacer(Modifier.height(24.dp))
                Text("Wait for green light...", color = Color.White, fontSize = 18.sp)
                Spacer(Modifier.height(8.dp))
                Text("DON'T CLICK YET!", color = Color.White.copy(alpha = 0.9f), fontSize = 14.sp)
            }
        }
    }
}


@Composable
fun IdleScreen(
    trialResults: List<Long>,
    onStart: () -> Unit
) {
    val bg = Color(0xFF7CC9D0)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .clickable { onStart() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Reaction", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Default.FlashOn,
                contentDescription = "Flash",
                tint = Color.White,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Test", color = Color.White.copy(alpha = 0.9f), fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            Text("Click to Start", color = Color.White.copy(alpha = 0.8f), fontSize = 16.sp)

            Spacer(Modifier.height(40.dp))
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
    val bg = Color(0xFF4CAF50)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .clickable { onContinue() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Trial $trialNumber Complete!", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = "Success",
                tint = Color.White,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text("Time: ${reactionTime}ms", color = Color.White, fontSize = 18.sp)
            Spacer(Modifier.height(8.dp))
            val next = (trialNumber + 1).coerceAtMost(totalTrials)
            if (trialNumber < totalTrials) {
                Text("Continue to Trial $next", color = Color.White.copy(alpha = 0.95f), fontSize = 16.sp)
            } else {
                Text("Tap to finish", color = Color.White.copy(alpha = 0.95f), fontSize = 16.sp)
            }
            Spacer(Modifier.height(24.dp))
            TrialResultsCard(trialResults)
        }
    }
}



@Composable
fun FailScreen(
    trialResults: List<Long>,
    onRetry: () -> Unit
) {
    val bg = Color(0xFFF44336)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bg)
            .clickable { onRetry() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("FAIL!", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Default.ThumbDown,
                contentDescription = "Dislike",
                tint = Color.White,
                modifier = Modifier.size(96.dp)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                "You clicked too early, TRY TO\nREAD THE RULE BRO",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(16.dp))
            Text("TRY AGAIN", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
            Spacer(Modifier.height(24.dp))
            TrialResultsCard(trialResults)
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
                                Text("${index + 1}", fontWeight = FontWeight.Bold, color = Color(0xFF4CAF50))
                                Text(text, fontWeight = FontWeight.Bold )
                            }
                        }
                    }
                    Text("Average Score", color = Color(0xFF03A9F4), fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text("${average} ms", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFFA500))

                }
            }
        }
    }
}

@Composable
fun TrialResultsCard(trialResults: List<Long>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Trial Results", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF2196F3))
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                trialResults.forEachIndexed { index, score ->
                    val text = if (score >= 0) "${score}ms" else "-"
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("${index + 1}", fontWeight = FontWeight.Medium, color = Color(0xFF4CAF50))
                        Text(text)
                    }
                }
            }
        }
    }
}



@Preview(showSystemUi = true, showBackground = true)
@Composable
fun Soal1Preview() {
ReactionApp()
}