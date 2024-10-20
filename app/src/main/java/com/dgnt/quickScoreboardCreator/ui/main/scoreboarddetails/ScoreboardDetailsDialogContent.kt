@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.R
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.ui.common.composable.BackButton
import com.dgnt.quickScoreboardCreator.ui.common.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.ui.common.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.ui.common.composable.LabelSwitch
import com.dgnt.quickScoreboardCreator.ui.common.composable.MultipleOptionsPicker
import com.dgnt.quickScoreboardCreator.ui.common.composable.OptionData
import com.dgnt.quickScoreboardCreator.ui.common.composable.ScoreboardIconPicker
import com.dgnt.quickScoreboardCreator.ui.common.composable.TimeLimitPicker
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.TriangleDown
import com.dgnt.quickScoreboardCreator.ui.common.imagevector.TriangleUp
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun ScoreboardDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: ScoreboardDetailsViewModel = hiltViewModel()
) {
    val valid by viewModel.valid.collectAsStateWithLifecycle()
    val title by viewModel.title.collectAsStateWithLifecycle()
    val description by viewModel.description.collectAsStateWithLifecycle()
    val winRule by viewModel.winRule.collectAsStateWithLifecycle()
    val icon by viewModel.icon.collectAsStateWithLifecycle()
    val iconChanging by viewModel.iconChanging.collectAsStateWithLifecycle()
    val isNewEntity by viewModel.isNewEntity.collectAsStateWithLifecycle()
    val intervalList by viewModel.intervalList.collectAsStateWithLifecycle()

    ScoreboardDetailsInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        title = title,
        onTitleChange = viewModel::onTitleChange,
        description = description,
        onDescriptionChange = viewModel::onDescriptionChange,
        winRule = winRule,
        onWinRuleChange = viewModel::onWinRuleChange,
        icon = icon,
        onIconChange = viewModel::onIconChange,
        iconChanging = iconChanging,
        onIconEdit = viewModel::onIconEdit,
        intervalList = intervalList,
        onIntervalEditForTimeIsIncreasing = viewModel::onIntervalEditForTimeIsIncreasing,
        onIntervalEditForMinute = viewModel::onIntervalEditForMinute,
        onIntervalEditForSecond = viewModel::onIntervalEditForSecond,
        onIntervalEditForAllowDeuceAdv = viewModel::onIntervalEditForAllowDeuceAdv,
        onIntervalEditForMaxScoreInput = viewModel::onIntervalEditForMaxScoreInput,
        onIntervalAdd = viewModel::onIntervalAdd,
        onIntervalRemove = viewModel::onIntervalRemove,
        onIntervalMove = viewModel::onIntervalMove,
        valid = valid,
        isNewEntity = isNewEntity,
        onDelete = viewModel::onDelete,
        onDismiss = viewModel::onDismiss,
        onConfirm = viewModel::onConfirm,
    )
}

