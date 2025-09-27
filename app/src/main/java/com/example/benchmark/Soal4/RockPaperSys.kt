package com.example.benchmark.Soal4

import kotlin.random.Random


enum class Move { ROCK, PAPER, SCISSORS}

enum class Result { WIN, LOSE, DRAW}
data class RoundResult(
    val playerMove: Move,
    val aiMove: Move,
    val result: Result
)

class RockPaperSys(){

    var pScore: Int = 0
        private set
    var aiScore: Int = 0
        private set
    var bestScore: Int = 0
        private set

    var bestOfN: Int = 5
        private set

    private var winTarget: Int = 3

    private val random = Random(System.currentTimeMillis())

    fun startNewGame() {
        bestOfN = listOf(3,5,7).random(random)
        winTarget = (bestOfN / 2) + 1

        pScore = 0
        aiScore = 0
    }
    fun playRound(playerMove: Move): RoundResult {
        val aiMove = Move.values().random(random)
        val result = getResult(playerMove, aiMove)

        when (result) {
            Result.WIN -> pScore++
            Result.LOSE -> aiScore++
            Result.DRAW ->{}
        }

        return RoundResult(playerMove, aiMove, result)
    }
    private fun getResult(player: Move, ai: Move): Result {
        if (player == ai) return Result.DRAW

        return when (player) {
            Move.ROCK -> if (ai == Move.SCISSORS) Result.WIN else Result.LOSE
            Move.PAPER -> if (ai == Move.ROCK) Result.WIN else Result.LOSE
            Move.SCISSORS -> if (ai == Move.PAPER) Result.WIN else Result.LOSE
        }
    }

    fun isGameOver(): Boolean {
        return pScore >= winTarget || aiScore >= winTarget
    }

    fun getWinner(): String? {
        if (pScore > bestScore) {
            bestScore = pScore
        }

        return when {
            pScore >= winTarget -> "Player"
            aiScore >= winTarget -> "AI"
            else -> null
        }
    }

}