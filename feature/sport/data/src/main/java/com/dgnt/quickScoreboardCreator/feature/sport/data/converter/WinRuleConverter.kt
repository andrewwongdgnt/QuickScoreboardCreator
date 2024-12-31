package com.dgnt.quickScoreboardCreator.feature.sport.data.converter

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.WinRuleType
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule


class WinRuleConverter {
    @TypeConverter
    fun toWinRule(value: String) = WinRuleType.valueOf(value).toWinRule()

    @TypeConverter
    fun toWinRuleType(value: WinRule) =
        when (value) {
            is WinRule.Sum -> WinRuleType.SUM.name
            is WinRule.Count -> WinRuleType.COUNT.name
            is WinRule.Final -> WinRuleType.FINAL.name
        }


}