@Composable
private fun ScoreboardDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    winRule: WinRule,
    onWinRuleChange: (WinRule) -> Unit,
    icon: ScoreboardIcon?,
    onIconChange: (ScoreboardIcon) -> Unit,
    iconChanging: Boolean,
    onIconEdit: (Boolean) -> Unit,
    onIntervalEditForTimeIsIncreasing: (Int, Boolean) -> Unit,
    onIntervalEditForMinute: (Int, String) -> Unit,
    onIntervalEditForSecond: (Int, String) -> Unit,
    onIntervalEditForAllowDeuceAdv: (Int, Boolean) -> Unit,
    onIntervalEditForMaxScoreInput: (Int, String) -> Unit,
    onIntervalAdd: (Int?) -> Unit,
    onIntervalRemove: (Int) -> Unit,
    onIntervalMove: (Boolean, Int) -> Unit,
    valid: Boolean,
    isNewEntity: Boolean,
    intervalList: List<IntervalEditingInfo>,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val context = LocalContext.current
    DefaultAlertDialog(
        title = stringResource(id = R.string.scoreboardDetailsTitle),
        actionIcon = Icons.Default.Delete.takeUnless { isNewEntity },
        actionContentDescription = stringResource(id = R.string.delete),
        actionOnClick = {
            Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show()
        },
        actionOnLongClick = onDelete,
        confirmText = stringResource(id = android.R.string.ok),
        confirmEnabled = valid,
        onConfirm = onConfirm,
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = onDismiss
    ) {
        if (iconChanging)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                BackButton { onIconEdit(false) }
                Spacer(modifier = Modifier.height(8.dp))
                ScoreboardIconPicker(
                    onIconChange = onIconChange
                )
            }
        else
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TitleAndDescription(
                    title = title,
                    onTitleChange = onTitleChange,
                    description = description,
                    onDescriptionChange = onDescriptionChange,
                )
                Spacer(modifier = Modifier.height(16.dp))
                IconDisplay(
                    iconRes = icon?.res,
                    onClick = { onIconEdit(true) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                WinRulePicker(
                    winRule = winRule,
                    onWinRuleChange = onWinRuleChange
                )
                Spacer(modifier = Modifier.height(16.dp))
                IntervalList(
                    modifier = Modifier.heightIn(min = 0.dp, max = LocalConfiguration.current.screenHeightDp.dp * intervalList.size),
                    intervalList = intervalList,
                    onIntervalEditForTimeIsIncreasing = onIntervalEditForTimeIsIncreasing,
                    onIntervalEditForMinute = onIntervalEditForMinute,
                    onIntervalEditForSecond = onIntervalEditForSecond,
                    onIntervalEditForAllowDeuceAdv = onIntervalEditForAllowDeuceAdv,
                    onIntervalEditForMaxScoreInput = onIntervalEditForMaxScoreInput,
                    onIntervalRemove = onIntervalRemove,
                    onIntervalMove = onIntervalMove
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    Button(onClick = { onIntervalAdd(null) }) {
                        Text(text = stringResource(R.string.addRound))
                    }
                }
            }
    }

}

