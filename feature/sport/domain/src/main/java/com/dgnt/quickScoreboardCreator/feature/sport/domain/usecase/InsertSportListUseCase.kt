package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase


import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import javax.inject.Inject

class InsertSportListUseCase @Inject constructor(
    private val repository: SportRepository
) {

    suspend operator fun invoke(sportList: List<SportModel>) =
        repository.insert(sportList)

}