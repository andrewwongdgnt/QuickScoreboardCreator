@file:OptIn(ExperimentalMaterial3Api::class)

package com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails

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
import androidx.compose.material3.Surface
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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.R
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.DefaultAlertDialog
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.IconDisplay
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.LabelSwitch
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.MoveableElement
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.MultipleOptionsPicker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.OptionData
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.TimeLimitPicker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconDrawableResHolder
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconGroupStringResHolder
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.composable.iconpicker.IconPicker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.imagevector.Speaker
import com.dgnt.quickScoreboardCreator.core.presentation.designsystem.theme.QuickScoreboardCreatorTheme
import com.dgnt.quickScoreboardCreator.core.presentation.ui.uievent.UiEvent
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.SportIcon
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.interval.IntervalEndSound
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.ScoreRule
import com.dgnt.quickScoreboardCreator.feature.sport.domain.model.score.WinRule
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.iconRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.soundEffectRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.resourcemapping.titleRes
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails.SportDetailsViewModel.Companion.MAX_INCREMENTS_COUNT
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails.SportDetailsViewModel.Companion.MAX_TEAMS
import com.dgnt.quickScoreboardCreator.feature.sport.presentation.sportdetails.SportDetailsViewModel.Companion.MIN_TEAMS
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

@Composable
fun SportDetailsDialogContent(
    onUiEvent: (UiEvent) -> Unit,
    viewModel: SportDetailsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SportDetailsInnerDialogContent(
        uiEvent = viewModel.uiEvent,
        onUiEvent = onUiEvent,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun SportDetailsInnerDialogContent(
    uiEvent: Flow<UiEvent>,
    onUiEvent: (UiEvent) -> Unit,
    state: SportDetailsState,
    onAction: (SportDetailsAction) -> Unit
) = state.run {

    LaunchedEffect(key1 = true) {
        uiEvent.collect(collector = onUiEvent)
    }

    val context = LocalContext.current
    DefaultAlertDialog(
        title = stringResource(id = R.string.sportDetailsTitle),
        actionIcon = Icons.Default.Delete.takeUnless { isNewEntity },
        actionContentDescription = stringResource(id = R.string.delete),
        actionOnClick = {
            Toast.makeText(context, R.string.longClickDeleteMsg, Toast.LENGTH_LONG).show()
        },
        actionOnLongClick = { onAction(SportDetailsAction.Delete) },
        confirmText = stringResource(id = android.R.string.ok),
        confirmEnabled = valid,
        onConfirm = { onAction(SportDetailsAction.Confirm) },
        dismissText = stringResource(id = android.R.string.cancel),
        onDismiss = { onAction(SportDetailsAction.Dismiss) }
    ) {
        if (iconState is SportIconState.Picked.Changing)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.Start
            ) {
                IconPicker(
                    iconGroups = mapOf(IconGroupStringResHolder(R.string.pickIconMsg) to SportIcon.entries.map { IconDrawableResHolder(it, it.iconRes()) }),
                    onCancel = { onAction(SportDetailsAction.IconEdit(false)) },
                    onIconChange = { onAction(SportDetailsAction.IconChange(it.originalIcon)) }
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
                    onTitleChange = { onAction(SportDetailsAction.TitleChange(it)) },
                    description = description,
                    onDescriptionChange = { onAction(SportDetailsAction.DescriptionChange(it)) },
                )
                Spacer(modifier = Modifier.height(16.dp))
                IconDisplay(
                    iconRes = (iconState as? SportIconState.Picked)?.sportIcon?.iconRes(),
                    onClick = { onAction(SportDetailsAction.IconEdit(true)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                WinRulePicker(
                    winRule = winRule,
                    onWinRuleChange = { onAction(SportDetailsAction.WinRuleChange(it)) }
                )
                Spacer(modifier = Modifier.height(16.dp))
                IntervalLabelEditor(
                    intervalLabel = intervalLabel,
                    onIntervalLabelChange = { onAction(SportDetailsAction.IntervalLabelChange(it))}
                )
                Spacer(modifier = Modifier.height(16.dp))
                IntervalList(
                    modifier = Modifier.heightIn(min = 0.dp, max = LocalConfiguration.current.screenHeightDp.dp * 10 * intervalList.size),
                    state = state,
                    onAction = onAction
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
    state: SportDetailsState,
    onAction: (SportDetailsAction) -> Unit
) =state.run{

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
                onMoveUp = { onAction(SportDetailsAction.IntervalMove(true, intervalIndex)) },
                onMoveDown = { onAction(SportDetailsAction.IntervalMove(false, intervalIndex)) },
                onDelete = { onAction(SportDetailsAction.IntervalRemove(intervalIndex)) },
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
                onOptionSelected = { onAction(SportDetailsAction.IntervalEditForSoundEffect(intervalIndex, it)) }
            )

            //Time and Max Score

            Spacer(modifier = Modifier.height(16.dp))
            LabelSwitch(
                label = stringResource(id = R.string.hasTimeLimit),
                checked = !intervalData.increasing,
                onCheckedChange = {
                    onAction(SportDetailsAction.IntervalEditForTimeIsIncreasing(intervalIndex, !it))
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
                        onMinuteChange = { onAction(SportDetailsAction.IntervalEditForMinute(intervalIndex, it)) },
                        secondString = intervalEditingInfo.timeRepresentationPair.second,
                        onSecondChange = { onAction(SportDetailsAction.IntervalEditForSecond(intervalIndex, it)) },
                        numberFieldWidth = numberFieldWidth
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(16.dp))
                LabelSwitch(
                    label = stringResource(id = R.string.allowDeuceAdvantage),
                    checked = scoreInfo.scoreRule is ScoreRule.Trigger.DeuceAdvantage,
                    onCheckedChange = {
                        onAction(SportDetailsAction.IntervalEditForAllowDeuceAdv(intervalIndex, it))
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
                            onAction(SportDetailsAction.IntervalEditForMaxScoreInput(intervalIndex, it))
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
                onOptionSelected = { onAction(SportDetailsAction.IntervalEditForTeamCount(intervalIndex, it)) }
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
                        onAction(SportDetailsAction.IntervalEditForInitialScoreInput(intervalIndex, it))
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
                    onAction = onAction,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            LabelSwitch(
                label = stringResource(id = R.string.hasPrimaryScoreToDisplayMapping),
                checked = intervalEditingInfo.allowPrimaryMapping,
                onCheckedChange = {
                    onAction(SportDetailsAction.IntervalEditForPrimaryMappingAllowed(intervalIndex, it))
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
                        onAction = onAction,
                    )
                }
            }

            // Secondary scoring
            Spacer(modifier = Modifier.height(16.dp))
            LabelSwitch(
                label = stringResource(id = R.string.hasSecondaryScoring),
                checked = intervalEditingInfo.allowSecondaryScore,
                onCheckedChange = {
                    onAction(SportDetailsAction.IntervalEditForSecondaryScoreAllowed(intervalIndex, it))
                },
                modifier = Modifier.fillMaxWidth(),
            )
            if (intervalEditingInfo.allowSecondaryScore)
                TextField(
                    value = intervalEditingInfo.scoreInfo.secondaryScoreLabel,
                    onValueChange = { onAction(SportDetailsAction.IntervalEditForSecondaryScoreLabel(intervalIndex, it)) },
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
        Button(onClick = { onAction(SportDetailsAction.IntervalAdd()) }) {
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
    onAction: (SportDetailsAction) -> Unit
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
                                    onAction(SportDetailsAction.IntervalEditForPrimaryIncrement(intervalIndex, index, it))

                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier
                                .width(numberFieldWidth)
                                .onFocusChanged { focusState ->
                                    if (!focusState.isFocused)
                                        onAction(SportDetailsAction.IntervalEditForPrimaryIncrementRefresh(intervalIndex, index))

                                }
                        )
                    }
                },
                size = increments.size,
                currentIndex = index,
                lastIndex = increments.lastIndex,
                onMoveUp = { onAction(SportDetailsAction.IntervalEditForPrimaryIncrementMove(intervalIndex, index, true)) },
                onMoveDown = { onAction(SportDetailsAction.IntervalEditForPrimaryIncrementMove(intervalIndex, index, false)) },
                onDelete = { onAction(SportDetailsAction.IntervalEditForPrimaryIncrementRemove(intervalIndex, index)) },
            )


        }
    }


    if (increments.size < MAX_INCREMENTS_COUNT) {
        Row {
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = { onAction(SportDetailsAction.IntervalEditForPrimaryIncrementAdd(intervalIndex)) }) {
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
    onAction: (SportDetailsAction) -> Unit
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
                                    onAction(SportDetailsAction.IntervalEditForPrimaryMappingOriginalScore(intervalIndex, index, it))

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
                                    onAction(SportDetailsAction.IntervalEditForPrimaryMappingDisplayScore(intervalIndex, index, it))

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
                onMoveUp = { onAction(SportDetailsAction.IntervalEditForPrimaryMappingMove(intervalIndex, index, true)) },
                onMoveDown = { onAction(SportDetailsAction.IntervalEditForPrimaryMappingMove(intervalIndex, index, false)) },
                onDelete = { onAction(SportDetailsAction.IntervalEditForPrimaryMappingRemove(intervalIndex, index)) },
            )
        }
    }
    Row {
        Spacer(modifier = Modifier.weight(1f))
        Button(onClick = { onAction(SportDetailsAction.IntervalEditForPrimaryMappingAdd(intervalIndex)) }) {
            Text(text = stringResource(R.string.addScoreMapping))
        }
    }
}
@Preview(showBackground = true)
@Composable
private fun SportDetailsDialogContentPreview(
    @PreviewParameter(SportDetailsPreviewStateProvider::class) state: SportDetailsState
) = QuickScoreboardCreatorTheme {
    Surface {
        SportDetailsInnerDialogContent(
            uiEvent = emptyFlow(),
            onUiEvent = {},
            state = state,
            onAction = {}
        )
    }
}
