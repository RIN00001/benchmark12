package com.example.benchmark.Soal3

import android.app.Activity
import android.widget.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
@Preview(showBackground = true, showSystemUi = true)
fun Soal3Preview(){
 UserIn()
}


@Composable
fun UserIn(){
    val game = remember { ColorGuessSys() }
    var currentScreen by remember { mutableStateOf("Welcome") }
    var round by remember { mutableStateOf<Round?>(null) }
    var timer by remember { mutableStateOf(5) }

    when (currentScreen){
        "Welcome" -> WelcomeScreen { currentScreen = "Countdown" }

        "Countdown" -> CountdownScreen(
            onFinish = {
                round = game.questionGenerator()
                timer = 5
                currentScreen = "Game"
            }
        )

        "Game" -> GameScreen(
            game = game,
            round = round!!,
            timer = timer,
            onTimeOut = {
                game.submitAnswer("", round!!.correctAnswer)
                if (game.isGameOver()){
                    currentScreen = "GameOver"
                } else {
                    round = game.questionGenerator()
                    timer = 5
                }
            },
            onAnswer = { answer ->
                game.submitAnswer(answer, round!!.correctAnswer)
                if (game.isGameOver()) {
                    currentScreen = "GameOver"
                } else {
                    round = game.questionGenerator()
                    timer = 5
                }
            },
            onTick = { newtime -> timer = newtime }
        )
        "GameOver" -> {
            val context = LocalContext.current
            GameOverScreen(
            score = game.score
            , bestScore = game.bestScore,
            onRestart = {
                game.gameReset()
                currentScreen = "Countdown"
            },
            onExit = {
                (context as? Activity)?.finish()
            }
        )}
    }

}

@Composable
fun WelcomeScreen(onStart: () -> Unit){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Welcome", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(8.dp))
        Text("to", fontSize = 28.sp, color = Color.Black)
        Spacer(Modifier.height(8.dp))
        Text("Color Word Matching", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = onStart,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Start Game", color = Color.White, fontSize = 20.sp)
        }
    }
}


@Composable
fun CountdownScreen(onFinish: () -> Unit) {
    var counter by remember { mutableStateOf(3) }
    var showStart by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (counter > 0) {
            delay(1000)
            counter--
        }
        showStart = true
        delay(1000)
        onFinish()
    }

    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (!showStart) counter.toString() else "Start!",
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
fun GameScreen(
    game: ColorGuessSys,
    round: Round,
    timer: Int,
    onTimeOut: () -> Unit,
    onAnswer: (String) -> Unit,
    onTick: (Int) -> Unit
){
    LaunchedEffect(round) {
        var time = 5
        while (time > 0) {
            delay(1000)
            time--
            onTick(time)
        }
        if (time == 0) {
            onTimeOut()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Mode: ${round.mode}", fontSize = 18.sp)
            Text("✔ ${game.score}", fontSize = 18.sp, color = Color.Green, fontWeight = FontWeight.Bold)
            Text("❌ ${game.strike}/3", fontSize = 18.sp, color = Color.Red, fontWeight = FontWeight.Bold)
        }

        Spacer(Modifier.height(24.dp))
        Text("$timer s", fontSize = 28.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(24.dp))

        Text(
            text = round.wordText,
            color = round.inkColor,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(50.dp)
        )

        Spacer(Modifier.height(100.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            round.option.forEach { option ->
                Button(
                    onClick = { onAnswer(option) },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(option, fontSize = 20.sp, color = Color.White)
                }
            }
        }
    }
}

@Composable
fun GameOverScreen(
    score: Int,
    bestScore: Int,
    onRestart: () -> Unit,
    onExit: () -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game Over!", fontSize = 36.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("Your Score", fontSize = 20.sp)
        Text("$score", fontWeight = FontWeight.Bold, fontSize = 24.sp)
        Spacer(Modifier.height(8.dp))
        Text("Best Score", fontSize = 16.sp)
        Text("$bestScore", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(Modifier.height(32.dp))
            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Restart Game", color = Color.White, fontSize = 18.sp)
            }
            Button(
                onClick = onExit,
                colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text("Exit", color = Color.White, fontSize = 18.sp)
            }

    }
}

