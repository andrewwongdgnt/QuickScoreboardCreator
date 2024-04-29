package com.dgnt.quickScoreboardCreator.ui.common

fun Int.asIncrementDisplay() = if (this >= 0) "+$this" else "-$this"