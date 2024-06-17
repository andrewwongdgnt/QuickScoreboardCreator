package com.dgnt.quickScoreboardCreator.domain.scoreboard.model

import androidx.annotation.DrawableRes
import com.dgnt.quickScoreboardCreator.R

enum class ScoreboardIcon(@DrawableRes val res: Int) {
    BASKETBALL(R.drawable.basketball),
    BOXING(R.drawable.boxing),
    HOCKEY(R.drawable.hockey),
    SOCCER(R.drawable.soccer),
    TENNIS(R.drawable.tennis),
    VOLLEYBALL(R.drawable.volleyball),

}