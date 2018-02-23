package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.*
import kotlin.coroutines.experimental.buildSequence

fun forcedToOne(p: Puzzle) = buildSequence {
    for (pair in allXorPairs(p)) {
        if (pair.cell.clue == ONE) {
            setUnknownTo(pair.opposedEdges, OFF)
        }
    }
}

fun singleXorPairEgress(p: Puzzle) = buildSequence {
    for (pair in allXorPairs(p)) {
        val externalEdges = pair.externalEdges
        if (externalEdges.count { it.unknown } == 1) {
            if (externalEdges.count { it.on } == 0) {
                setUnknownTo(externalEdges, ON)
            } else {
                setUnknownTo(externalEdges, OFF)
            }
        }
    }
}

fun threeTouchedByXorPair(p: Puzzle) = buildSequence {
    for (pair in allXorPairs(p)) {
        if (pair.hasOpposedCell) {
            val c = pair.opposedCell
            if (c.clue == THREE) {
                setUnknownTo(c.opposedEdges(pair.dot), ON)
            }
        }
    }
}

private fun allXorPairs(p: Puzzle) = propagateAlongTwos(
        buildSequence {
            yieldAll(lastTwoUnknownEdgesOfCell(p))
            yieldAll(forcedTo(p))
        })

private fun lastTwoUnknownEdgesOfCell(p: Puzzle) = buildSequence {
    for (c in p.clueCells()) {
        val unknown = c.edges(UNKNOWN)
        val on = c.edges(ON)
        if (unknown.size == 2 && on.size == c.clue - 1) {
            // one away from satisfied, and two unknowns
            val dots = unknown.first().dots.intersect(unknown.last().dots)
            if (dots.size == 1) {
                // unknowns make a corner!
                maybeYieldXorPair(c, dots.first())
            }
        }
    }
}

private fun forcedTo(p: Puzzle) = buildSequence {
    for (c in p.cells) {
        for (d in c.dots) {
            val externalEdges = c.externalEdges(d)
            if (externalEdges.count { it.on } == 1
                    && externalEdges.count { it.unknown } == 0
                    && c.internalEdges(d).none { it.on }
            ) {
                maybeYieldXorPair(c, d)
            }
        }
    }
}
