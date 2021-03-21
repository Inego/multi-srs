package org.inego.multisrs.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout

// Adapted from https://gist.github.com/cbeyls/66142cc04348d8246475b730aabef701

@Composable
fun FlowLayout(
    modifier: Modifier = Modifier,
    childVerticalGravity: Alignment.Vertical = Alignment.Top,
    content: @Composable () -> Unit
) {
    Layout(
        content = content,
        modifier = modifier
    ) { measurables, constraints ->

        val childConstraints = constraints.copy(minWidth = 0, minHeight = 0)

        val placeables = measurables.map { it.measure(childConstraints) }

        val rows = mutableListOf<RowInfo>()

        var rowWidth = 0
        var rowHeight = 0

        val maxWidth = constraints.maxWidth

        placeables.forEachIndexed { index, placeable ->
            val newRowWidth = rowWidth + placeable.width
            if (newRowWidth > maxWidth) {
                rows.add(RowInfo(width = rowWidth, height = rowHeight, nextChildIndex = index))
                rowWidth = placeable.width
                rowHeight = placeable.height
            } else {
                rowWidth = newRowWidth
                rowHeight = maxOf(rowHeight, placeable.height)
            }
        }

        rows.add(RowInfo(width = rowWidth, height = rowHeight, nextChildIndex = measurables.size))

        val contentWidth = rows.maxOf { it.width }
        val contentHeight = rows.sumBy { it.height }

        layout(
            width = maxOf(contentWidth, constraints.minWidth),
            height = maxOf(contentHeight, constraints.minHeight)
        ) {
            var childIndex = 0
            var y = 0

            rows.forEach { rowInfo ->
                var x = 0
                while (childIndex < rowInfo.nextChildIndex) {
                    val placeable = placeables[childIndex]
                    placeable.placeRelative(
                        x = x,
                        y = y + childVerticalGravity.align(rowInfo.height, placeable.height)
                    )
                    x += placeable.width
                    childIndex++
                }
                y += rowInfo.height
            }
        }
    }
}

private class RowInfo(val width: Int, val height: Int, val nextChildIndex: Int)
