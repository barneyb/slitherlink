package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dot
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

fun singleLoop(p: Puzzle) = buildSequence {
    val segments = mutableMapOf<Dot, Dot>()
    for (start in p.dots()) {
        if (segments.containsKey(start)) {
            // already found it in the other direction
            continue
        }
        val edges = start.edges(ON)
        if (edges.size == 1) {
            // an end
            val e = edges[0]
            val stats = findOtherEndHelper(start.otherEnd(e), start)
            segments[stats.otherEnd] = start
        }
    }

    for ((start, end) in segments) {
        if (!start.adjacent(end)) {
            continue // don't care
        }
        val edge = start.edgeTo(end)
        if (edge.state != UNKNOWN) {
            continue // already filled in
        }
        if (segments.size > 1) {
            // multiple loops means none can close
            setTo(edge, OFF)
            continue
        }
        /*
         The "all clues satisfied" here is interesting, because there MUST be
         some cells which don't have their final number of edges, but they
         can't have clues, because otherwise needsRemaining would have filled
         it in. So it's more selective than it has to be, but we'll never know
         because it's covered up elsewhere.
         */
        setTo(edge, if (p.clueCells().all { it.satisfied }) ON else OFF)
    }
}
