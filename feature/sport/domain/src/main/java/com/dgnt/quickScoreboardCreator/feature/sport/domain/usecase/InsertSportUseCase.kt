package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase


import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import javax.inject.Inject

class InsertSportUseCase @Inject constructor(
    private val repository: SportRepository
) {

    suspend operator fun invoke(sport: SportModel) =
        repository.insert(sport)

}