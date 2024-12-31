package com.dgnt.quickScoreboardCreator.feature.sport.presentation.uievent

import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIdentifier

data class LaunchScoreboard(val sportIdentifier: SportIdentifier) : UiEvent
data class SportDetails(val sportIdentifier: SportIdentifier? = null) : UiEvent



