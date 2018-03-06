package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Puzzle
import java.awt.Color

fun visualize(p: Puzzle) = SnapshotViewer(p)

val highlightColors = listOf(
        Color.YELLOW,
        Color.PINK,
        Color.GREEN,
        Color.ORANGE,
        Color.LIGHT_GRAY,
        Color.CYAN
)
    .map {
        Color(it.red, it.green, it.blue, 127)
    }
