package com.shv.android.composition.data

import com.shv.android.composition.domain.entity.GameSettings
import com.shv.android.composition.domain.entity.Level
import com.shv.android.composition.domain.entity.Question
import com.shv.android.composition.domain.repository.GameRepository
import kotlin.math.max
import kotlin.math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_VALUE_ANSWER = 1

    override fun generateQuestion(maxSumValue: Int, countOfOptions: Int): Question {
        val sum = Random.nextInt(MIN_SUM_VALUE, maxSumValue + 1)
        val visibleNumber = Random.nextInt(MIN_VALUE_ANSWER, sum)

        val options = HashSet<Int>()
        val rightAnswer = sum - visibleNumber
        options.add(rightAnswer)
        val from = max(rightAnswer - countOfOptions, MIN_VALUE_ANSWER)
        val to = min(maxSumValue, rightAnswer - countOfOptions)

        while (options.size < countOfOptions) {
            val option = Random.nextInt(from, to)
            options.add(option)
        }

        return Question(sum, visibleNumber, options.toList())
    }

    override fun getGameSettings(level: Level): GameSettings {
        return when(level) {
            Level.TEST -> {
                GameSettings(
                    10,
                    3,
                    50,
                8
                )
            }
            Level.EASY -> {
                GameSettings(
                    10,
                    10,
                    70,
                    60
                )
            }
            Level.NORMAL -> {
                GameSettings(
                    20,
                    20,
                    80,
                    40
                )
            }
            Level.HARD -> {
                GameSettings(
                    30,
                    30,
                    90,
                    40
                )
            }
        }
    }
}