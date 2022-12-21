package com.shv.android.composition.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shv.android.composition.R
import com.shv.android.composition.data.GameRepositoryImpl
import com.shv.android.composition.domain.entity.GameResult
import com.shv.android.composition.domain.entity.GameSettings
import com.shv.android.composition.domain.entity.Level
import com.shv.android.composition.domain.entity.Question
import com.shv.android.composition.domain.usecases.GenerateQuestionUseCase
import com.shv.android.composition.domain.usecases.GetGameSettingsUseCase

class GameViewModel(
    private val application: Application,
    private val level: Level
) : ViewModel() {

    private val repository = GameRepositoryImpl

    private val getGameSettingsUseCase = GetGameSettingsUseCase(repository)
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)

    private var timer: CountDownTimer? = null

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private lateinit var gameSetting: GameSettings

    private var countOfRightAnswers = 0
    private var countOfQuestions = 0

    private val _percentRightAnswers = MutableLiveData<Int>()
    val percentRightAnswers: LiveData<Int>
        get() = _percentRightAnswers

    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _enoughCountOfRightAnswers = MutableLiveData<Boolean>()
    val enoughCountOfRightAnswers: LiveData<Boolean>
        get() = _enoughCountOfRightAnswers

    private val _enoughPercentOfRightAnswers = MutableLiveData<Boolean>()
    val enoughPercentOfRightAnswers: LiveData<Boolean>
        get() = _enoughPercentOfRightAnswers

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    init {
        startGame()
    }

    private fun startGame() {
        getGameSettings()
        startTimer()
        loadQuestion()
        updateProgress()
    }

    private fun getGameSettings() {
        this.gameSetting = getGameSettingsUseCase(level)
        _minPercent.value = gameSetting.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSetting.gameTimeInSeconds * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                endGame()
            }

        }
        timer?.start()
    }

    private fun endGame() {
        _gameResult.value = GameResult(
            enoughCountOfRightAnswers.value == true && enoughPercentOfRightAnswers.value == true,
            countOfRightAnswers,
            countOfQuestions,
            gameSetting
        )
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECOND
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)

        return String.format("%02d:%02d", minutes, leftSeconds)
    }

    fun loadQuestion() {
        _question.value = generateQuestionUseCase(gameSetting.maxSumValue)
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        loadQuestion()
    }

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestions++
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentRightAnswers.value = percent
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.counter_right_answers),
            countOfRightAnswers,
            gameSetting.minCountOfRightAnswers.toString()
        )
        _enoughCountOfRightAnswers.value =
            countOfRightAnswers >= gameSetting.minCountOfRightAnswers
        _enoughPercentOfRightAnswers.value =
            percent >= gameSetting.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestions == 0) return 0
        return ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECOND = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }
}