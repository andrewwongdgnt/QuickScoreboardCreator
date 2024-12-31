package com.dgnt.quickScoreboardCreator.feature.sport.data.filedto

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreData
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreGroup
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule


abstract class SportFileDTO {
    val type: String = ""
    val winRuleType: WinRuleType = WinRuleType.FINAL
    var intervalList: List<IntervalFileDTO> = emptyList()
    val repeatRule: List<RepeatRuleFileDTO> = emptyList()
}

data class IntervalFileDTO(
    val scoreInfo: ScoreInfoFileDTO,
    val intervalData: IntervalDataFileDTO
)

data class RepeatRuleFileDTO(
    val from: Int,
    val to: List<Int>
)

data class ScoreInfoFileDTO(
    val scoreRule: ScoreRuleFileDTO,
    val scoreMapping: Map<String, String>?,
    val secondaryScoreLabel: String?,
    val dataList: List<ScoreGroupFileDTO>
) {
    fun toScoreInfo() =
        ScoreInfo(
            scoreRule.toScoreRule(),
            scoreMapping?.mapKeys { it.key.toInt() } ?: mapOf(),
            secondaryScoreLabel ?: "",
            dataList.map {
                it.toScoreGroup()
            }
        )
}

data class ScoreRuleFileDTO(
    val type: ScoreRuleType,
    val trigger: Int
) {
    fun toScoreRule() =
        when (type) {
            ScoreRuleType.NO_RULE -> ScoreRule.None
            ScoreRuleType.MAX_RULE -> ScoreRule.Trigger.Max(trigger)
            ScoreRuleType.DEUCE_ADVANTAGE -> ScoreRule.Trigger.DeuceAdvantage(trigger)
        }
}

data class ScoreGroupFileDTO(
    val primary: ScoreDataFileDTO,
    val secondary: ScoreDataFileDTO?
) {
    fun toScoreGroup() =
        ScoreGroup(
            primary.toScoreData(),
            secondary?.toScoreData(),
        )
}

data class ScoreDataFileDTO(
    val initial: Int,
    val increments: List<Int>
) {
    fun toScoreData() =
        ScoreData(
            initial,
            initial,
            increments
        )
}

data class IntervalDataFileDTO(
    val initial: Long = 0,
    val increasing: Boolean = false,
    val soundEffect: IntervalEndSoundType? = null
) {
    fun toIntervalData() =
        IntervalData(
            initial,
            initial,
            increasing,
            soundEffect?.toIntervalEndSound() ?: IntervalEndSound.None
        )
}