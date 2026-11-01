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

/**
 * Версия 3 → 4: добавили таблицу ключей страниц для постраничной загрузки.
 * Старый кэш не знает, на какой странице остановился, поэтому очищаем его:
 * лента загрузится заново с первой страницы. «Смотреть позже» не трогаем.
 */
val MIGRATION_3_4 = object : Migration(3, 4) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("DELETE FROM `cached_news`")
        db.execSQL(
            "CREATE TABLE IF NOT EXISTS `remote_keys` (" +
                "`category` TEXT NOT NULL, " +
                "`nextPage` INTEGER, " +
                "PRIMARY KEY(`category`))"
        )
    }
}

/**
 * Версия 4 → 5: у сохранённых новостей появился текст статьи и статус его загрузки.
 * Уже сохранённые новости получают статус PENDING: при следующем запуске приложение
 * поставит их тексты в очередь и загрузит, как только будет сеть.
 */
val MIGRATION_4_5 = object : Migration(4, 5) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE `watch_later_news` ADD COLUMN `content` TEXT")
        db.execSQL(
            "ALTER TABLE `watch_later_news` ADD COLUMN `contentStatus` TEXT NOT NULL DEFAULT 'PENDING'"
        )
    }
}
