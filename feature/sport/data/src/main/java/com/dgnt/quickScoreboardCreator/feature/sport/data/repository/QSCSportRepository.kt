package com.dgnt.quickScoreboardCreator.feature.sport.data.repository

import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.base.repository.BaseRepository
import com.dgnt.quickScoreboardCreator.feature.sport.data.dao.SportDao
import com.dgnt.quickScoreboardCreator.feature.sport.data.entity.SportEntity
import com.dgnt.quickScoreboardCreator.feature.sport.data.filedto.SportFileDTO
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportModel
import com.dgnt.quickScoreboardCreator.feature.sport.domain.repository.SportRepository
import javax.inject.Inject

class QSCSportRepository @Inject constructor(
    dao: SportDao,
    loader: BaseFileDao<SportFileDTO>,
    mapEntityToDomain: Mapper<SportEntity, SportModel>,
    mapDomainToEntity: Mapper<SportModel, SportEntity>,
    mapFileDTOToDomain: Mapper<SportFileDTO, SportModel>
) : BaseRepository<SportEntity, SportFileDTO, SportModel>(dao, loader, mapEntityToDomain, mapDomainToEntity, mapFileDTOToDomain), SportRepository