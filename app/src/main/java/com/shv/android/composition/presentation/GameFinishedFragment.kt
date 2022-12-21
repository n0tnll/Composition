package com.shv.android.composition.presentation

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.shv.android.composition.R
import com.shv.android.composition.databinding.FragmentGameFinishedBinding
import com.shv.android.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {


    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private lateinit var gameResult: GameResult

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListeners()
        showResult()
    }

    private fun setupOnClickListeners() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                retryGame()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.btnPlayAgain.setOnClickListener {
            retryGame()
        }
    }


    private fun parseArgs() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(GAME_RESULT, GameResult::class.java)?.let {
                gameResult = it
            }
        } else
            requireArguments().getParcelable<GameResult>(GAME_RESULT)?.let {
                gameResult = it
            }
    }

    private fun showResult() {
        with(binding) {
            val drawableImg = getResultImage(gameResult.winner)
            imResult.setImageResource(drawableImg)

            val minCountOfRightAnswers = String.format(
                getString(R.string.required_right_answers),
                gameResult.gameSettings.minCountOfRightAnswers
            )
            tvRequiredAnswers.text = minCountOfRightAnswers

            val yourCount = String.format(
                getString(R.string.your_count),
                gameResult.countOfRightAnswers
            )
            tvYourCount.text = yourCount

            val requiredPercent = String.format(
                getString(R.string.required_percent_right_answers),
                gameResult.gameSettings.minPercentOfRightAnswers
            )
            tvRequiredPercentAnswers.text = requiredPercent

            val percentRightAnswers = String.format(
                resources.getString(R.string.percent_right_answers),
                getPercentOfRightAnswers()
            )
            tvPercentRightAnswers.text = percentRightAnswers
        }
    }

    private fun getPercentOfRightAnswers() =
        gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble() * 100

    private fun getResultImage(winState: Boolean): Int {
        return if (winState)
            R.drawable.ic_smile
        else
            R.drawable.ic_sad
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager.popBackStack(
            GameFragment.NAME,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(GAME_RESULT, gameResult)
                }
            }
        }
    }
}