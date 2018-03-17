package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Clue
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.ONE
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

/**
 * If a three has an edge pair, they must on
 */
fun threeWithEdgePair(p: Puzzle) = edgePairs(p, THREE, ON)

/**
 * If a one has an edge pair, they must be off
 */
fun oneWithEdgePair(p: Puzzle) = edgePairs(p, ONE, OFF)

private fun edgePairs(p: Puzzle, clue: Clue, state: EdgeState) = buildSequence {
    for (pair in allAndPairs(p, clue)) {
        setTo(pair.edges, state)
    }
}

/**
 * If a two has an edge pair, the opposite corners must have exactly one
 * edge coming out.
 */
fun twoWithEdgePairHasWhiskers(p: Puzzle) = buildSequence {
    for (pair in allAndPairs(p, TWO)) {
        for (d in pair.ends) {
            val edges = pair.cell.externalEdges(d)
            if (edges.count { it.unknown } == 1) {
                setUnknownTo(edges, if (edges.any { it.on }) OFF else ON)
            }
        }
    }
}

/**
 * If a two has an edge pair and only one edge at the other corner is
 * unknown, it has to be the same as the other edge.
 *
 */
fun twoWithEdgePairRepelsAtOtherCorner(p: Puzzle) = buildSequence {
    for (pair in allAndPairs(p, TWO)) {
        val edges = pair.cell.externalEdges(pair.opposedDot)
        if (edges.count { it.on } == 1) {
            setUnknownTo(edges, ON)
        }
        if (edges.count { it.off } == 1) {
            setUnknownTo(edges, OFF)
        }
    }
}

/**
 * If a two has an edge pair, no adjacent constraints, and one of the pairs is
 * unreachable from the outside, the two must be satisfied on that corner and
 * pull the external edges of the opposite corner.
 */
fun twoWithEdgePairAndNoConstraintsPullsAtCorner(p: Puzzle) = buildSequence {
    for (pair in allAndPairs(p, TWO)) {
        if (pair.externalEdges.all { it.off }) {
            val c = pair.cell
            if (!c.northRow && !c.cellToNorth.blank) continue
            if (!c.southRow && !c.cellToSouth.blank) continue
            if (!c.eastCol && !c.cellToEast.blank) continue
            if (!c.westCol && !c.cellToWest.blank) continue
            setUnknownTo(pair.edges, ON)
            setUnknownTo(pair.cell.externalEdges(pair.opposedDot), ON)
        }
    }
}

internal fun allAndPairs(p: Puzzle) =
    propagateAlongTwos(lastTwoUnknownEdgesOfDot(p))

internal fun allAndPairs(p: Puzzle, clue: EdgeState) = allAndPairs(p).filter {
    it.cell.clue == clue
}

private fun lastTwoUnknownEdgesOfDot(p: Puzzle) = buildSequence {
    for (d in p.dots()) {
        val unknown = d.edges(UNKNOWN)
        val on = d.edges(ON)
        if (unknown.size == 2 && on.isEmpty()) {
            // one away from satisfied, and two unknowns
            val cells = unknown.first().cells.intersect(unknown.last().cells)
            if (cells.size == 1) {
                // unknowns make a corner!
                val c = cells.first()
                maybeYieldEdgePair(c, d)
                if (d.hasOpposedCell(c)) {
                    val oc = d.opposedCell(c)
                    if (oc.clue == 2) {
                        maybeYieldEdgePair(oc, d)
                    }
                }
            }
        }
    }
}
