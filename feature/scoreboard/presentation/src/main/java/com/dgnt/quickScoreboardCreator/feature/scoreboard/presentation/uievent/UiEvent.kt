package com.dgnt.quickScoreboardCreator.feature.scoreboard.presentation.uievent

import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier

data class IntervalEditor(val currentTimeValue: Long, val index: Int, val sportIdentifier: SportIdentifier) : UiEvent




