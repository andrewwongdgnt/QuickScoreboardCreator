package com.dgnt.quickScoreboardCreator.core.presentation.ui

import com.ibm.icu.text.RuleBasedNumberFormat
import java.util.Locale


object DateDisplayUtil {

    fun getDayOfMonthSuffix(n: Int): String {
        val formatter = RuleBasedNumberFormat(Locale.getDefault(), RuleBasedNumberFormat.ORDINAL)
        return formatter.format(n)
    }

}