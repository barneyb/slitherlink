package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Moves
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.UNKNOWN
import com.barneyb.slitherlink.edges
import com.barneyb.slitherlink.edgesIf

fun clueSatisfied(p: Puzzle): Moves {
    var moves: Moves = null
    for (c in p.clueCells()) {
        val edges = c.edges
        if (edges.count { it.state == ON } == c.clue) {
            moves = moves.edgesIf(edges, OFF, UNKNOWN)
        }
    }
    return moves
}

fun needAllRemaining(p: Puzzle): Moves {
    var moves: Moves = null
    for (c in p.clueCells()) {
        val unknown = c.edges(UNKNOWN)
        if (unknown.isEmpty()) {
            continue
        }
        val onCount = c.edges(ON).size
        if (onCount + unknown.size == c.clue) {
            moves = moves.edges(unknown, ON)
        }
    }
    return moves
}

fun reachOneShortOfSatisfiedMustStay(p: Puzzle): Moves {
    var moves: Moves = null
    for (c in p.clueCells()) {
        if (c.edges(OFF).size == 3 - c.clue) {
            // one short of satisfied
            for (d in c.dots) {
                if (c.externalEdges(d).count { it.state == ON } == 1) {
                    // one line to it
                    if (c.internalEdges(d).all { it.state == UNKNOWN }) {
                        // this cell can will receive
                        moves = moves.edgesIf(c.externalEdges(d), OFF, UNKNOWN)
                        moves = moves.edgesIf(c.opposedEdges(d), ON, UNKNOWN)
                    }
                }
            }
        }
    }
    return moves
}
