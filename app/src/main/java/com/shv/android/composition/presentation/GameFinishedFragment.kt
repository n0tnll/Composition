package com.shv.android.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shv.android.composition.R
import com.shv.android.composition.databinding.FragmentGameFinishedBinding
import com.shv.android.composition.domain.entity.GameResult

class GameFinishedFragment : Fragment() {


    private var _binding: FragmentGameFinishedBinding? = null
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishedBinding == null")

    private val args by navArgs<GameFinishedFragmentArgs>()

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
        binding.btnPlayAgain.setOnClickListener {
            retryGame()
        }
    }

    private fun showResult() {
        with(binding) {
            val drawableImg = getResultImage(args.gameResult.winner)
            imResult.setImageResource(drawableImg)

            val minCountOfRightAnswers = String.format(
                getString(R.string.required_right_answers),
                args.gameResult.gameSettings.minCountOfRightAnswers
            )
            tvRequiredAnswers.text = minCountOfRightAnswers

            val yourCount = String.format(
                getString(R.string.your_count),
                args.gameResult.countOfRightAnswers
            )
            tvYourCount.text = yourCount

            val requiredPercent = String.format(
                getString(R.string.required_percent_right_answers),
                args.gameResult.gameSettings.minPercentOfRightAnswers
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
        args.gameResult.countOfRightAnswers / args.gameResult.countOfQuestions.toDouble() * 100

    private fun getResultImage(winState: Boolean): Int {
        return if (winState)
            R.drawable.ic_smile
        else
            R.drawable.ic_sad
    }

    private fun retryGame() {
        findNavController().popBackStack()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}