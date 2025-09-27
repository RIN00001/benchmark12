package com.example.soal1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.benchmark.R
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

data class CardRes(
    val message: String,
    val color: Color,
    val imageRes: Int
)


class ReactSys(private val trialCount: Int = 3) {


    enum class GameState { IDLE, WAITING, GO, RESULT, FINISHED ,FAIL}


    var gameState by mutableStateOf(GameState.IDLE)
        private set
    private var startTime: Long = 0
    private var currentTrial = 0
    private val trialResults = LongArray(trialCount) { -1 }
    private var trialJob: Job? = null
    suspend fun startTrial() {
        trialJob?.cancel()
        trialJob = null

        gameState = GameState.WAITING
        val delayTime = Random.nextLong(2000, 5000)

        trialJob = GlobalScope.launch {
            delay(delayTime)

            if (gameState == GameState.WAITING) {
                startTime = System.currentTimeMillis()
                gameState = GameState.GO
            }
        }
    }


    fun recordReaction(): Long {
        if (gameState != GameState.GO) return -1
        val tapTime = System.currentTimeMillis()
        val reaction = tapTime - startTime
        trialResults[currentTrial] = reaction
        gameState = GameState.RESULT
        return reaction
    }


    fun failTrial() {
        if (gameState == GameState.WAITING) {
            trialJob?.cancel()
            trialJob = null
            gameState = GameState.FAIL
        }
    }
    fun prepFromResult(){
        if (gameState != GameState.RESULT) return
        currentTrial++
        gameState = if (currentTrial >= trialCount) GameState.FINISHED else GameState.IDLE
    }

    fun getResults(): List<Long> = trialResults.toList()

    fun getAverage(): Long {
        val validScores = trialResults.filter { it >= 0 }
        return if (validScores.isNotEmpty()) validScores.sum() / validScores.size else 0
    }


    fun getEvaluation(): CardRes {
        val avg = getAverage()
        return when {
            avg > 600 -> CardRes(
                "YOU LIKE A SNAIL BRO",
                Color(0xFF961C14),
                R.drawable.norispek
            )
            avg > 400 -> CardRes(
                "MEH LIKE OTHER PERSON",
                Color(0xFFFF9800),
                R.drawable.mid
            )
            avg > 200 -> CardRes(
                "YOUR REFLEX IS GOOD",
                Color(0xFF182C9D),
                R.drawable.good
            )
            else -> CardRes(
                "DANG YOU ARE SO FAST BRO!",
                Color(0xFF90EE90),
                R.drawable.sweat
            )
        }
    }

    fun resetAfterFail() {
        // make sure no stale job runs
        trialJob?.cancel()
        trialJob = null
        if (gameState == GameState.FAIL) gameState = GameState.IDLE
    }


    fun resetAll() {
        trialJob?.cancel()
        trialJob = null
        startTime = 0L
        currentTrial = 0
        for (i in trialResults.indices) trialResults[i] = -1
        gameState = GameState.IDLE
    }
}
