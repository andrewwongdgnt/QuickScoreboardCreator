@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails

import android.content.Context
import android.media.MediaPlayer
import android.widget.Toast
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.BackButton
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.LabelSwitch
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.MoveableElement
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.MultipleOptionsPicker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.OptionData
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.TimeLimitPicker
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.ScoreboardIcon
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalData
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreInfo
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.core.domain.scoreboard.model.score.WinRule
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.Speaker
import com.dgnt.quickScoreboardCreator.ui.common.ScoreboardIconPicker
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.iconRes
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.soundEffectRes
import com.dgnt.quickScoreboardCreator.ui.common.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.ui.common.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsViewModel.Companion.MAX_INCREMENTS_COUNT
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsViewModel.Companion.MAX_TEAMS
import com.dgnt.quickScoreboardCreator.ui.main.scoreboarddetails.ScoreboardDetailsViewModel.Companion.MIN_TEAMS
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
    val intervalLabel by viewModel.intervalLabel.collectAsStateWithLifecycle()
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
        intervalLabel = intervalLabel,
        onIntervalLabelChange = viewModel::onIntervalLabelChange,
        intervalList = intervalList,
        onIntervalEditForSoundEffect = viewModel::onIntervalEditForSoundEffect,
        onIntervalEditForTimeIsIncreasing = viewModel::onIntervalEditForTimeIsIncreasing,
        onIntervalEditForMinute = viewModel::onIntervalEditForMinute,
        onIntervalEditForSecond = viewModel::onIntervalEditForSecond,
        onIntervalEditForAllowDeuceAdv = viewModel::onIntervalEditForAllowDeuceAdv,
        onIntervalEditForMaxScoreInput = viewModel::onIntervalEditForMaxScoreInput,
        onIntervalEditForTeamCount = viewModel::onIntervalEditForTeamCount,
        onIntervalEditForPrimaryIncrementAdd = viewModel::onIntervalEditForPrimaryIncrementAdd,
        onIntervalEditForInitialScoreInput = viewModel::onIntervalEditForInitialScoreInput,
        onIntervalEditForPrimaryIncrement = viewModel::onIntervalEditForPrimaryIncrement,
        onIntervalEditForPrimaryIncrementMove = viewModel::onIntervalEditForPrimaryIncrementMove,
        onIntervalEditForPrimaryIncrementRemove = viewModel::onIntervalEditForPrimaryIncrementRemove,
        onIntervalEditForPrimaryIncrementRefresh = viewModel::onIntervalEditForPrimaryIncrementRefresh,
        onIntervalEditForPrimaryMappingAllowed = viewModel::onIntervalEditForPrimaryMappingAllowed,
        onIntervalEditForPrimaryMappingAdd = viewModel::onIntervalEditForPrimaryMappingAdd,
        onIntervalEditForPrimaryMappingOriginalScore = viewModel::onIntervalEditForPrimaryMappingOriginalScore,
        onIntervalEditForPrimaryMappingDisplayScore = viewModel::onIntervalEditForPrimaryMappingDisplayScore,
        onIntervalEditForPrimaryMappingMove = viewModel::onIntervalEditForPrimaryMappingMove,
        onIntervalEditForPrimaryMappingRemove = viewModel::onIntervalEditForPrimaryMappingRemove,
        onIntervalEditForSecondaryScoreAllowed = viewModel::onIntervalEditForSecondaryScoreAllowed,
        onIntervalEditForSecondaryScoreLabel = viewModel::onIntervalEditForSecondaryScoreLabel,
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
    intervalLabel: String,
    onIntervalLabelChange: (String) -> Unit,
    onIntervalEditForSoundEffect: (Int, IntervalEndSound) -> Unit,
    onIntervalEditForTimeIsIncreasing: (Int, Boolean) -> Unit,
    onIntervalEditForMinute: (Int, String) -> Unit,
    onIntervalEditForSecond: (Int, String) -> Unit,
    onIntervalEditForAllowDeuceAdv: (Int, Boolean) -> Unit,
    onIntervalEditForMaxScoreInput: (Int, String) -> Unit,
    onIntervalEditForTeamCount: (Int, Int) -> Unit,
    onIntervalEditForPrimaryIncrementAdd: (Int) -> Unit,
    onIntervalEditForInitialScoreInput: (Int, String) -> Unit,
    onIntervalEditForPrimaryIncrement: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryIncrementMove: (Int, Int, Boolean) -> Unit,
    onIntervalEditForPrimaryIncrementRemove: (Int, Int) -> Unit,
    onIntervalEditForPrimaryIncrementRefresh: (Int, Int) -> Unit,
    onIntervalEditForPrimaryMappingAllowed: (Int, Boolean) -> Unit,
    onIntervalEditForPrimaryMappingAdd: (Int) -> Unit,
    onIntervalEditForPrimaryMappingOriginalScore: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryMappingDisplayScore: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryMappingMove: (Int, Int, Boolean) -> Unit,
    onIntervalEditForPrimaryMappingRemove: (Int, Int) -> Unit,
    onIntervalEditForSecondaryScoreAllowed: (Int, Boolean) -> Unit,
    onIntervalEditForSecondaryScoreLabel: (Int, String) -> Unit,
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
                    iconRes = icon?.iconRes(),
                    onClick = { onIconEdit(true) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                WinRulePicker(
                    winRule = winRule,
                    onWinRuleChange = onWinRuleChange
                )
                Spacer(modifier = Modifier.height(16.dp))
                IntervalLabelEditor(
                    intervalLabel = intervalLabel,
                    onIntervalLabelChange = onIntervalLabelChange
                )
                Spacer(modifier = Modifier.height(16.dp))
                IntervalList(
                    modifier = Modifier.heightIn(min = 0.dp, max = LocalConfiguration.current.screenHeightDp.dp * 10 * intervalList.size),
                    intervalLabel = intervalLabel,
                    intervalList = intervalList,
                    onIntervalEditForSoundEffect = onIntervalEditForSoundEffect,
                    onIntervalEditForTimeIsIncreasing = onIntervalEditForTimeIsIncreasing,
                    onIntervalEditForMinute = onIntervalEditForMinute,
                    onIntervalEditForSecond = onIntervalEditForSecond,
                    onIntervalEditForAllowDeuceAdv = onIntervalEditForAllowDeuceAdv,
                    onIntervalEditForMaxScoreInput = onIntervalEditForMaxScoreInput,
                    onIntervalEditForTeamCount = onIntervalEditForTeamCount,
                    onIntervalEditForPrimaryIncrementAdd = onIntervalEditForPrimaryIncrementAdd,
                    onIntervalEditForInitialScoreInput = onIntervalEditForInitialScoreInput,
                    onIntervalEditForPrimaryIncrement = onIntervalEditForPrimaryIncrement,
                    onIntervalEditForPrimaryIncrementMove = onIntervalEditForPrimaryIncrementMove,
                    onIntervalEditForPrimaryIncrementRemove = onIntervalEditForPrimaryIncrementRemove,
                    onIntervalEditForPrimaryIncrementRefresh = onIntervalEditForPrimaryIncrementRefresh,
                    onIntervalEditForPrimaryMappingAllowed = onIntervalEditForPrimaryMappingAllowed,
                    onIntervalEditForPrimaryMappingAdd = onIntervalEditForPrimaryMappingAdd,
                    onIntervalEditForPrimaryMappingOriginalScore = onIntervalEditForPrimaryMappingOriginalScore,
                    onIntervalEditForPrimaryMappingDisplayScore = onIntervalEditForPrimaryMappingDisplayScore,
                    onIntervalEditForPrimaryMappingRemove = onIntervalEditForPrimaryMappingRemove,
                    onIntervalEditForPrimaryMappingMove = onIntervalEditForPrimaryMappingMove,
                    onIntervalEditForSecondaryScoreAllowed = onIntervalEditForSecondaryScoreAllowed,
                    onIntervalEditForSecondaryScoreLabel = onIntervalEditForSecondaryScoreLabel,
                    onIntervalAdd = onIntervalAdd,
                    onIntervalRemove = onIntervalRemove,
                    onIntervalMove = onIntervalMove
                )
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

@Composable
private fun IntervalLabelEditor(
    intervalLabel: String,
    onIntervalLabelChange: (String) -> Unit,
) {
    TextField(
        value = intervalLabel,
        onValueChange = onIntervalLabelChange,
        placeholder = { Text(text = stringResource(R.string.intervalLabelPlaceholder)) },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun IntervalList(
    modifier: Modifier = Modifier,
    intervalLabel: String,
    intervalList: List<IntervalEditingInfo>,
    onIntervalEditForSoundEffect: (Int, IntervalEndSound) -> Unit,
    onIntervalEditForTimeIsIncreasing: (Int, Boolean) -> Unit,
    onIntervalEditForMinute: (Int, String) -> Unit,
    onIntervalEditForSecond: (Int, String) -> Unit,
    onIntervalEditForAllowDeuceAdv: (Int, Boolean) -> Unit,
    onIntervalEditForMaxScoreInput: (Int, String) -> Unit,
    onIntervalEditForTeamCount: (Int, Int) -> Unit,
    onIntervalEditForPrimaryIncrementAdd: (Int) -> Unit,
    onIntervalEditForInitialScoreInput: (Int, String) -> Unit,
    onIntervalEditForPrimaryIncrement: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryIncrementMove: (Int, Int, Boolean) -> Unit,
    onIntervalEditForPrimaryIncrementRemove: (Int, Int) -> Unit,
    onIntervalEditForPrimaryIncrementRefresh: (Int, Int) -> Unit,
    onIntervalEditForPrimaryMappingAllowed: (Int, Boolean) -> Unit,
    onIntervalEditForPrimaryMappingAdd: (Int) -> Unit,
    onIntervalEditForPrimaryMappingOriginalScore: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryMappingDisplayScore: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryMappingMove: (Int, Int, Boolean) -> Unit,
    onIntervalEditForPrimaryMappingRemove: (Int, Int) -> Unit,
    onIntervalEditForSecondaryScoreAllowed: (Int, Boolean) -> Unit,
    onIntervalEditForSecondaryScoreLabel: (Int, String) -> Unit,
    onIntervalAdd: (Int?) -> Unit,
    onIntervalRemove: (Int) -> Unit,
    onIntervalMove: (Boolean, Int) -> Unit,
) {

    val context = LocalContext.current
    val defaultIntervalLabel = stringResource(id = R.string.defaultIntervalLabel)
    val resolvedIntervalLabel = intervalLabel.takeIf { it.isNotEmpty() } ?: defaultIntervalLabel
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(intervalList) { intervalIndex, intervalEditingInfo ->

            val scoreInfo = intervalEditingInfo.scoreInfo
            val intervalData = intervalEditingInfo.intervalData

            val dividerColor = MaterialTheme.colorScheme.onBackground
            HorizontalDivider(
                color = dividerColor,
                modifier = Modifier
                    .padding(vertical = 6.dp)
                    .fillMaxWidth(),
            )

            // Interval Header
            MoveableElement(
                context = context,
                element = { mod ->
                    Text(
                        modifier = mod,
                        text = stringResource(id = R.string.rulesForInterval, resolvedIntervalLabel, intervalIndex + 1),
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                size = intervalList.size,
                currentIndex = intervalIndex,
                lastIndex = intervalList.lastIndex,
                onMoveUp = { onIntervalMove(true, intervalIndex) },
                onMoveDown = { onIntervalMove(false, intervalIndex) },
                onDelete = { onIntervalRemove(intervalIndex) },
            )

            // Sound Effect

            Spacer(modifier = Modifier.height(16.dp))
            val playSoundEffect: (Int?) -> Unit = { rawRes ->
                rawRes?.let {
                    val mMediaPlayer = MediaPlayer.create(context, it)
                    mMediaPlayer.start()
                }
            }
            MultipleOptionsPicker(
                header = stringResource(id = R.string.winSoundEffect),
                headerIconPair = intervalData.soundEffect.takeUnless { it == IntervalEndSound.None }?.let {
                    Speaker to ""
                },
                onHeaderIconClick = {
                    playSoundEffect(intervalData.soundEffect.soundEffectRes())
                },
                options = listOf(
                    OptionData(
                        label = stringResource(id = IntervalEndSound.None.titleRes()),
                        data = IntervalEndSound.None
                    ),
                    OptionData(
                        label = stringResource(id = IntervalEndSound.Bell.titleRes()),
                        data = IntervalEndSound.Bell,
                        iconPair = Speaker to stringResource(id = R.string.bellSoundEffect),
                        onIconClick = { playSoundEffect(IntervalEndSound.Bell.soundEffectRes()) }
                    ),
                    OptionData(
                        label = stringResource(id = IntervalEndSound.Buzzer.titleRes()),
                        data = IntervalEndSound.Buzzer,
                        iconPair = Speaker to stringResource(id = R.string.buzzerSoundEffect),
                        onIconClick = { playSoundEffect(IntervalEndSound.Buzzer.soundEffectRes()) }
                    ),
                    OptionData(
                        label = stringResource(id = IntervalEndSound.LowBuzzer.titleRes()),
                        data = IntervalEndSound.LowBuzzer,
                        iconPair = Speaker to stringResource(id = R.string.lowBuzzerSoundEffect),
                        onIconClick = { playSoundEffect(IntervalEndSound.LowBuzzer.soundEffectRes()) }
                    ),
                    OptionData(
                        label = stringResource(id = IntervalEndSound.Horn.titleRes()),
                        data = IntervalEndSound.Horn,
                        iconPair = Speaker to stringResource(id = R.string.hornSoundEffect),
                        onIconClick = { playSoundEffect(IntervalEndSound.Horn.soundEffectRes()) }
                    ),
                    OptionData(
                        label = stringResource(id = IntervalEndSound.Whistle.titleRes()),
                        data = IntervalEndSound.Whistle,
                        iconPair = Speaker to stringResource(id = R.string.whistleSoundEffect),
                        onIconClick = { playSoundEffect(IntervalEndSound.Whistle.soundEffectRes()) }
                    ),
                ),
                selectedOption = intervalData.soundEffect,
                onOptionSelected = { onIntervalEditForSoundEffect(intervalIndex, it) }
            )

            //Time and Max Score

            Spacer(modifier = Modifier.height(16.dp))
            LabelSwitch(
                label = stringResource(id = R.string.hasTimeLimit),
                checked = !intervalData.increasing,
                onCheckedChange = {
                    onIntervalEditForTimeIsIncreasing(intervalIndex, !it)
                },
                modifier = Modifier.fillMaxWidth(),
            )

            val numberFieldWidth = 65.dp
            if (!intervalData.increasing) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.decreasingTimeMsg))
                    TimeLimitPicker(
                        minuteString = intervalEditingInfo.timeRepresentationPair.first,
                        onMinuteChange = { onIntervalEditForMinute(intervalIndex, it) },
                        secondString = intervalEditingInfo.timeRepresentationPair.second,
                        onSecondChange = { onIntervalEditForSecond(intervalIndex, it) },
                        numberFieldWidth = numberFieldWidth
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                LabelSwitch(
                    label = stringResource(id = R.string.allowDeuceAdvantage),
                    checked = scoreInfo.scoreRule is ScoreRule.Trigger.DeuceAdvantage,
                    onCheckedChange = {
                        onIntervalEditForAllowDeuceAdv(intervalIndex, it)
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
                            onIntervalEditForMaxScoreInput(intervalIndex, it)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.width(numberFieldWidth)
                    )
                }

            }

            //Team count

            Spacer(modifier = Modifier.height(16.dp))
            MultipleOptionsPicker(
                header = stringResource(id = R.string.teamCountHeader, resolvedIntervalLabel),
                options = (MIN_TEAMS..MAX_TEAMS).map {
                    OptionData(
                        label = it.toString(),
                        data = it
                    )
                },

                selectedOption = scoreInfo.dataList.size,
                onOptionSelected = { onIntervalEditForTeamCount(intervalIndex, it) }
            )

            //Primary Scoring

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(id = R.string.initialScoreHeader),
                style = MaterialTheme.typography.titleMedium
            )
            TextField(
                value = intervalEditingInfo.initialScoreInput,
                onValueChange = {
                    if (it.length <= 3)
                        onIntervalEditForInitialScoreInput(intervalIndex, it)
                },
                placeholder = { Text(text = stringResource(R.string.initialScorePlaceHolder)) },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                )

            Spacer(modifier = Modifier.height(16.dp))


            intervalEditingInfo.primaryIncrementInputList.let { increments ->
                IncrementList(
                    context = context,
                    modifier = Modifier.heightIn(min = 0.dp, max = 170.dp * increments.size),
                    numberFieldWidth = numberFieldWidth,
                    intervalIndex = intervalIndex,
                    increments = increments,
                    onIntervalEditForPrimaryIncrementAdd = onIntervalEditForPrimaryIncrementAdd,
                    onIntervalEditForPrimaryIncrement = onIntervalEditForPrimaryIncrement,
                    onIntervalEditForPrimaryIncrementMove = onIntervalEditForPrimaryIncrementMove,
                    onIntervalEditForPrimaryIncrementRemove = onIntervalEditForPrimaryIncrementRemove,
                    onIntervalEditForPrimaryIncrementRefresh = onIntervalEditForPrimaryIncrementRefresh,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LabelSwitch(
                label = stringResource(id = R.string.hasPrimaryScoreToDisplayMapping),
                checked = intervalEditingInfo.allowPrimaryMapping,
                onCheckedChange = {
                    onIntervalEditForPrimaryMappingAllowed(intervalIndex, it)
                },
                modifier = Modifier.fillMaxWidth(),
            )

            if (intervalEditingInfo.allowPrimaryMapping) {
                intervalEditingInfo.primaryMappingInputList.let { mappings ->
                    ScoreMappingList(
                        context = context,
                        modifier = Modifier.heightIn(min = 0.dp, max = LocalConfiguration.current.screenHeightDp.dp * 5 * mappings.size),
                        numberFieldWidth = numberFieldWidth,
                        intervalIndex = intervalIndex,
                        mappings = mappings,
                        onIntervalEditForPrimaryMappingAdd = onIntervalEditForPrimaryMappingAdd,
                        onIntervalEditForPrimaryMappingOriginalScore = onIntervalEditForPrimaryMappingOriginalScore,
                        onIntervalEditForPrimaryMappingDisplayScore = onIntervalEditForPrimaryMappingDisplayScore,
                        onIntervalEditForPrimaryMappingMove = onIntervalEditForPrimaryMappingMove,
                        onIntervalEditForPrimaryMappingRemove = onIntervalEditForPrimaryMappingRemove,
                    )
                }
            }

            // Secondary scoring
            Spacer(modifier = Modifier.height(16.dp))
            LabelSwitch(
                label = stringResource(id = R.string.hasSecondaryScoring),
                checked = intervalEditingInfo.allowSecondaryScore,
                onCheckedChange = {
                    onIntervalEditForSecondaryScoreAllowed(intervalIndex, it)
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (intervalEditingInfo.allowSecondaryScore)
                TextField(
                    value = intervalEditingInfo.scoreInfo.secondaryScoreLabel,
                    onValueChange = { onIntervalEditForSecondaryScoreLabel(intervalIndex, it) },
                    placeholder = { Text(text = stringResource(R.string.secondaryScorePlaceholder)) },
                    modifier = Modifier.fillMaxWidth()
                )


        }
    }

    Spacer(modifier = Modifier.height(16.dp))
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onIntervalAdd(null) }) {
            Text(text = stringResource(R.string.addAnotherInterval, resolvedIntervalLabel))
        }
    }
}

@Composable
private fun IncrementList(
    context: Context,
    modifier: Modifier = Modifier,
    numberFieldWidth: Dp,
    intervalIndex: Int,
    increments: List<String>,
    onIntervalEditForPrimaryIncrementAdd: (Int) -> Unit,
    onIntervalEditForPrimaryIncrement: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryIncrementMove: (Int, Int, Boolean) -> Unit,
    onIntervalEditForPrimaryIncrementRemove: (Int, Int) -> Unit,
    onIntervalEditForPrimaryIncrementRefresh: (Int, Int) -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = stringResource(id = R.string.primaryIncrementsHeader),
            style = MaterialTheme.typography.titleMedium
        )
    }
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(increments) { index, increment ->

            MoveableElement(
                context = context,
                element = { mod ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = mod
                    ) {
                        TextField(
                            value = increment,
                            onValueChange = {
                                if (it.length <= 4)
                                    onIntervalEditForPrimaryIncrement(intervalIndex, index, it)

                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(numberFieldWidth)
                                .onFocusChanged { focusState ->
                                    if (!focusState.isFocused)
                                        onIntervalEditForPrimaryIncrementRefresh(intervalIndex, index)

                                }
                        )
                    }
                },
                size = increments.size,
                currentIndex = index,
                lastIndex = increments.lastIndex,
                onMoveUp = { onIntervalEditForPrimaryIncrementMove(intervalIndex, index, true) },
                onMoveDown = { onIntervalEditForPrimaryIncrementMove(intervalIndex, index, false) },
                onDelete = { onIntervalEditForPrimaryIncrementRemove(intervalIndex, index) },
            )


        }
    }


    if (increments.size < MAX_INCREMENTS_COUNT) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { onIntervalEditForPrimaryIncrementAdd(intervalIndex) }) {
                Text(text = stringResource(R.string.addScoringMethod))
            }
        }
    }
}

