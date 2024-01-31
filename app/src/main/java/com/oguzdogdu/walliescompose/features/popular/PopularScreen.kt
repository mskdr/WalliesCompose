package com.oguzdogdu.walliescompose.features.popular

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import androidx.paging.compose.itemKey
import coil.compose.SubcomposeAsyncImage
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.data.common.ImageLoadingState
import com.oguzdogdu.walliescompose.domain.model.popular.PopularImage
import com.oguzdogdu.walliescompose.ui.theme.medium

@Composable
fun PopularScreenRoute(
    modifier: Modifier = Modifier,
    viewModel: PopularViewModel = hiltViewModel(),
    onPopularClick: (String?) -> Unit,
    onBackClick: () -> Unit
) {
    val popularListState: LazyPagingItems<PopularImage> =
        viewModel.getPopular.collectAsLazyPagingItems()
    val lifecycleOwner = LocalLifecycleOwner.current

    LifecycleEventEffect(event = Lifecycle.Event.ON_CREATE, lifecycleOwner = lifecycleOwner) {
        viewModel.handleUIEvent(PopularScreenEvent.FetchPopularData)

    }
    Scaffold(modifier = modifier
        .fillMaxSize()
        .background(Color.Magenta), topBar = {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { onBackClick.invoke() },
                modifier = modifier
                    .wrapContentSize()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.back),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = modifier
                        .wrapContentSize()
                )
            }

            Text(
                modifier = modifier,
                text = stringResource(id = R.string.popular_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontSize = 16.sp,
                fontFamily = medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Start
            )
        }
    }) {
        Column(
            modifier = modifier
                .padding(it)
                .fillMaxSize()
        ) {
            PopularDetailListScreen(modifier = modifier, popularLazyPagingItems = popularListState, onTopicClick = { id ->
                onPopularClick.invoke(id)
            })
        }
    }
}

@Composable
private fun PopularDetailListScreen(
    modifier: Modifier,
    popularLazyPagingItems: LazyPagingItems<PopularImage>,
    onTopicClick: (String) -> Unit
) {
    Column(modifier = modifier
        .fillMaxSize()
        .padding(bottom = 8.dp, start = 8.dp, end = 8.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxSize(),
            state = rememberLazyGridState(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(
                count = popularLazyPagingItems.itemCount,
                key = popularLazyPagingItems.itemKey { item: PopularImage -> item.id.hashCode() },
                contentType = popularLazyPagingItems.itemContentType { "Popular" }) { index: Int ->
                val popular: PopularImage? = popularLazyPagingItems[index]
                if (popular != null) {
                    PopularListItem(popularImage = popular, onPopularClick = { onTopicClick.invoke(it) })
                }
            }
        }
    }
}
@Composable
fun PopularListItem(popularImage: PopularImage, onPopularClick: (String) -> Unit) {
    Box(
        modifier = Modifier
            .wrapContentSize()
            .clickable {
                popularImage.id?.let {
                    onPopularClick.invoke(
                        it
                    )
                }
            }
        , contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = popularImage.url,
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(CircleShape.copy(all = CornerSize(16.dp)))
            , loading = {
                ImageLoadingState()
            }
        )
    }
}