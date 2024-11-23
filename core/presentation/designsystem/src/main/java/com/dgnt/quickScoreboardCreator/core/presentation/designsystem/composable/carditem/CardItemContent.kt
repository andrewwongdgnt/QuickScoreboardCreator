package com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.carditem

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R

@Composable
fun CardItemContent(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    @DrawableRes iconRes: Int,
    onClick: () -> Unit,
    supportingText: String? = null,
    iconContent: @Composable () -> Unit = { },
) {
    ListItem(
        modifier = modifier
            .clip(MaterialTheme.shapes.small)
            .clickable(onClick = onClick),
        headlineContent = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                iconContent()
            }
        },
        supportingContent = {
            Text(
                text = description,
                style = MaterialTheme.typography.labelSmall,
                fontStyle = FontStyle.Italic,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingContent = {
            Column {
                Image(
                    painterResource(iconRes),
                    null,
                    modifier = Modifier
                        .width(48.dp)
                )
                supportingText?.let {
                    Text(
                        text = supportingText,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier
                            .padding(top = 4.dp),
                        fontStyle = FontStyle.Italic,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun `Normal`() =
    CardItemContent(
        title = "Dragons",
        description = "Legendary creatures",
        iconRes = R.drawable.alien,
        onClick = {},
        supportingText = "6th, 2:30 PM"
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }

@Preview(showBackground = true)
@Composable
private fun `Long description`() =
    CardItemContent(
        title = "Dragons",
        description = "Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures",
        iconRes = R.drawable.dragon,
        onClick = {},
        supportingText = "6th, 2:30 PM"
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }

@Preview(showBackground = true)
@Composable
private fun `Long description and long description`() =
    CardItemContent(
        title = "Dragons Dragons Dragons Dragons Dragons Dragons Dragons Dragons",
        description = "Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures",
        iconRes = R.drawable.dragon,
        onClick = {}
    ) {
        IconButton(onClick = { }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = ""
            )
        }
    }

@Preview(showBackground = true)
@Composable
private fun `Cannot Launch`() =
    CardItemContent(
        title = "Dragons Dragons Dragons Dragons Dragons Dragons Dragons Dragons",
        description = "Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures",
        iconRes = R.drawable.dragon,
        onClick = {}
    )
