package com.oguzdogdu.walliescompose.util

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CornerBasedShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.R


@Composable
fun <T> LazyItemScope.ReusableMenuRow(
    data: List<T>,
    index: Int,
    modifier: Modifier,
    itemContent: @Composable BoxScope.(T) -> Unit,
    onClick: (Int) -> Unit
) {
    val size = data.count()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        for (columnIndex in 0 until size) {
            val itemIndex = index + columnIndex
            if (itemIndex < size) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .clickable { onClick.invoke(itemIndex) }
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = mediumBackground(index = itemIndex, size = data.size)
                        )
                ) {
                    itemContent(data[itemIndex])
                }
                if (index < size - 1 && columnIndex < size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(), thickness = 1.dp, color = Color.Gray
                    )
                }
            }
        }
    }
}

fun mediumBackground(index: Int, size: Int): CornerBasedShape {
    return when (size) {
        1 -> Shapes().medium.copy(
            topStart = CornerSize(16.dp),
            topEnd = CornerSize(16.dp),
            bottomStart = CornerSize(16.dp),
            bottomEnd = CornerSize(16.dp)
        )

        else -> {
            when (index) {
                0 -> Shapes().medium.copy(
                    topStart = CornerSize(16.dp),
                    topEnd = CornerSize(16.dp),
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )

                size - 1 -> Shapes().medium.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(16.dp),
                    bottomEnd = CornerSize(16.dp)
                )

                else -> Shapes().medium.copy(
                    topStart = CornerSize(0.dp),
                    topEnd = CornerSize(0.dp),
                    bottomStart = CornerSize(0.dp),
                    bottomEnd = CornerSize(0.dp)
                )
            }
        }
    }
}
