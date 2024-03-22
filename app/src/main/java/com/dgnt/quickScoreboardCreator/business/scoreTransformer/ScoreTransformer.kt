package com.dgnt.quickScoreboardCreator.business.scoreTransformer

interface ScoreTransformer {

    fun provideTransformMap(map: Map<Int, String>)
    fun transform(list: List<Int>): List<String>
}