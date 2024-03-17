package com.dgnt.quickScoreboardCreator.manager.scoreTransformer


class QSBScoreTransformer : ScoreTransformer {

    var map: Map<Int, String> = mapOf()
    override fun provideTransformMap(map: Map<Int, String>) {
        this.map = map
    }

    override fun transform(list: List<Int>) =
        list.map {
            map[it] ?: it.toString()
        }


}