package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.model.NewsFeed
import com.pobezhkin.learningnews.domain.util.Result

interface NewsRepository {

    /**
     * Свежая лента выбранной темы с сервера. Если сети нет или сервер ответил ошибкой,
     * отдаёт последнюю сохранённую ленту этой темы. Ошибка — только когда кэш темы пуст.
     */
    suspend fun getTopHeadlines(category: NewsCategory): Result<NewsFeed>
}
