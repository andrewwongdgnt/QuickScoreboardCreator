package com.dgnt.quickScoreboardCreator.core.util



fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    if (index1 in 0 until size && index2 in 0 until size && index1 != index2) {
        val temp = this[index1]
        this[index1] = this[index2]
        this[index2] = temp
    }
}