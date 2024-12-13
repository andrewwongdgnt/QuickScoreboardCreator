package com.dgnt.quickScoreboardCreator.core.data.base.loader

import java.io.InputStream

interface BaseFileDao<T> {

    fun import(inputStream: InputStream): T?
}