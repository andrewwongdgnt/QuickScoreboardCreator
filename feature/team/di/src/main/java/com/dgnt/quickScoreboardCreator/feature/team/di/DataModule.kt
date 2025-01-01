package com.dgnt.quickScoreboardCreator.feature.team.di

import com.dgnt.quickScoreboardCreator.core.data.base.dao.BaseDao
import com.dgnt.quickScoreboardCreator.core.data.base.loader.BaseFileDao
import com.dgnt.quickScoreboardCreator.core.data.base.mapper.Mapper
import com.dgnt.quickScoreboardCreator.core.data.serializer.Serializer
import com.dgnt.quickScoreboardCreator.feature.team.data.entity.TeamEntity
import com.dgnt.quickScoreboardCreator.feature.team.data.filedao.TeamFileDao
import com.dgnt.quickScoreboardCreator.feature.team.data.filedto.TeamFileDTO
import com.dgnt.quickScoreboardCreator.feature.team.data.repository.QSCTeamRepository
import com.dgnt.quickScoreboardCreator.feature.team.domain.model.TeamModel
import com.dgnt.quickScoreboardCreator.feature.team.domain.repository.TeamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Provides
    @Singleton
    fun provideTeamFileDao(serializer: Serializer): BaseFileDao<TeamFileDTO> =
        TeamFileDao(serializer)

    @Provides
    @Singleton
    fun provideTeamRepository(
        teamDao: BaseDao<TeamEntity>,
        teamFileDao: BaseFileDao<TeamFileDTO>,
        mapTeamDataToDomain: Mapper<TeamEntity, TeamModel>,
        mapTeamDomainToData: Mapper<TeamModel, TeamEntity>,
        mapTeamFileDTOToDomain: Mapper<TeamFileDTO, TeamModel>,
    ): TeamRepository =
        QSCTeamRepository(
            teamDao,
            teamFileDao,
            mapTeamDataToDomain,
            mapTeamDomainToData,
            mapTeamFileDTOToDomain
        )
}