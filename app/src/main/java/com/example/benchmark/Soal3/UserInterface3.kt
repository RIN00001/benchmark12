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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay


@Composable
@Preview
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
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Button(onClick = onStart) {
            Text("Start Game")
        }
    }
}


@Composable
fun CountdownScreen(onFinish: () -> Unit){
    var counter by remember { mutableStateOf(5)}

    LaunchedEffect(Unit) {
        while (counter > 0){
            delay(1000)
            counter--
        }
        onFinish()
    }
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (counter > 0) counter.toString() else "Start!",
            fontSize = 40.sp,
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
        while (time > 0){
            delay(1000)
            time--
            onTick(time)
        }
        if (time == 0){
            onTimeOut()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Mode: ${round.mode}")
            Text("Score: ${game.score}")
            Text("Strikes: ${game.strike}/3")
        }

        Text("Time left: $timer", fontSize = 24.sp)

        Text(
            text = round.wordText,
            color = round.inkColor,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            round.option.forEach { option ->
                Button(onClick = { onAnswer(option) }) {
                    Text(option)
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
        , verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Game over", fontSize = 32.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        Text("Score: $score", fontSize = 24.sp)
        Text("Best Score: $bestScore", fontSize = 24.sp)
        Spacer(Modifier.height(32.dp))
        Row {
            Button(onClick = onRestart, modifier = Modifier.padding(8.dp)) {
                Text("Restart")
            }
            Button(onClick = onExit, modifier = Modifier.padding(8.dp)) {
                Text("Exit")
        }
    }
}
}