@Composable
fun ScoreMappingList(
    context: Context,
    modifier: Modifier = Modifier,
    numberFieldWidth: Dp,
    intervalIndex: Int,
    mappings: List<Pair<String, String>>,
    onIntervalEditForPrimaryMappingAdd: (Int) -> Unit,
    onIntervalEditForPrimaryMappingOriginalScore: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryMappingDisplayScore: (Int, Int, String) -> Unit,
    onIntervalEditForPrimaryMappingMove: (Int, Int, Boolean) -> Unit,
    onIntervalEditForPrimaryMappingRemove: (Int, Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        itemsIndexed(mappings) { index, (originalScore, displayedScore) ->
            MoveableElement(
                context = context,
                element = { mod ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = mod
                    ) {
                        TextField(
                            value = originalScore,
                            onValueChange = {
                                if (it.length <= 3)
                                    onIntervalEditForPrimaryMappingOriginalScore(intervalIndex, index, it)

                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(numberFieldWidth)
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = stringResource(R.string.mapTo)
                        )
                        TextField(
                            value = displayedScore,
                            onValueChange = {
                                if (it.length <= 5)
                                    onIntervalEditForPrimaryMappingDisplayScore(intervalIndex, index, it)

                            },
                            singleLine = true,
                            modifier = Modifier
                                .width(numberFieldWidth)
                        )
                    }
                },
                size = mappings.size,
                currentIndex = index,
                lastIndex = mappings.lastIndex,
                onMoveUp = { onIntervalEditForPrimaryMappingMove(intervalIndex, index, true) },
                onMoveDown = { onIntervalEditForPrimaryMappingMove(intervalIndex, index, false) },
                onDelete = { onIntervalEditForPrimaryMappingRemove(intervalIndex, index) },
            )
        }
    }
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onIntervalEditForPrimaryMappingAdd(intervalIndex) }) {
            Text(text = stringResource(R.string.addScoreMapping))
        }
    }
}

