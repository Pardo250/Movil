package com.example.condorapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.condorapp.R
import com.example.condorapp.data.Review
import com.example.condorapp.data.local.ReviewRepository
import com.example.condorapp.ui.theme.CondorappTheme

data class ReviewScreenUiState(
    val review: Review? = null,
    val comments: List<Review> = emptyList(),
    val userComment: String = ""
)

@Composable
fun ReviewScreenRoute(
    modifier: Modifier = Modifier,
    reviewId: String = "1",
    onBackClick: () -> Unit
) {
    val reviews = remember { ReviewRepository.getReviews() }
    val mainReview = remember(reviewId) { reviews.firstOrNull() }
    val comments = remember(reviewId) { reviews.drop(1) }

    var state by remember {
        mutableStateOf(ReviewScreenUiState(review = mainReview, comments = comments))
    }

    ReviewScreenContent(
        state = state,
        modifier = modifier,
        onBack = onBackClick,
        onUserCommentChange = { state = state.copy(userComment = it) },
        onPostComment = {
            if (state.userComment.isNotBlank()) {
                state = state.copy(userComment = "")
            }
        }
    )
}

@Composable
fun ReviewScreenContent(
    state: ReviewScreenUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    onUserCommentChange: (String) -> Unit,
    onPostComment: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    if (state.review == null) {
        Box(modifier.fillMaxSize().background(colorScheme.background), contentAlignment = Alignment.Center) {
            Text("Reseña no encontrada", color = colorScheme.onBackground)
        }
        return
    }

    Scaffold(
        modifier = modifier,
        topBar = { ReviewTopBar(onBack = onBack) },
        bottomBar = {
            CommentInputField(
                comment = state.userComment,
                onCommentChange = onUserCommentChange,
                onPostComment = onPostComment
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(colorScheme.background),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                MainReviewCard(review = state.review)
            }
            item {
                Text(
                    text = "Comentarios (${state.comments.size})",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = colorScheme.primary
                )
            }
            items(state.comments) {
                CommentItem(comment = it)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewTopBar(onBack: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    TopAppBar(
        title = { Text("Review de Viaje", fontWeight = FontWeight.Bold) },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
            }
        },
        actions = {
            IconButton(onClick = { }) { Icon(Icons.Default.Share, contentDescription = null) }
            IconButton(onClick = { }) { Icon(Icons.Default.MoreVert, contentDescription = null) }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorScheme.background,
            titleContentColor = colorScheme.onBackground
        )
    )
}

@Composable
fun MainReviewCard(review: Review) {
    val colorScheme = MaterialTheme.colorScheme
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier.size(45.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(review.name, fontWeight = FontWeight.Bold, color = colorScheme.onSurface)
                Text("Valle del Cocora, Quindío • hace 2 días", style = MaterialTheme.typography.bodySmall, color = colorScheme.outline)
            }
        }
        Spacer(Modifier.height(12.dp))
        Row {
            repeat(5) { index ->
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (index < review.rating) Color(0xFFFFB400) else colorScheme.outlineVariant,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
        Spacer(Modifier.height(12.dp))
        Text(
            text = "¡Una experiencia mágica entre las palmas!",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = colorScheme.onBackground
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = review.comment,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(16.dp))
        Image(
            painter = painterResource(id = R.drawable.valle_del_cocora),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(200.dp).clip(RoundedCornerShape(16.dp))
        )
        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = colorScheme.primary, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text(review.likes.toString(), color = colorScheme.onSurface)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.ChatBubble, contentDescription = null, tint = colorScheme.outline, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(4.dp))
                Text("24", color = colorScheme.onSurface)
            }
        }
    }
}

@Composable
fun CommentItem(comment: Review) {
    val colorScheme = MaterialTheme.colorScheme
    Row(modifier = Modifier.padding(vertical = 12.dp)) {
        Image(
            painter = painterResource(id = R.drawable.avatar),
            contentDescription = null,
            modifier = Modifier.size(36.dp).clip(CircleShape)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(comment.name, fontWeight = FontWeight.SemiBold, color = colorScheme.onSurface, fontSize = 14.sp)
            Text(comment.comment, style = MaterialTheme.typography.bodyMedium, color = colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
fun CommentInputField(comment: String, onCommentChange: (String) -> Unit, onPostComment: () -> Unit) {
    val colorScheme = MaterialTheme.colorScheme
    Surface(
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        color = colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.avatar),
                contentDescription = null,
                modifier = Modifier.size(32.dp).clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            TextField(
                value = comment,
                onValueChange = onCommentChange,
                placeholder = { Text("Añadir un comentario...") },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            IconButton(onClick = onPostComment) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, tint = colorScheme.primary)
            }
        }
    }
}

@Preview(showBackground = true, name = "Review Detail - Light")
@Composable
fun ReviewScreenLightPreview() {
    CondorappTheme(darkTheme = false) {
        ReviewScreenRoute(onBackClick = {})
    }
}

@Preview(showBackground = true, name = "Review Detail - Dark")
@Composable
fun ReviewScreenDarkPreview() {
    CondorappTheme(darkTheme = true) {
        ReviewScreenRoute(onBackClick = {})
    }
}
