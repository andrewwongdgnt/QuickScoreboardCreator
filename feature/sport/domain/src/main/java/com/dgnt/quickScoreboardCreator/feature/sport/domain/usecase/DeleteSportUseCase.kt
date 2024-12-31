package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import javax.inject.Inject

class DeleteSportUseCase @Inject constructor(
    private val repository: SportRepository
) {

    suspend operator fun invoke(sportModel: SportModel) =
        repository.delete(sportModel)

}