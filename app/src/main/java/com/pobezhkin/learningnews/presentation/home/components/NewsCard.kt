package com.pobezhkin.learningnews.presentation.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.presentation.util.toDisplayDate
import com.pobezhkin.learningnews.ui.theme.LearningNewsTheme

@Composable
fun NewsCard(
    news: News,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {}
) {
    Card(
        onClick = { onClick(news.url) },
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column {
            AsyncImage(
                model = news.urlToImage,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop,
                error = painterResource(android.R.drawable.ic_menu_gallery)
            )

            Text(
                text = news.title,
                modifier = Modifier.padding(11.dp),
                style = MaterialTheme.typography.headlineLarge
            )

            news.author?.let { author ->
                Text(text = author, modifier = Modifier.padding(11.dp))
            }

            news.description?.let { description ->
                Text(text = description, modifier = Modifier.padding(11.dp))
            }

            Text(
                text = news.publishedAt.toDisplayDate(),
                modifier = Modifier.padding(11.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsCardPreview() {
    LearningNewsTheme {
        NewsCard(
            news = News(
                id = "1",
                title = "NASA discovers water on Mars surface",
                author = "John Smith",
                description = "Scientists have found evidence of liquid water beneath the Martian surface, raising new questions about the possibility of life.",
                url = "https://example.com/news/1",
                urlToImage = null,
                publishedAt = "2026-05-01T12:13:00Z",
                sourceName = "BBC Science"
            )
        )
    }
}
