package com.shv.android.composition.domain.entity

data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val listOptions: List<Int>,
) {
    val rightAnswer: Int
        get() = sum - visibleNumber
}