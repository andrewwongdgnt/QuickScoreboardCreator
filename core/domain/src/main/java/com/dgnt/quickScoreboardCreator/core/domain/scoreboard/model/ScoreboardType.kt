package com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model

enum class ScoreboardType(
    val icon: ScoreboardIcon
) {
    BASKETBALL(ScoreboardIcon.BASKETBALL),
    HOCKEY(ScoreboardIcon.HOCKEY),
    SPIKEBALL(ScoreboardIcon.SPIKEBALL),
    TENNIS(ScoreboardIcon.TENNIS),
    BOXING(ScoreboardIcon.BOXING),
}