@Composable
private fun ScoreboardDetailsInnerDialogContentForPreview(
    uiEvent: Flow<UiEvent> = emptyFlow(),
    onUiEvent: (UiEvent) -> Unit = {},
    title: String = "",
    onTitleChange: (String) -> Unit = {},
    description: String = "",
    onDescriptionChange: (String) -> Unit = {},
    winRule: WinRule = WinRule.Final,
    onWinRuleChange: (WinRule) -> Unit = {},
    icon: ScoreboardIcon? = ScoreboardIcon.HOCKEY,
    onIconChange: (ScoreboardIcon) -> Unit = {},
    iconChanging: Boolean = false,
    onIconEdit: (Boolean) -> Unit = {},
    intervalLabel: String = "Period",
    onIntervalLabelChange: (String) -> Unit = {},
    onIntervalEditForSoundEffect: (Int, IntervalEndSound) -> Unit = { _, _ -> },
    onIntervalEditForTimeIsIncreasing: (Int, Boolean) -> Unit = { _, _ -> },
    onIntervalEditForMinute: (Int, String) -> Unit = { _, _ -> },
    onIntervalEditForSecond: (Int, String) -> Unit = { _, _ -> },
    onIntervalEditForAllowDeuceAdv: (Int, Boolean) -> Unit = { _, _ -> },
    onIntervalEditForMaxScoreInput: (Int, String) -> Unit = { _, _ -> },
    onIntervalEditForTeamCount: (Int, Int) -> Unit = { _, _ -> },
    onIntervalEditForPrimaryIncrementAdd: (Int) -> Unit = { _ -> },
    onIntervalEditForInitialScoreInput: (Int, String) -> Unit = { _, _ -> },
    onIntervalEditForPrimaryIncrement: (Int, Int, String) -> Unit = { _, _, _ -> },
    onIntervalEditForPrimaryIncrementMove: (Int, Int, Boolean) -> Unit = { _, _, _ -> },
    onIntervalEditForPrimaryIncrementRemove: (Int, Int) -> Unit = { _, _ -> },
    onIntervalEditForPrimaryIncrementRefresh: (Int, Int) -> Unit = { _, _ -> },
    onIntervalEditForPrimaryMappingAllowed: (Int, Boolean) -> Unit = { _, _ -> },
    onIntervalEditForPrimaryMappingAdd: (Int) -> Unit = { _ -> },
    onIntervalEditForPrimaryMappingOriginalScore: (Int, Int, String) -> Unit = { _, _, _ -> },
    onIntervalEditForPrimaryMappingDisplayScore: (Int, Int, String) -> Unit = { _, _, _ -> },
    onIntervalEditForPrimaryMappingMove: (Int, Int, Boolean) -> Unit = { _, _, _ -> },
    onIntervalEditForPrimaryMappingRemove: (Int, Int) -> Unit = { _, _ -> },
    onIntervalEditForSecondaryScoreAllowed: (Int, Boolean) -> Unit = { _, _ -> },
    onIntervalEditForSecondaryScoreLabel: (Int, String) -> Unit = { _, _ -> },
    onIntervalAdd: (Int?) -> Unit = { _ -> },
    onIntervalRemove: (Int) -> Unit = { _ -> },
    onIntervalMove: (Boolean, Int) -> Unit = { _, _ -> },
    valid: Boolean = true,
    isNewEntity: Boolean = true,
    intervalList: List<IntervalEditingInfo> = listOf(),
    onDelete: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onConfirm: () -> Unit = {},
) {
    ScoreboardDetailsInnerDialogContent(
        uiEvent,
        onUiEvent,
        title,
        onTitleChange,
        description,
        onDescriptionChange,
        winRule,
        onWinRuleChange,
        icon,
        onIconChange,
        iconChanging,
        onIconEdit,
        intervalLabel,
        onIntervalLabelChange,
        onIntervalEditForSoundEffect,
        onIntervalEditForTimeIsIncreasing,
        onIntervalEditForMinute,
        onIntervalEditForSecond,
        onIntervalEditForAllowDeuceAdv,
        onIntervalEditForMaxScoreInput,
        onIntervalEditForTeamCount,
        onIntervalEditForPrimaryIncrementAdd,
        onIntervalEditForInitialScoreInput,
        onIntervalEditForPrimaryIncrement,
        onIntervalEditForPrimaryIncrementMove,
        onIntervalEditForPrimaryIncrementRemove,
        onIntervalEditForPrimaryIncrementRefresh,
        onIntervalEditForPrimaryMappingAllowed,
        onIntervalEditForPrimaryMappingAdd,
        onIntervalEditForPrimaryMappingOriginalScore,
        onIntervalEditForPrimaryMappingDisplayScore,
        onIntervalEditForPrimaryMappingMove,
        onIntervalEditForPrimaryMappingRemove,
        onIntervalEditForSecondaryScoreAllowed,
        onIntervalEditForSecondaryScoreLabel,
        onIntervalAdd,
        onIntervalRemove,
        onIntervalMove,
        valid,
        isNewEntity,
        intervalList,
        onDelete,
        onDismiss,
        onConfirm,
    )
}

