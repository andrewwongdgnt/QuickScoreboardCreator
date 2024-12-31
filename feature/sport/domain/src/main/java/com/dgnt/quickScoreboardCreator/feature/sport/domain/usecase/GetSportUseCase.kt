package com.dgnt.quickScoreboardCreator.feature.sport.domain.usecase

import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import java.io.InputStream
import javax.inject.Inject

class GetSportUseCase @Inject constructor(
    private val repository: SportRepository
) {

    suspend operator fun invoke(id: Int) =
        repository.getById(id)

    operator fun invoke(inputStream: InputStream) =
        repository.import(inputStream)

}