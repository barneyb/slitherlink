package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Clue
import com.barneyb.slitherlink.Edge
import com.barneyb.slitherlink.Moves
import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.ONE
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.StatelessStrategy
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO
import com.barneyb.slitherlink.edges

class AdjacentThrees : StatelessStrategy {
    override fun nextMoves(p: Puzzle): Moves {
        val edges = mutableListOf<Edge>()
        val ticks = mutableListOf<Edge>()
        for (a in p.clueCells(THREE)) {
            if (!a.eastCol) {
                val b = a.cellToEast
                if (b.clue == THREE) {
                    edges.add(a.edgeToWest)
                    edges.add(a.edgeToEast)
                    edges.add(b.edgeToEast)
                    if (!a.northRow) {
                        ticks.add(a.cellToNorth.edgeToEast)
                    }
                    if (!a.southRow) {
                        ticks.add(a.cellToSouth.edgeToEast)
                    }
                }
            }
            if (!a.southRow) {
                val b = a.cellToSouth
                if (b.clue == THREE) {
                    edges.add(a.edgeToNorth)
                    edges.add(a.edgeToSouth)
                    edges.add(b.edgeToSouth)
                    if (!a.westCol) {
                        ticks.add(a.cellToWest.edgeToSouth)
                    }
                    if (!a.eastCol) {
                        ticks.add(a.cellToEast.edgeToSouth)
                    }
                }
            }
        }
        return edges(edges, ON)
                .edges(ticks, OFF)
    }
}

class KittyCornerThrees : StatelessStrategy {
    override fun nextMoves(p: Puzzle): Moves {
        val edges = mutableListOf<Edge>()
        for (a in p.clueCells(THREE)) {
            if (a.eastCol) {
                continue
            }
            if (!a.northRow) {
                var b = a.cellToNorth.cellToEast
                while (b.clue == TWO && !b.northRow && !b.eastCol) {
                    b = b.cellToNorth.cellToEast
                }
                if (b.clue == THREE) {
                    edges.add(a.edgeToSouth)
                    edges.add(a.edgeToWest)
                    edges.add(b.edgeToNorth)
                    edges.add(b.edgeToEast)
                }
            }
            if (!a.southRow) {
                var b = a.cellToSouth.cellToEast
                while (b.clue == TWO && !b.southRow && !b.eastCol) {
                    b = b.cellToSouth.cellToEast
                }
                if (b.clue == THREE) {
                    edges.add(a.edgeToNorth)
                    edges.add(a.edgeToWest)
                    edges.add(b.edgeToSouth)
                    edges.add(b.edgeToEast)
                }
            }
        }
        return edges(edges, ON)
    }
}

class AdjacentOnesOnEdge : StatelessStrategy {
    override fun nextMoves(p: Puzzle): Moves {
        val edges = mutableListOf<Edge>()
        for (a in p.clueCells(ONE)) {
            if ((a.northRow || a.southRow) && ! a.eastCol) {
                if (a.cellToEast.clue == ONE) {
                    edges.add(a.edgeToEast)
                }
            }
            if ((a.westCol || a.eastCol) && ! a.southRow) {
                if (a.cellToSouth.clue == ONE) {
                    edges.add(a.edgeToSouth)
                }
            }
        }
        return edges(edges, OFF)
    }

}

class TwoInCorner : StatelessStrategy {
    override fun nextMoves(p: Puzzle): Moves {
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
    override fun nextMoves(p: Puzzle): Moves {
        return edges(externalCornerEdges(p, THREE), ON)
    }
}

class OneInCorner : StatelessStrategy {
    override fun nextMoves(p: Puzzle): Moves {
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
