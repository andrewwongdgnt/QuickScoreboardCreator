package com.dgnt.quickScoreboardCreator.feature.scoreboard.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
import javax.inject.Inject


class CalculateWinUseCase @Inject constructor() {

    private val scoreMap = mutableMapOf<Int, List<ScoreData>>()
    fun store(scoreInfo: ScoreInfo, intervalIndex: Int) {
        scoreMap[intervalIndex] = scoreInfo.dataList.map { it.primary }
    }

    fun calculate(winRule: WinRule): Set<Int> {
        val listOfScores = scoreMap.toList().sortedBy { it.first }.map { it.second.map { scoreData -> scoreData.current } }
        return when (winRule) {
            is WinRule.Final -> listOfScores.last().let { scoreList ->
                if (scoreList.isEmpty())
                    emptySet()
                else {
                    indexOfMax(scoreList)
                }
            }

            WinRule.Count -> listOfScores.map { scoreList ->
                indexOfMax(scoreList).associateWith { 1 }
            }.reduce { acc, ele ->
                addMaps(acc, ele)
            }.let {
                findMaxValues(it)
            }

            WinRule.Sum -> {
                listOfScores.reduce { acc, ele ->
                    acc.indices.map { i ->
                        acc[i] + ele[i]
                    }

                }.let {
                    indexOfMax(it)
                }
            }
        }
    }

    private fun indexOfMax(scores: List<Int>): Set<Int> {
        val max = scores.maxByOrNull { it }
        return scores.mapIndexedNotNull { index, elem -> if (elem == max) index else null }.toSet()
    }

    private fun addMaps(map1: Map<Int, Int>, map2: Map<Int, Int>): Map<Int, Int> {
        val result = mutableMapOf<Int, Int>()

        // Add all the entries from map1 to the result.
        for ((key, value) in map1) {
            result[key] = value
        }

        // Add all the entries from map2 to the result, summing the values for any keys that are already in the result.
        for ((key, value) in map2) {
            result[key] = result.getOrDefault(key, 0) + value
        }

        return result
    }

    private fun findMaxValues(map: Map<Int, Int>): Set<Int> {
        // Find the maximum value in the map.
        val maxValue = map.values.maxOrNull()

        // Create a set to store the keys with the maximum value.
        val maxKeys = mutableSetOf<Int>()

        // Iterate through the map and add keys with the maximum value to the set.
        for ((key, value) in map) {
            if (value == maxValue) {
                maxKeys.add(key)
            }
        }

        // Return the set of keys with the maximum value.
        return maxKeys
    }
}