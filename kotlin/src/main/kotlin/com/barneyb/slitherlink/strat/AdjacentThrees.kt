package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StatelessStrategy
import com.barneyb.slitherlink.THREE

/**
 *
 *
 * @author barneyb
 */
class AdjacentThrees : StatelessStrategy {
    override fun nextMoves(p: Puzzle): Collection<Move>? {
        val edges = mutableListOf<Edge>()
        val ticks = mutableListOf<Edge>()
        for (a in p.clueCells(THREE)) {
            if (! a.eastCol) {
                val b = a.cellToEast
                if (b.clue == THREE) {
                    edges.add(a.edgeToWest)
                    edges.add(a.edgeToEast)
                    edges.add(b.edgeToEast)
                    if (! a.northRow) {
                        ticks.add(a.cellToNorth.edgeToEast)
                    }
                    if (! a.southRow) {
                        ticks.add(a.cellToSouth.edgeToEast)
                    }
                }
            }
            if (! a.southRow) {
                val b = a.cellToSouth
                if (b.clue == THREE) {
                    edges.add(a.edgeToNorth)
                    edges.add(a.edgeToSouth)
                    edges.add(b.edgeToSouth)
                    if (! a.westCol) {
                        ticks.add(a.cellToWest.edgeToSouth)
                    }
                    if (! a.eastCol) {
                        ticks.add(a.cellToEast.edgeToSouth)
                    }
                }
            }
        }
        return edges(edges(edges, ON), ticks, OFF)
    }
}
