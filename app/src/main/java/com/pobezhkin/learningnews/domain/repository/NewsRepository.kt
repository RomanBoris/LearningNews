package com.pobezhkin.learningnews.domain.repository

import androidx.paging.PagingData
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.NewsCategory
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    /**
     * Новости выбранной темы постранично. Читаются из локальной базы, а недостающие страницы
     * подгружаются с сервера. Если сети нет, показывается то, что уже загружено.
     * Ошибки приходят как [com.pobezhkin.learningnews.domain.util.NewsLoadException]
     * в состоянии загрузки списка.
     */
    fun getNewsPaged(category: NewsCategory): Flow<PagingData<News>>
}
