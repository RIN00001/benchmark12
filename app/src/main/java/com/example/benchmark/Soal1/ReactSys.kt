package com.example.soal1

import com.example.benchmark.R
import kotlinx.coroutines.delay
import kotlin.random.Random

data class Evaluation(
    val message: String,
    val colorHex: String,
    val imageRes: Int
)


class ReactSys(private val trialCount: Int = 3) {
val red: String = "#FF0000"
val orange: String = "#FFA500"
val blue: String = "#0000FF"
val green: String = "#00FF00"

    enum class GameState { IDLE, WAITING, GO, RESULT, FINISHED }

    var gameState: GameState = GameState.IDLE
        private set

    private var startTime: Long = 0
    private var currentTrial = 0
    private val trialResults = LongArray(trialCount) { -1 }

    suspend fun startTrial(): Long {
        gameState = GameState.WAITING
        val delayTime = Random.nextLong(2000, 5000) // 2–5 seconds
        delay(delayTime)
        startTime = System.currentTimeMillis()
        gameState = GameState.GO
        return startTime
    }

    fun recordReaction(): Long {
        if (gameState != GameState.GO) return -1
        val tapTime = System.currentTimeMillis()
        val reaction = tapTime - startTime
        trialResults[currentTrial] = reaction
        gameState = GameState.RESULT

        currentTrial++
        if (currentTrial >= trialCount) {
            gameState = GameState.FINISHED
        } else {
            gameState = GameState.IDLE
        }
        return reaction
    }


    fun failTrial() {
        if (gameState == GameState.WAITING) {
            // Do NOT increment trial, retry same one
            gameState = GameState.IDLE
        }
    }

    fun getResults(): List<Long> = trialResults.toList()

    fun isGameOver(): Boolean = gameState == GameState.FINISHED

    fun getAverage(): Long {
        val validScores = trialResults.filter { it >= 0 }
        return if (validScores.isNotEmpty()) validScores.sum() / validScores.size else 0
    }


    fun getEvaluation(): Evaluation {
        val avg = getAverage()
        return when {
            avg > 600 -> Evaluation(
                "YOU LIKE A SNAIL BRO",
                "#FF0000",
                R.drawable.norispek
            )
            avg > 400 -> Evaluation(
                "MEH LIKE OTHER PERSON",
                "#FFA500",
                R.drawable.mid
            )
            avg > 200 -> Evaluation(
                "YOUR REFLEX IS GOOD",
                "#0000FF",
                R.drawable.good
            )
            else -> Evaluation(
                "DANG YOU ARE SO FAST BRO!",
                "#00FF00",
                R.drawable.sweat
            )
        }
    }

}
