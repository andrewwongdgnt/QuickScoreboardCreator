package com.dgnt.quickScoreboardCreator.core.data.db

import androidx.room.TypeConverter
import com.dgnt.quickScoreboardCreator.core.data.sport.filedto.WinRuleType
import com.dgnt.quickScoreboardCreator.core.domain.sport.model.score.WinRule

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