@Preview(showBackground = true)
@Composable
private fun `New Icon Selection`() =
    ScoreboardDetailsInnerDialogContentForPreview(
        iconChanging = true,
    )

@Preview(showBackground = true)
@Composable
private fun `Basketball`() =
    ScoreboardDetailsInnerDialogContentForPreview(
        icon = ScoreboardIcon.BASKETBALL,
        intervalLabel = "Quarter",
        isNewEntity = false,
    )

@Preview(showBackground = true)
@Composable
private fun `Hockey`() =
    ScoreboardDetailsInnerDialogContentForPreview(
        icon = ScoreboardIcon.HOCKEY,
        intervalLabel = "Period",
    )

@Preview(showBackground = true)
@Composable
private fun `Loading Icon`() =
    ScoreboardDetailsInnerDialogContentForPreview(
        icon = null,
    )


@Preview(showBackground = true)
@Composable
private fun `One default interval`() =
    ScoreboardDetailsInnerDialogContentForPreview(
        intervalLabel = "Round",
        intervalList = listOf(
            IntervalEditingInfo(
                scoreInfo = ScoreInfo(
                    scoreRule = ScoreRule.None,
                    scoreToDisplayScoreMap = mapOf(),
                    secondaryScoreLabel = "",
                    dataList = listOf()
                ),
                intervalData = IntervalData(
                    current = 0,
                    initial = 0,
                    increasing = false
                ),
                timeRepresentationPair = Pair("9", "24"),
                maxScoreInput = "33",
                initialScoreInput = "10",
                primaryIncrementInputList = listOf("+1"),
                allowPrimaryMapping = false,
                primaryMappingInputList = listOf(),
                allowSecondaryScore = false
            ),
        )
    )
