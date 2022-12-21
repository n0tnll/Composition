package com.shv.android.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.shv.android.composition.R
import com.shv.android.composition.domain.entity.GameResult


interface OnOptionClickLister {

    fun onOptionClick(option: Int)
}

//GameFinishedFragment Data Binding

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

//GameFragment Data Binding

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, sum: Int) {
    textView.text = sum.toString()
}

@BindingAdapter(value = ["progress", "secondaryProgress"], requireAll = true)
fun bindProgressState(progressBar: ProgressBar, progress: Int, progressSecondary: Int) {
    progressBar.setProgress(progress, true)
    progressBar.secondaryProgress = progressSecondary
}

@BindingAdapter("setColorText")
fun bindColorText(textView: TextView, enoughCount: Boolean) {
    textView.setTextColor(getColorByState(textView.context, enoughCount))
}

private fun getColorByState(context: Context, state: Boolean): Int {
    val colorResId = if (state)
        android.R.color.holo_green_light
    else
        android.R.color.holo_red_light
    return ContextCompat.getColor(context, colorResId)
}

@BindingAdapter("setColorProgressBar")
fun bindColorProgressBar(progressBar: ProgressBar, enoughPercent: Boolean) {
    progressBar.progressTintList = ColorStateList.valueOf(
        getColorByState(
            progressBar.context,
            enoughPercent
        )
    )
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickLister) {
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}