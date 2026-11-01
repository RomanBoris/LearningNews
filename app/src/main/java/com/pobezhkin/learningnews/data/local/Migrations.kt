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

/**
 * Версия 2 → 3: кэш теперь хранит ленту отдельно для каждой темы (ключ — тема + адрес).
 * Кэш — это копия данных с сервера, поэтому таблицу проще пересоздать, чем переносить:
 * лента подгрузится заново. «Смотреть позже» не трогаем.
 */
val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DROP TABLE IF EXISTS `cached_news`")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `cached_news` (" +
                "`category` TEXT NOT NULL, " +
                "`url` TEXT NOT NULL, " +
                "`author` TEXT, " +
                "`title` TEXT NOT NULL, " +
                "`description` TEXT, " +
                "`urlToImage` TEXT, " +
                "`publishedAt` TEXT NOT NULL, " +
                "`sourceName` TEXT NOT NULL, " +
                "`position` INTEGER NOT NULL, " +
                "PRIMARY KEY(`category`, `url`))"
        )
    }
}
