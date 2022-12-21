package com.shv.android.composition.presentation

import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.shv.android.composition.R
import com.shv.android.composition.databinding.FragmentGameBinding
import com.shv.android.composition.domain.entity.GameResult
import com.shv.android.composition.domain.entity.Level

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private val args by navArgs<GameFragmentArgs>()

    private val gameViewModelFactory: GameViewModelFactory by lazy {
        GameViewModelFactory(requireActivity().application, args.level)
    }

    private val gameViewModel: GameViewModel by lazy {
        ViewModelProvider(this, gameViewModelFactory)[GameViewModel::class.java]
    }

    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            with(binding) {
                add(tvOption1)
                add(tvOption2)
                add(tvOption3)
                add(tvOption4)
                add(tvOption5)
                add(tvOption6)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModels()
        setupOnClickListenersToOptions()

    }

    private fun observeViewModels() {
        with(binding) {
            gameViewModel.formattedTime.observe(viewLifecycleOwner) {
                tvTimer.text = it
            }

            gameViewModel.question.observe(viewLifecycleOwner) {
                tvSum.text = it.sum.toString()
                tvVisibleNumber.text = it.visibleNumber.toString()

                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.listOptions[i].toString()
                }
            }

            gameViewModel.progressAnswers.observe(viewLifecycleOwner) {
                tvCountRightAnswers.text = it
            }

            gameViewModel.percentRightAnswers.observe(viewLifecycleOwner) {
                pbProgress.setProgress(it, true)
            }

            gameViewModel.enoughCountOfRightAnswers.observe(viewLifecycleOwner) {
                tvCountRightAnswers.setTextColor(getColorByState(it))
            }

            gameViewModel.enoughPercentOfRightAnswers.observe(viewLifecycleOwner) {
                val color = getColorByState(it)
                pbProgress.progressTintList = ColorStateList.valueOf(color)
            }

            gameViewModel.minPercent.observe(viewLifecycleOwner) {
                pbProgress.secondaryProgress = it
            }

            gameViewModel.gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }
        }
    }


    private fun setupOnClickListenersToOptions() {
        for (tvOption in tvOptions) {
            tvOption.setOnClickListener {
                gameViewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }

    private fun getColorByState(state: Boolean): Int {
        val colorResId = if (state)
            android.R.color.holo_green_light
        else
            android.R.color.holo_red_light
        return ContextCompat.getColor(requireContext(), colorResId)
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}