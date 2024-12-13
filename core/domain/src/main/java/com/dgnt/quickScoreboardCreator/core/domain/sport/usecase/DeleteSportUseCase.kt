package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.model.SportModel
import com.dgnt.quickScoreboardCreator.core.domain.sport.repository.SportRepository
import javax.inject.Inject

class DeleteSportUseCase @Inject constructor(
    private val repository: SportRepository
) {

    suspend operator fun invoke(sportModel: SportModel) =
        repository.delete(sportModel)

}