@Composable
private fun TitleAndDescription(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
) {
    TextField(
        value = title,
        onValueChange = onTitleChange,
        placeholder = { Text(text = stringResource(R.string.namePlaceholder)) },
        modifier = Modifier.fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(8.dp))
    TextField(
        value = description,
        onValueChange = onDescriptionChange,
        placeholder = { Text(text = stringResource(R.string.descriptionPlaceholder)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = false,
        maxLines = 5
    )
}

@Composable
private fun WinRulePicker(
    winRule: WinRule,
    onWinRuleChange: (WinRule) -> Unit
) {
    MultipleOptionsPicker(
        header = stringResource(id = R.string.winRule),
        options = listOf(
            OptionData(
                label = stringResource(id = R.string.winRuleFinal),
                data = WinRule.Final
            ),
            OptionData(
                label = stringResource(id = R.string.winRuleCount),
                data = WinRule.Count
            ),
            OptionData(
                label = stringResource(id = R.string.winRuleSum),
                data = WinRule.Sum
            ),
        ),
        selectedOption = winRule,
        onOptionSelected = onWinRuleChange
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun IntervalList(
    modifier: Modifier = Modifier,
    intervalList: List<IntervalEditingInfo>,
    onIntervalEditForTimeIsIncreasing: (Int, Boolean) -> Unit,
    onIntervalEditForMinute: (Int, String) -> Unit,
    onIntervalEditForSecond: (Int, String) -> Unit,
    onIntervalEditForAllowDeuceAdv: (Int, Boolean) -> Unit,
    onIntervalEditForMaxScoreInput: (Int, String) -> Unit,
    onIntervalRemove: (Int) -> Unit,
    onIntervalMove: (Boolean, Int) -> Unit,
) {

    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(intervalList) { index, intervalEditingInfo ->
            val dividerColor = MaterialTheme.colorScheme.onBackground
            HorizontalDivider(
                color = dividerColor,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = stringResource(id = R.string.rulesForInterval, index + 1), style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                if (intervalList.size > 1) {
                    if (index > 0)
                        IconButton(onClick = { onIntervalMove(true, index) }) {
                            Icon(
                                imageVector = TriangleUp,
                                contentDescription = stringResource(R.string.up)
                            )
                        }
                    if (index < intervalList.lastIndex)
                        IconButton(onClick = { onIntervalMove(false, index) }) {
                            Icon(
                                imageVector = TriangleDown,
                                contentDescription = stringResource(R.string.up)
                            )
                        }
                    val context = LocalContext.current
                    Icon(modifier = Modifier.combinedClickable(
                        onClick = { Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show() },
                        onLongClick = { onIntervalRemove(index) }
                    ),
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.delete)
                    )

                }
            }

            val scoreInfo = intervalEditingInfo.scoreInfo
            val intervalData = intervalEditingInfo.intervalData

            LabelSwitch(
                label = stringResource(id = R.string.hasTimeLimit),
                checked = !intervalData.increasing,
                onCheckedChange = {
                    onIntervalEditForTimeIsIncreasing(index, !it)
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (!intervalData.increasing) {
                val numberFieldWidth = 65.dp
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.decreasingTimeMsg))
                    TimeLimitPicker(
                        minuteString = intervalEditingInfo.timeRepresentationPair.first,
                        onMinuteChange = { onIntervalEditForMinute(index, it) },
                        secondString = intervalEditingInfo.timeRepresentationPair.second,
                        onSecondChange = { onIntervalEditForSecond(index, it) },
                        numberFieldWidth = numberFieldWidth
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                LabelSwitch(
                    label = stringResource(id = R.string.allowDeuceAdvantage),
                    checked = scoreInfo.scoreRule is ScoreRule.Trigger.DeuceAdvantage,
                    onCheckedChange = {
                        onIntervalEditForAllowDeuceAdv(index, it)
                    },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = stringResource(id = R.string.maxScore)
                    )

                    TextField(
                        value = intervalEditingInfo.maxScoreInput,
                        onValueChange = {
                            onIntervalEditForMaxScoreInput(index, it)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.width(numberFieldWidth)
                    )
                }

            }


        }
    }
}

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        winRule = WinRule.Sum,
        onWinRuleChange = {},
        icon = null,
        onIconChange = {},
        iconChanging = true,
        onIconEdit = {},
        onIntervalEditForTimeIsIncreasing = { _, _ -> },
        onIntervalEditForMinute = { _, _ -> },
        onIntervalEditForSecond = { _, _ -> },
        onIntervalEditForAllowDeuceAdv = { _, _ -> },
        onIntervalEditForMaxScoreInput = { _, _ -> },
        onIntervalAdd = { _ -> },
        onIntervalRemove = { _ -> },
        onIntervalMove = { _, _ -> },
        valid = true,
        isNewEntity = true,
        intervalList = listOf(),
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Basketball`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        winRule = WinRule.Sum,
        onWinRuleChange = {},
        icon = ScoreboardIcon.BASKETBALL,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        onIntervalEditForTimeIsIncreasing = { _, _ -> },
        onIntervalEditForMinute = { _, _ -> },
        onIntervalEditForSecond = { _, _ -> },
        onIntervalEditForAllowDeuceAdv = { _, _ -> },
        onIntervalEditForMaxScoreInput = { _, _ -> },
        onIntervalAdd = { _ -> },
        onIntervalRemove = { _ -> },
        onIntervalMove = { _, _ -> },
        valid = true,
        isNewEntity = false,
        intervalList = listOf(),
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Hockey`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        winRule = WinRule.Sum,
        onWinRuleChange = {},
        icon = ScoreboardIcon.HOCKEY,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        onIntervalEditForTimeIsIncreasing = { _, _ -> },
        onIntervalEditForMinute = { _, _ -> },
        onIntervalEditForSecond = { _, _ -> },
        onIntervalEditForAllowDeuceAdv = { _, _ -> },
        onIntervalEditForMaxScoreInput = { _, _ -> },
        onIntervalAdd = { _ -> },
        onIntervalRemove = { _ -> },
        onIntervalMove = { _, _ -> },
        valid = true,
        isNewEntity = true,
        intervalList = listOf(),
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )

@Preview(showBackground = true)
@Composable
private fun `Loading Icon`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        winRule = WinRule.Count,
        onWinRuleChange = {},
        icon = null,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        onIntervalEditForTimeIsIncreasing = { _, _ -> },
        onIntervalEditForMinute = { _, _ -> },
        onIntervalEditForSecond = { _, _ -> },
        onIntervalEditForAllowDeuceAdv = { _, _ -> },
        onIntervalEditForMaxScoreInput = { _, _ -> },
        onIntervalAdd = { _ -> },
        onIntervalRemove = { _ -> },
        onIntervalMove = { _, _ -> },
        valid = true,
        isNewEntity = true,
        intervalList = listOf(),
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )


@Preview(showBackground = true)
@Composable
private fun `One default interval`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        winRule = WinRule.Sum,
        onWinRuleChange = {},
        icon = ScoreboardIcon.HOCKEY,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        onIntervalEditForTimeIsIncreasing = { _, _ -> },
        onIntervalEditForMinute = { _, _ -> },
        onIntervalEditForSecond = { _, _ -> },
        onIntervalEditForAllowDeuceAdv = { _, _ -> },
        onIntervalEditForMaxScoreInput = { _, _ -> },
        onIntervalAdd = { _ -> },
        onIntervalRemove = { _ -> },
        onIntervalMove = { _, _ -> },
        valid = true,
        isNewEntity = true,
        intervalList = listOf(
            IntervalEditingInfo(
                scoreInfo = ScoreInfo(
                    scoreRule = ScoreRule.None,
                    scoreToDisplayScoreMap = mapOf(),
                    dataList = listOf()
                ),
                intervalData = IntervalData(
                    current = 0,
                    initial = 0,
                    increasing = false
                ),
                timeRepresentationPair = Pair("9", "24"),
                maxScoreInput = "33"
            ),
        ),
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )


