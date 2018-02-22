package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

fun clueSatisfied(p: Puzzle) = buildSequence {
    for (c in p.clueCells()) {
        val edges = c.edges
        if (edges.count { it.on } == c.clue) {
            setUnknownTo(edges, OFF)
        }
    }
}

fun needAllRemaining(p: Puzzle) = buildSequence {
    for (c in p.clueCells()) {
        val unknown = c.edges(UNKNOWN)
        if (unknown.isEmpty()) {
            continue
        }
        val onCount = c.edges(ON).size
        if (onCount + unknown.size == c.clue) {
            setTo(unknown, ON)
        }
    }
}

fun reachOneShortOfSatisfiedMustStay(p: Puzzle) = buildSequence {
    for (c in p.clueCells()) {
        if (c.edges(OFF).size == 3 - c.clue) {
            // one short of satisfied
            for (d in c.dots) {
                if (c.externalEdges(d).count { it.on } == 1) {
                    // one line to it
                    if (c.internalEdges(d).all { it.unknown }) {
                        // this cell can will receive
                        setUnknownTo(c.externalEdges(d), OFF)
                        setUnknownTo(c.opposedEdges(d), ON)
                    }
                }
            }
        }
    }
}
