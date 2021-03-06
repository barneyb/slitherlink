package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

/**
 * If an edge would close a loop without satisfying all the clues AND being
 * fully connected, the edge is [OFF]. If it _is_ fully connected and all
 * clues are satifieied, the edge is [ON] (for the win).
 */
fun singleLoop(p: Puzzle) = buildSequence {
    val segments = allSegments(p)
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

/**
 * If placing an edge leads to a rules violation, that ends must be [OFF].
 */
fun cantForceIllegalMove(p: Puzzle) = buildSequence {
    for (s in allSegmentsDirected(p)) {
        for (e in s.start.edges(UNKNOWN)) {
            val (contradiction, evidence) = createsContradiction(p, Move(e, ON))
            if (contradiction) {
                yield(Move(e, OFF, evidence))
                return@buildSequence
            }
        }
    }
}
