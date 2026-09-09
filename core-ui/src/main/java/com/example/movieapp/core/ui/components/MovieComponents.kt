package com.example.movieapp.core.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.movieapp.core.ui.theme.MovieAppTheme

/** Minimal movie model consumed by shared UI components. */
data class MovieUiModel(
    val id: String,
    val title: String,
    val posterUrl: String,
    val progressPercent: Float? = null,
)

/** Displays a clickable 2:3 movie poster with an optional progress bar. */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MovieCard(
    title: String,
    posterUrl: String,
    progressPercent: Float? = null,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .width(128.dp)
            .combinedClickable(onClick = onClick, onLongClick = onLongClick),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(192.dp)
                .clip(RoundedCornerShape(10.dp)),
        ) {
            AsyncImage(
                model = posterUrl,
                contentDescription = title,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
            progressPercent?.let {
                LinearProgressIndicator(
                    progress = { it.coerceIn(0f, 1f) },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}

/** Centers a progress indicator in the available screen space. */
@Composable
fun LoadingIndicator(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

/** Shows an error message and a retry action. */
@Composable
fun ErrorView(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Icon(Icons.Default.ErrorOutline, contentDescription = null)
        Text(message, style = MaterialTheme.typography.bodyLarge)
        PrimaryButton(text = "Thử lại", onClick = onRetry, modifier = Modifier.fillMaxWidth())
    }
}

/** Displays the primary full-width rounded action button. */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
    ) {
        Text(text)
    }
}

/** Displays a titled horizontal row of movie cards. */
@Composable
fun MovieRow(
    title: String,
    movies: List<MovieUiModel>,
    onMovieClick: (MovieUiModel) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(horizontal = 16.dp),
        )
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            items(movies, key = { it.id }) { movie ->
                MovieCard(
                    title = movie.title,
                    posterUrl = movie.posterUrl,
                    progressPercent = movie.progressPercent,
                    onClick = { onMovieClick(movie) },
                    onLongClick = { onMovieClick(movie) },
                )
            }
        }
    }
}

private val previewMovies = listOf(
    MovieUiModel("1", "Interstellar", "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg", .42f),
    MovieUiModel("2", "Dune: Part Two", "https://image.tmdb.org/t/p/w500/1pdfLvkbY9ohJlCjQH2CZjjYVvJ.jpg"),
)

@Preview(showBackground = true)
@Composable
private fun MovieCardLightPreview() = MovieAppTheme(darkTheme = false) {
    MovieCard("Interstellar", previewMovies[0].posterUrl, .42f, {}, {})
}

@Preview(showBackground = true)
@Composable
private fun MovieCardDarkPreview() = MovieAppTheme(darkTheme = true) {
    MovieCard("Interstellar", previewMovies[0].posterUrl, .42f, {}, {})
}

@Preview(showBackground = true)
@Composable
private fun LoadingIndicatorLightPreview() = MovieAppTheme(darkTheme = false) { LoadingIndicator() }

@Preview(showBackground = true)
@Composable
private fun LoadingIndicatorDarkPreview() = MovieAppTheme(darkTheme = true) { LoadingIndicator() }

@Preview(showBackground = true)
@Composable
private fun ErrorViewLightPreview() = MovieAppTheme(darkTheme = false) { ErrorView("Không thể tải dữ liệu", {}) }

@Preview(showBackground = true)
@Composable
private fun ErrorViewDarkPreview() = MovieAppTheme(darkTheme = true) { ErrorView("Không thể tải dữ liệu", {}) }

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonLightPreview() = MovieAppTheme(darkTheme = false) { PrimaryButton("Đăng nhập", {}) }

@Preview(showBackground = true)
@Composable
private fun PrimaryButtonDarkPreview() = MovieAppTheme(darkTheme = true) { PrimaryButton("Đăng nhập", {}) }

@Preview(showBackground = true)
@Composable
private fun MovieRowLightPreview() = MovieAppTheme(darkTheme = false) {
    MovieRow("Xu hướng", previewMovies, {})
}

@Preview(showBackground = true)
@Composable
private fun MovieRowDarkPreview() = MovieAppTheme(darkTheme = true) {
    MovieRow("Xu hướng", previewMovies, {})
}
