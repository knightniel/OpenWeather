package com.nielaclag.openweather.presentation.ui.composable.component.core

import android.annotation.SuppressLint
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TabRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Created by Niel on 10/21/2024.
 */
@Composable
fun CustomScrollableTabRow(
    selectedTabIndex: Int,
    modifier: Modifier = Modifier,
    minimumTabWidth: Dp = ScrollableTabRowMinimumTabWidth,
    edgePadding: Dp = ScrollableTabRowPadding,
    tabBaseBackground: @Composable (index: Int) -> Unit = {},
    backgroundIndicator: @Composable (tabPositions: List<TabRowPosition>) -> Unit = @Composable { tabPositions ->
        TabRowIndicator(
            Modifier.tabRowIndicatorOffset(tabPositions[selectedTabIndex])
        )
    },
    foregroundIndicator: @Composable (rowTabPositions: List<TabRowPosition>) -> Unit = @Composable { tabPositions ->
        TabRowIndicator(
            Modifier.tabRowIndicatorOffset(tabPositions[selectedTabIndex])
        )
    },
    bottomDivider: @Composable () -> Unit = @Composable {
        HorizontalDivider()
    },
    tabDivider: @Composable (index: Int) -> Unit = {},
    tabs: @Composable () -> Unit
) {
//    Surface( // SURFACE CLIPS BOUND AND TABS COULDN'T HAVE SHADOWS, USED BOX INSTEAD
    Box(
        modifier = modifier
    ) {
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()
        val scrollableTabData = remember(scrollState, coroutineScope) {
            ScrollableTabData(
                scrollState = scrollState,
                coroutineScope = coroutineScope
            )
        }
        SubcomposeLayout(
            modifier = Modifier
                .wrapContentSize(align = Alignment.CenterStart)
                .horizontalScroll(scrollState)
                .selectableGroup()
//                .clipToBounds()
        ) { constraints ->
//            val minTabWidth = ScrollableTabRowMinimumTabWidth.roundToPx()
            val minTabWidth = minimumTabWidth.roundToPx()
            val padding = edgePadding.roundToPx()

            val tabMeasurables = subcompose(TabRowSlots.Tabs, tabs)
            val layoutHeight = tabMeasurables.fold(initial = 0) { curr, measurable ->
                maxOf(curr, measurable.maxIntrinsicHeight(Constraints.Infinity))
            }

            val tabConstraints = constraints.copy(minWidth = minTabWidth, minHeight = layoutHeight)
            val tabPlaceables = tabMeasurables.map { it.measure(tabConstraints) }

            val tabDividerConstraints = constraints.copy(maxHeight = layoutHeight)
            val tabDividerMeasurables = List(tabMeasurables.take((tabMeasurables.size - 1).coerceAtLeast(0)).size) { index ->
                subcompose("${ TabRowSlots.TabDivider }_$index") {
                    tabDivider(index)
                }
            }
            val tabDividerPlaceables = tabDividerMeasurables.map {
                it.map { tabBaseBackgroundMeasurable ->
                    tabBaseBackgroundMeasurable.measure(tabDividerConstraints)
                }
            }

            val layoutWidth = tabPlaceables.fold(initial = padding * 2) { curr, placeable ->
                curr + placeable.width
            } + tabDividerPlaceables.fold(initial = 0) { curr, placeables ->
                curr + placeables.fold(initial = 0) { curr2, placeable ->
                    maxOf(curr2, placeable.width)
                }
            }

            // Position the children.
            layout(layoutWidth, layoutHeight) {
                val tabPositions = mutableListOf<TabRowPosition>()
                val tabDividerPositions = mutableListOf<TabRowPosition>()
                var left = padding

                tabPlaceables.forEachIndexed { index, tabPlaceable ->
                    tabPositions.add(
                        TabRowPosition(
                            left = left.toDp(),
                            width = tabPlaceable.width.toDp()
                        )
                    )
                    left += tabPlaceable.width

                    tabDividerPlaceables.getOrNull(index)?.let { tabDividerPlaceable ->
                        val width = tabDividerPlaceable.fold(initial = 0) { curr2, placeable ->
                            maxOf(curr2, placeable.width)
                        }
                        tabDividerPositions.add(
                            TabRowPosition(
                                left = left.toDp(),
                                width = width.toDp()
                            )
                        )
                        left += width
                    }
                }

//                // Base backgrounds for each tab
                tabMeasurables.forEachIndexed { index, measurables ->
                    subcompose("${ TabRowSlots.TabBaseBackground }_$index") {
                        tabBaseBackground(index)
                    }.forEach { tabMeasurable ->
                        tabMeasurable
                            .measure(Constraints.fixed(tabPlaceables[index].width, layoutHeight))
                            .placeRelative(tabPositions[index].left.roundToPx(), 0)
                    }
                }

                // The indicator container is measured to fill the entire space occupied by the tab
                // row, and then placed on top of the divider.
                subcompose(TabRowSlots.BackgroundIndicator) {
                    backgroundIndicator(tabPositions)
                }.forEach { backgroundIndicator ->
                    backgroundIndicator
                        .measure(Constraints.fixed(layoutWidth, layoutHeight))
                        .placeRelative(0, 0)
                }

                // Place the tabs
                tabPlaceables.forEachIndexed { index, placeable ->
                    placeable.placeRelative(tabPositions[index].left.roundToPx(), 0)
//                    tabPositions.add(TabPosition(left = left.toDp(), width = it.width.toDp()))
//                    left += it.width
                }

                // Place the tab dividers
                tabDividerPlaceables.forEachIndexed { index, placeables ->
                    placeables.forEach { placeable ->
                        val height = placeables.fold(initial = 0) { curr, placeables2 ->
                            maxOf(curr, placeables2.height)
                        }
                        placeable.placeRelative(tabDividerPositions[index].left.roundToPx(), (layoutHeight - height) / 2)
                    }
//                    tabPositions.add(TabPosition(left = left.toDp(), width = it.width.toDp()))
//                    left += it.width
                }

                // The divider is measured with its own height, and width equal to the total width
                // of the tab row, and then placed on top of the tabs.
                subcompose(TabRowSlots.BottomDivider, bottomDivider).forEach {
                    val placeable = it.measure(
                        constraints.copy(
                            minHeight = 0,
                            minWidth = layoutWidth,
                            maxWidth = layoutWidth
                        )
                    )
                    placeable.placeRelative(0, layoutHeight - placeable.height)
                }

                // The indicator container is measured to fill the entire space occupied by the tab
                // row, and then placed on top of the divider.
                subcompose(TabRowSlots.ForegroundIndicator) {
                    foregroundIndicator(tabPositions)
                }.forEach {
                    it.measure(Constraints.fixed(layoutWidth, layoutHeight)).placeRelative(0, 0)
                }

                scrollableTabData.onLaidOut(
                    density = this@SubcomposeLayout,
                    edgeOffset = padding,
                    rowTabPositions = tabPositions,
                    selectedTab = selectedTabIndex
                )
            }
        }
    }
}

