package com.dgnt.quickScoreboardCreator.data.state

interface State {
    val next: State?
}