package com.shv.android.composition.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.shv.android.composition.R
import com.shv.android.composition.domain.entity.GameResult

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_right_answers),
        count
    )
}

@BindingAdapter("yourCount")
fun bindYourCount(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.your_count),
        count
    )
}

@BindingAdapter("requiredPercentAnswers")
fun bindRequiredPercentAnswers(textView: TextView, count: Int) {
    textView.text = String.format(
        textView.context.getString(R.string.required_percent_right_answers),
        count
    )
}

@BindingAdapter("percentRightAnswers")
fun bindPercentRightAnswers(textView: TextView, gameResult: GameResult) {
    textView.text = String.format(
        textView.context.getString(R.string.percent_right_answers),
        getPercentOfRightAnswers(gameResult)
    )
}

@BindingAdapter("imageResult")
fun bindImageResult(imageView: ImageView, winner: Boolean) {
    val resId = getResultImage(winner)
    imageView.setImageResource(resId)
}

private fun getPercentOfRightAnswers(gameResult: GameResult): Int {
    if (gameResult.countOfQuestions == 0) return 0
    return (gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble() * 100).toInt()
}

private fun getResultImage(winState: Boolean): Int {
    return if (winState)
        R.drawable.ic_smile
    else
        R.drawable.ic_sad
}
