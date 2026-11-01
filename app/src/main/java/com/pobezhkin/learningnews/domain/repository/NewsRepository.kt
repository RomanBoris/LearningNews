package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.NewsFeed
import com.pobezhkin.learningnews.domain.util.Result

interface NewsRepository {

    /**
     * Свежая лента с сервера. Если сети нет или сервер ответил ошибкой,
     * отдаёт последнюю сохранённую ленту. Ошибка — только когда кэш пуст.
     */
    suspend fun getTopHeadlines(): Result<NewsFeed>
}
