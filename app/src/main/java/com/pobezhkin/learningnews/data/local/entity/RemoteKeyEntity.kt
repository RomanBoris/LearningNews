package com.pobezhkin.learningnews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Какую страницу сервера грузить следующей для темы. Нужна, чтобы подгрузка помнила своё место. */
@Entity(tableName = "remote_keys")
data class RemoteKeyEntity(
    /** Имя значения [com.pobezhkin.learningnews.domain.model.NewsCategory]. */
    @PrimaryKey val category: String,
    /** null — сервер больше страниц не отдаёт, лента дошла до конца. */
    val nextPage: Int?
)
