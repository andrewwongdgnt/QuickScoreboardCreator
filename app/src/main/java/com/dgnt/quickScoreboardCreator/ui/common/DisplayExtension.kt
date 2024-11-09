package com.dgnt.quickScoreboardCreator.ui.common

fun Int.asIncrementDisplay() = if (this >= 0) "+$this" else "$this"

/**
 * Assuming the string is supposed to be a number
 *
 */
fun String.asIncrementDisplay(): String {
    val main = (if (length <= 1) this
    else substring(1)).filter { it.isDigit() }

    val first = if (length > 1)
        first().let {
            if (it.isDigit() || it == '+')
                it.toString()
            else
                "-"
        } else ""

    return (first + main).toIntOrNull()?.asIncrementDisplay() ?: ""


}