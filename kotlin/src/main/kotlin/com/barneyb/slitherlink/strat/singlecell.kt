package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO
import com.barneyb.slitherlink.UNKNOWN
import kotlin.coroutines.experimental.buildSequence

/**
 * If a clue has on edges equal to itself, any unknown edges must be off.
 */
fun clueSatisfied(p: Puzzle) = buildSequence {
    for (c in p.clueCells()) {
        val edges = c.edges
        if (edges.count { it.on } == c.clue) {
            setUnknownTo(edges, OFF)
        }
    }
}

/**
 * If a clue's on and unknown edges add up to itself, all the unknowns must
 * be on.
 */
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

/**
 * If a cell that needs one less than all remaining has a corner with one
 * external edge on and both internal edges unknown, it must turn in (it can't
 * bounce).
 */
fun reachOneShortOfSatisfiedMustStay(p: Puzzle) = buildSequence {
    for (cell in p.clueCells()) {
        if (cell.edges(OFF).size == 3 - cell.clue) {
            // one short of satisfied
            for (dot in cell.dots) {
                if (cell.internalEdges(dot).all { it.unknown }) {
                    // this cell can receive
                    var c = cell
                    var d = dot
                    while (true) {
                        if (c.externalEdges(d).count { it.on } == 1) {
                            // one line to it
                            setUnknownTo(c.externalEdges(d), OFF)
                            setUnknownTo(cell.opposedEdges(dot), ON)
                        }
                        if (!d.hasOpposedCell(c)) break
                        c = d.opposedCell(c)
                        if (c.clue != TWO) break
                        d = c.opposedDot(d)
                    }
                }
            }
        }
    }
}

/**
 * If a two's opposite corners each have an on edge to them, they have to
 * turn into the two (they can't bounce).
 */
fun pinchedTwoMustStay(p: Puzzle) = buildSequence {
    for (c in p.clueCells(TWO)) {
        for ((a, b) in mapOf(
            c.dotToNorthWest to c.dotToSouthEast,
            c.dotToNorthEast to c.dotToSouthWest
        )) {
            val aExternalEdges = c.externalEdges(a)
            val bExternalEdges = c.externalEdges(b)
            if (aExternalEdges.count { it.on } == 1 && bExternalEdges.count { it.on } == 1) {
                setUnknownTo(aExternalEdges, OFF)
                setUnknownTo(bExternalEdges, OFF)
            }
        }
    }
}

/**
 * I test each direction a three clue can face, and if any lead to a
 * contradiction, mark that edge as [ON].
 */
fun testThree(p: Puzzle) = buildSequence {
    for (c in p.clueCells(THREE)) {
        val unknowns = c.edges(UNKNOWN)
        for (u in unknowns) {
            if (createsContradiction(p, (unknowns - u).map { Move(it, ON) })) {
                yield(Move(u, ON))
                return@buildSequence
            }
        }
    }
}