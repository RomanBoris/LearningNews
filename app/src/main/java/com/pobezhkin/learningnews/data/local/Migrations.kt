package com.pobezhkin.learningnews.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Версия 1 → 2: добавили таблицу кэша ленты.
 * Сохранённые в «Смотреть позже» новости не трогаем — у пользователей они не пропадут.
 * Текст запроса должен в точности совпадать со схемой CachedNewsEntity, иначе Room не откроет базу.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `cached_news` (" +
                "`url` TEXT NOT NULL, " +
                "`author` TEXT, " +
                "`title` TEXT NOT NULL, " +
                "`description` TEXT, " +
                "`urlToImage` TEXT, " +
                "`publishedAt` TEXT NOT NULL, " +
                "`sourceName` TEXT NOT NULL, " +
                "`position` INTEGER NOT NULL, " +
                "PRIMARY KEY(`url`))"
        )
    }
}
