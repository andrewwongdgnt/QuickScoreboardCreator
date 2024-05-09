import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamIcon
import com.dgnt.quickScoreboardCreator.domain.team.model.TeamItemData

@Composable
fun TeamItemContent(
    modifier: Modifier = Modifier,
    item: TeamItemData,
    onItemClick: (TeamItemData) -> Unit,
    onItemDelete: ((Int) -> Unit)? = null,
) {
    Row(
        modifier = modifier.clickable {
            onItemClick(item)
        },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(start = 3.dp, top = 10.dp, bottom = 10.dp)
            ) {
                Image(
                    painterResource(item.teamIcon.res),
                    null,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .width(48.dp)
                )
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item.title, style = MaterialTheme.typography.titleLarge, maxLines = 1, overflow = TextOverflow.Ellipsis, modifier = Modifier.weight(1f))

                        if (onItemDelete != null) {
                            IconButton(onClick = {
                                onItemDelete(item.id)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = stringResource(R.string.delete)
                                )
                            }
                        }
                    }
                    Text(text = item.description, style = MaterialTheme.typography.labelSmall, fontStyle = FontStyle.Italic, maxLines = 2, overflow = TextOverflow.Ellipsis)
                }
            }
            HorizontalDivider()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun `Normal`() =
    TeamItemContent(
        item = TeamItemData(
            0, title = "Dragons", description = "Legendary creatures",
            teamIcon = TeamIcon.ALIEN
        ),
        onItemClick = {},
        onItemDelete = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Long description`() =
    TeamItemContent(
        item =  TeamItemData(
            0,
            title = "Dragons",
            description = "Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures",
            teamIcon = TeamIcon.DRAGON
        ),
        onItemClick = {},
        onItemDelete = {}
    )

@Preview(showBackground = true)
@Composable
private fun `Long description and long description`() =
    TeamItemContent(
        item = TeamItemData(
            0,
            title = "Dragons Dragons Dragons Dragons Dragons Dragons Dragons Dragons",
            description = "Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures",
            teamIcon = TeamIcon.DRAGON
        ),
        onItemClick = {},
        onItemDelete = {}
    )

@Preview(showBackground = true)
@Composable
private fun `Cannot delete`() =
    TeamItemContent(
       item = TeamItemData(
            0,
            title = "Dragons Dragons Dragons Dragons Dragons Dragons Dragons Dragons",
            description = "Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures Legendary creatures",
            teamIcon = TeamIcon.DRAGON
        ),
        onItemClick = {}
    )
