package com.dgnt.quickScoreboardCreator.core.domain.sport.usecase

import com.dgnt.quickScoreboardCreator.core.domain.sport.repository.SportRepository
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