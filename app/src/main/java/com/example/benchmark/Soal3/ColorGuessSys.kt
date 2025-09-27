package com.example.benchmark.Soal3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlin.random.Random


//Bisa pake text tapi pengen coba pake ENUM
enum class GameMode{
    TEXT, COLOR
}


//Setor data yang aku perluin tiap ronde
data class Round(
    val wordText: String,
    val inkColor: Color,
    val inkColorName: String,
    val mode: GameMode,
    val option: List<String>,
    val correctAnswer: String
)



class ColorGuessSys() {


    private val colors = listOf(
        "Red" to Color.Red,
        "Green" to Color.Green,
        "Blue" to Color.Blue,
        "Yellow" to Color.Yellow,
        "Black" to Color.Black,
        "White" to Color.White,
        "Purple" to Color(0xFF800080),
        "Orange" to Color(0xFFFFA500)
    )

    var strike: Int = 0
        private set
    var score: Int = 0
        private set
    var bestScore: Int = 0
        private set

    private val random = Random(System.currentTimeMillis())

    fun questionGenerator(): Round{
        val mode = if (random.nextBoolean()) GameMode.TEXT else GameMode.COLOR

        val (wordText, _) = colors.random(random)
        val (inkColorName, inkColor) = colors.random(random)

        val correctAnswer: String
        val wrongAnswer: String

        if (mode == GameMode.TEXT) {
            correctAnswer = wordText
            wrongAnswer = inkColorName
        } else {
            correctAnswer = inkColorName
            wrongAnswer = wordText
        }

        val options = listOf(correctAnswer, wrongAnswer).shuffled(random)

        return Round(
            wordText = wordText,
            inkColor = inkColor,
            inkColorName = inkColorName,
            mode = mode,
            option = options,
            correctAnswer = correctAnswer
        )
    }

    fun submitAnswer(answer: String, correctAnswer: String): Boolean{
        return if (answer == correctAnswer){
            score += 1
            return true
        } else {
            strike += 1
            false
        }
    }

    fun gameReset(){
        strike = 0
        score = 0
    }

    fun isGameOver(): Boolean {
        if (strike >= 3) {
            if (score > bestScore) {
                bestScore = score
            }
            return true
        }
        return false
    }


}