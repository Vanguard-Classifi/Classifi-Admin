package com.vanguard.classifiadmin.domain.extensions

import java.text.DecimalFormat

fun List<Float>.sum(): Float {
    var sum = 0.0f
    forEach { sum += it }
    return sum
}

fun List<Float>.frequencies(): Map<Float, Int> {
    var key = 0.0f
    var frequency = 1
    var container = mutableMapOf<Float, Int>()
    val items = this.sorted()

    for (index in indices) {
        if (index == 0) {
            container[items[index]] = frequency
            continue
        }

        if (items[index - 1] == items[index]) {
            container[items[index]] = ++frequency
        } else {
            frequency = 1
            container[items[index]] = frequency
        }
    }

    return container
}

fun Float.toGrade(): Char {
    return when {
        this in 0.75f..1f -> 'A'
        0.60f <= this && this < 0.75f -> 'B'
        0.50f <= this && this < 0.60f -> 'C'
        0.45 <= this && this < 0.50f -> 'D'
        0.40f <= this && this < 0.45f -> 'E'
        0 <= this && this < 0.40f -> 'F'
        else -> 'F'
    }
}

fun List<Float>.average(): Float {
    val sum = sum()
    return sum / size
}

fun Float.toPercentage(): String {
    val percentage = DecimalFormat("0.00").format(this * 100)
    return "$percentage%"
}