package com.shv.android.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.shv.android.composition.R
import com.shv.android.composition.databinding.FragmentGameBinding
import com.shv.android.composition.domain.entity.GameResult
import com.shv.android.composition.domain.entity.GameSettings
import com.shv.android.composition.domain.entity.Level

class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    private lateinit var level: Level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
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

        binding.tvSum.setOnClickListener {
            launchGameFinishedFragment()
        }
    }

    private fun parseArgs() {
        level = if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU)
            requireArguments().getSerializable(GAME_LEVEL, Level::class.java) as Level
        else
            requireArguments().getSerializable(GAME_LEVEL) as Level
    }

    private fun launchGameFinishedFragment() {
        val gameResult = GameResult(
            true, 3, 3,
            GameSettings(
                4, 5, 3, 2
            )
        )
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.gameFragmentContainerView, GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val GAME_LEVEL = "game_level"
        const val NAME = "GameFragment"

        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(GAME_LEVEL, level)
                }
            }
        }

    }
}