/**
 * Default indicator, which will be positioned at the bottom of the [TabRow], on top of the
 * divider.
 *
 * @param modifier modifier for the indicator's layout
 * @param height height of the indicator
 * @param color color of the indicator
 */
@Composable
fun TabRowIndicator(
    modifier: Modifier = Modifier,
    height: Dp = 3.dp,
    color: Color = MaterialTheme.colorScheme.outline
) {
    Box(
        modifier
            .fillMaxWidth()
            .height(height)
            .background(color = color)
    )
}

enum class TabRowSlots {
    Tabs,
    BottomDivider,
    ForegroundIndicator,
    BackgroundIndicator,
    TabBaseBackground,
    TabDivider
}

/**
 * [Modifier] that takes up all the available width inside the [TabRow], and then animates
 * the offset of the indicator it is applied to, depending on the [currentTabPosition].
 *
 * @param currentTabPosition [TabRowPosition] of the currently selected tab. This is used to
 * calculate the offset of the indicator this modifier is applied to, as well as its width.
 */
@SuppressLint("UseOfNonLambdaOffsetOverload")
fun Modifier.tabRowIndicatorOffset(
    currentTabPosition: TabRowPosition
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {
    val scope = rememberCoroutineScope()
    var tabLoaded by remember {
        mutableStateOf(false)
    }
    val currentTabWidth = animateDpAsState(
        targetValue = currentTabPosition.width,
        animationSpec = if (tabLoaded) tween(durationMillis = 250, easing = FastOutSlowInEasing) else snap(),
        finishedListener = {
            tabLoaded = true
        },
        label = "Tab Width"
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = if (tabLoaded) tween(durationMillis = 250, easing = FastOutSlowInEasing) else snap(),
        finishedListener = {
            tabLoaded = true
        },
        label = "Indicator Offset"
    )
    LaunchedEffect(indicatorOffset) {
        scope.launch {
            delay(200)
            tabLoaded = true
        }
    }
    fillMaxWidth()
        .wrapContentSize(Alignment.BottomStart)
        .offset(x = indicatorOffset)
        .width(currentTabWidth.value)
}

@Immutable
class TabRowPosition internal constructor(val left: Dp, val width: Dp) {
    val right: Dp get() = left + width

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TabRowPosition) return false

        if (left != other.left) return false
        if (width != other.width) return false

        return true
    }

    override fun hashCode(): Int {
        var result = left.hashCode()
        result = 31 * result + width.hashCode()
        return result
    }

    override fun toString(): String {
        return "TabPosition(left=$left, right=$right, width=$width)"
    }
}

