package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.OFF
import com.barneyb.slitherlink.ON
import com.barneyb.slitherlink.ONE
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.THREE
import com.barneyb.slitherlink.TWO
import kotlin.coroutines.experimental.buildSequence

fun adjacentThrees(p: Puzzle) = buildSequence {
    for (a in p.clueCells(THREE)) {
        if (!a.eastCol) {
            val b = a.cellToEast
            if (b.clue == THREE) {
                setUnknownTo(a.edgeToWest, ON)
                setUnknownTo(a.edgeToEast, ON)
                setUnknownTo(b.edgeToEast, ON)
                if (!a.northRow) {
                    setUnknownTo(a.cellToNorth.edgeToEast, OFF)
                }
                if (!a.southRow) {
                    setUnknownTo(a.cellToSouth.edgeToEast, OFF)
                }
            }
        }
        if (!a.southRow) {
            val b = a.cellToSouth
            if (b.clue == THREE) {
                setUnknownTo(a.edgeToNorth, ON)
                setUnknownTo(a.edgeToSouth, ON)
                setUnknownTo(b.edgeToSouth, ON)
                if (!a.westCol) {
                    setUnknownTo(a.cellToWest.edgeToSouth, OFF)
                }
                if (!a.eastCol) {
                    setUnknownTo(a.cellToEast.edgeToSouth, OFF)
                }
            }
        }
    }
}

fun kittyCornerThrees(p: Puzzle) = buildSequence {
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
                setUnknownTo(a.edgeToSouth, ON)
                setUnknownTo(a.edgeToWest, ON)
                setUnknownTo(b.edgeToNorth, ON)
                setUnknownTo(b.edgeToEast, ON)
            }
        }
        if (!a.southRow) {
            var b = a.cellToSouth.cellToEast
            while (b.clue == TWO && !b.southRow && !b.eastCol) {
                b = b.cellToSouth.cellToEast
            }
            if (b.clue == THREE) {
                setUnknownTo(a.edgeToNorth, ON)
                setUnknownTo(a.edgeToWest, ON)
                setUnknownTo(b.edgeToSouth, ON)
                setUnknownTo(b.edgeToEast, ON)
            }
        }
    }
}

fun adjacentOnesOnEdge(p: Puzzle) = buildSequence {
    for (a in p.clueCells(ONE)) {
        if ((a.northRow || a.southRow) && !a.eastCol) {
            if (a.cellToEast.clue == ONE) {
                setUnknownTo(a.edgeToEast, OFF)
            }
        }
        if ((a.westCol || a.eastCol) && !a.southRow) {
            if (a.cellToSouth.clue == ONE) {
                setUnknownTo(a.edgeToSouth, OFF)
            }
        }
    }
}
