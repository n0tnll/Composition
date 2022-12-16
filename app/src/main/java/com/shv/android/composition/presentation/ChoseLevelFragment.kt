package com.shv.android.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.shv.android.composition.R
import com.shv.android.composition.databinding.FragmentChoseLevelBinding
import com.shv.android.composition.domain.entity.Level

class ChoseLevelFragment : Fragment() {

    private var _binding: FragmentChoseLevelBinding? = null
    private val binding: FragmentChoseLevelBinding
        get() = _binding ?: throw RuntimeException("FragmentChoseLevelBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChoseLevelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupOnClickListeners()
    }


    private fun setupOnClickListeners() {
        with(binding) {
            btnLevelTest.setOnClickListener {
                launchGameFragment(Level.TEST)
            }
            btnLevelEasy.setOnClickListener {
                launchGameFragment(Level.EASY)
            }
            btnLevelNormal.setOnClickListener {
                launchGameFragment(Level.NORMAL)
            }
            btnLevelHard.setOnClickListener {
                launchGameFragment(Level.HARD)
            }
        }
    }

    private fun launchGameFragment(level: Level) {
        val fragment = GameFragment.newInstance(level)
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.gameFragmentContainerView, fragment)
            .addToBackStack(GameFragment.NAME)
            .commit()
    }

    companion object {


        const val NAME = "ChoseLevelFragment"

        fun newInstance(): ChoseLevelFragment {
            return ChoseLevelFragment()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}