@Preview(showBackground = true)
@Composable
private fun `One interval`() =
    ScoreboardDetailsInnerDialogContent(
        uiEvent = emptyFlow(),
        onUiEvent = {},
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        winRule = WinRule.Sum,
        onWinRuleChange = {},
        icon = ScoreboardIcon.HOCKEY,
        onIconChange = {},
        iconChanging = false,
        onIconEdit = {},
        onIntervalEditForTimeIsIncreasing = { _, _ -> },
        onIntervalEditForMinute = { _, _ -> },
        onIntervalEditForSecond = { _, _ -> },
        onIntervalEditForAllowDeuceAdv = { _, _ -> },
        onIntervalEditForMaxScoreInput = { _, _ -> },
        onIntervalAdd = { _ -> },
        onIntervalRemove = { _ -> },
        onIntervalMove = { _, _ -> },
        valid = true,
        isNewEntity = true,
        intervalList = listOf(
            IntervalEditingInfo(
                scoreInfo = ScoreInfo(
                    scoreRule = ScoreRule.None,
                    scoreToDisplayScoreMap = mapOf(),
                    dataList = listOf()
                ),
                intervalData = IntervalData(
                    current = 0,
                    initial = 0,
                    increasing = true
                ),
                timeRepresentationPair = Pair("9", "24"),
                maxScoreInput = "33"
            ),
        ),
        onDelete = {},
        onDismiss = {},
        onConfirm = {},
    )