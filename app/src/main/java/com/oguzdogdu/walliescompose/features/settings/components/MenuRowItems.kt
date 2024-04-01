package com.oguzdogdu.walliescompose.features.settings.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.oguzdogdu.walliescompose.R
import com.oguzdogdu.walliescompose.util.MenuRow

@Composable
fun MenuRowItems(modifier: Modifier, menuRow: MenuRow,arrow:Boolean) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(modifier = modifier.fillMaxWidth()) {
            Row(
                modifier = modifier.wrapContentSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                menuRow.icon?.let {
                    Image(painter = painterResource(id = it), contentDescription = "")
                }
                Spacer(modifier = modifier.size(8.dp))
                menuRow.titleRes?.let {
                    Text(
                        modifier = modifier.padding(start = 8.dp),
                        text = stringResource(id = it),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

            }
            if (arrow) {
                Icon(
                    painter = painterResource(id = R.drawable.arrow_small),
                    contentDescription = "",
                    modifier = modifier.align(Alignment.CenterEnd)
                )
            }
        }
    }
}
