package com.pobezhkin.learningnews.presentation.home.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pobezhkin.learningnews.domain.model.News

@Composable
fun NewsCard(news: News) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),

        content = {
            Column {

                Text(
                    text = news.title,
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.headlineLarge
                )
                if (news.author !== null) {
                    Text(news.author, modifier = Modifier.padding(16.dp))
                }

                if (news.description !== null) {
                    Text(news.description, modifier = Modifier.padding(16.dp))
                }

                Text(
                    news.publishedAt,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style =MaterialTheme.typography.labelSmall
                )
            }

        }
    )
}