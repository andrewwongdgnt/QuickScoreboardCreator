package com.dgnt.quickScoreboardCreator.ui.common.composable.carditem

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgnt.quickScoreboardCreator.domain.team.model.CategorizedTeamItemData

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategorizedTeamListContent(
    modifier: Modifier = Modifier,
    onItemClick: (Int) -> Unit,
    onItemDelete: ((Int) -> Unit)? = null,
    categorizedTeamList: List<CategorizedTeamItemData>
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {

        categorizedTeamList
            .forEach { category ->
                val title = category.title
                stickyHeader {
                    Text(
                        text = title,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(10.dp)
                    )
                }
                val itemList = category.teamItemDataList
                items(
                    items = itemList,
                    key = { it.id }
                ) { team ->
                    val cardItemContent = @Composable {
                        CardItemContent(
                            modifier = Modifier
                                .fillMaxWidth(),
                            title = team.title,
                            description = team.description,
                            iconRes = team.icon.res,
                            onClick = {
                                onItemClick(team.id)
                            }
                        )
                    }

                    onItemDelete?.let {
                        SwipeBox(
                            modifier = Modifier.animateItem(),
                            onDelete = {
                                onItemDelete.invoke(team.id)
                            },
                        content = cardItemContent)
                    } ?: run {
                        cardItemContent()
                    }

                }
            }
    }
}