package com.dgnt.quickScoreboardCreator.feature.team.domain.usecase

import javax.inject.Inject

class ValidateTeamDetailsUseCase @Inject constructor() {
    operator fun invoke(title: String) = title.isNotBlank()
}