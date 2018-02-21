package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Clue
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.Move
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StatelessStrategy
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO

class AdjacentThrees : StatelessStrategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
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

class TwoInCorner : StatelessStrategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        val edges = mutableListOf<Edge>()
        var c = p.northWestCorner()
        if (c.clue == TWO) {
            edges.add(c.cellToSouth.edgeToWest)
            edges.add(c.cellToEast.edgeToNorth)
        }
        c = p.northEastCorner()
        if (c.clue == TWO) {
            edges.add(c.cellToSouth.edgeToEast)
            edges.add(c.cellToWest.edgeToNorth)
        }
        c = p.southEastCorner()
        if (c.clue == TWO) {
            edges.add(c.cellToNorth.edgeToEast)
            edges.add(c.cellToWest.edgeToSouth)
        }
        c = p.southWestCorner()
        if (c.clue == TWO) {
            edges.add(c.cellToNorth.edgeToWest)
            edges.add(c.cellToEast.edgeToSouth)
        }
        return edges(edges, ON)
    }
}

class ThreeInCorner : StatelessStrategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        return edges(externalCornerEdges(p, THREE), ON)
    }
}

class OneInCorner : StatelessStrategy {
    override fun nextMoves(p: Puzzle): List<Move>? {
        return edges(externalCornerEdges(p, ON), OFF)
    }
}

private fun externalCornerEdges(p: Puzzle, clue: Clue): List<Edge> {
    val edges = mutableListOf<Edge>()
    var c = p.northWestCorner()
    if (c.clue == clue) {
        edges.add(c.edgeToWest)
        edges.add(c.edgeToNorth)
    }
    c = p.northEastCorner()
    if (c.clue == clue) {
        edges.add(c.edgeToEast)
        edges.add(c.edgeToNorth)
    }
    c = p.southEastCorner()
    if (c.clue == clue) {
        edges.add(c.edgeToEast)
        edges.add(c.edgeToSouth)
    }
    c = p.southWestCorner()
    if (c.clue == clue) {
        edges.add(c.edgeToWest)
        edges.add(c.edgeToSouth)
    }
    return edges
}
