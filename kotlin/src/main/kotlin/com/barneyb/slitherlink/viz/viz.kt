package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SolveState
import java.awt.Color

fun visualize(p: Puzzle, work: (SnapshotViewer) -> Unit = {}) {
    val viewer = SnapshotViewer(p)
    work(viewer)
    viewer.show()
}

fun visualize(ss: SolveState) {
    SolveViewer(ss).show()
}

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
