package com.barneyb.slitherlink.viz

import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.SolveState
import java.awt.Color
import java.awt.Point
import java.util.concurrent.atomic.AtomicInteger

private var frameCount = AtomicInteger()

fun nextFrameLocation(): Point {
    val i = frameCount.getAndUpdate {
        if (it > 15) 0 else it + 1
    }
    return Point(
        200 + 25 * i,
        200 + 25 * i
    )
}

fun visualize(p: Puzzle, work: (SnapshotViewer) -> Unit = {}) {
    val viewer = SnapshotViewer(p)
    work(viewer)
    viewer.show()
}

fun visualize(ss: SolveState) {
    SolveViewer(ss).show()
}

val highlightColors = listOf(
        Color.GREEN,
        Color.PINK,
        Color.YELLOW,
        Color.CYAN,
        Color.ORANGE,
        Color.LIGHT_GRAY
)
    .map {
        Color(it.red, it.green, it.blue, 127)
    }