internal class ScrollableTabData(
    private val scrollState: ScrollState,
    private val coroutineScope: CoroutineScope
) {
    private var selectedTab: Int? = null

    fun onLaidOut(
        density: Density,
        edgeOffset: Int,
        rowTabPositions: List<TabRowPosition>,
        selectedTab: Int
    ) {
        // Animate if the new tab is different from the old tab, or this is called for the first
        // time (i.e selectedTab is `null`).
        if (this.selectedTab != selectedTab) {
            this.selectedTab = selectedTab
            rowTabPositions.getOrNull(selectedTab)?.let {
                // Scrolls to the tab with [tabPosition], trying to place it in the center of the
                // screen or as close to the center as possible.
                val calculatedOffset = it.calculateTabOffset(density, edgeOffset, rowTabPositions)
                if (scrollState.value != calculatedOffset) {
                    coroutineScope.launch {
                        scrollState.animateScrollTo(
                            calculatedOffset,
                            animationSpec = ScrollableTabRowScrollSpec
                        )
                    }
                }
            }
        }
    }

    /**
     * @return the offset required to horizontally center the tab inside this TabRow.
     * If the tab is at the start / end, and there is not enough space to fully centre the tab, this
     * will just clamp to the min / max position given the max width.
     */
    private fun TabRowPosition.calculateTabOffset(
        density: Density,
        edgeOffset: Int,
        rowTabPositions: List<TabRowPosition>
    ): Int = with(density) {
        val totalTabRowWidth = rowTabPositions.last().right.roundToPx() + edgeOffset
        val visibleWidth = totalTabRowWidth - scrollState.maxValue
        val tabOffset = left.roundToPx()
        val scrollerCenter = visibleWidth / 2
        val tabWidth = width.roundToPx()
        val centeredTabOffset = tabOffset - (scrollerCenter - tabWidth / 2)
        // How much space we have to scroll. If the visible width is <= to the total width, then
        // we have no space to scroll as everything is always visible.
        val availableSpace = (totalTabRowWidth - visibleWidth).coerceAtLeast(0)
        return centeredTabOffset.coerceIn(0, availableSpace)
    }
}

/**
 * [AnimationSpec] used when scrolling to a tab that is not fully visible.
 */
private val ScrollableTabRowScrollSpec: AnimationSpec<Float> = tween(
    durationMillis = 250,
    easing = FastOutSlowInEasing
)


private val ScrollableTabRowMinimumTabWidth = 90.dp
private val ScrollableTabRowPadding = 52.dp