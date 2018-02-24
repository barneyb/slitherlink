package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.findOtherEndHelper

private const val DOT = '·'
private const val VERT = '│'
private const val HORIZ = '─'
private const val TICK = '×'

data class Puzzle(
    val humanRows: Int,
    val humanCols: Int
) {
    val gridRows = humanRows * 2 + 1
    val gridCols = humanCols * 2 + 1
    internal val grid: IntArray

    init {
        assert(BLANK == UNKNOWN)
        grid = IntArray(gridRows * gridCols).apply { fill(BLANK) }
    }

    // grid accessors

    fun cell(r: Int, c: Int) = Cell(this, r, c)

    fun edge(r: Int, c: Int) = Edge(this, r, c)

    fun dot(r: Int, c: Int) = Dot(this, r, c)

    fun northWestCorner() = cell(1, 1)
    fun northEastCorner() = cell(1, gridCols - 2)
    fun southWestCorner() = cell(gridRows - 2, 1)
    fun southEastCorner() = cell(gridRows - 2, gridCols - 2)

    val cells: List<Cell>
        get() {
            val ds = mutableListOf<Cell>()
            for (r in 1 until gridRows step 2) {
                for (c in 1 until gridCols step 2) {
                    ds.add(cell(r, c))
                }
            }
            return ds
        }

    fun clueCells() = cells.filter {
        !it.blank
    }

    fun clueCells(c: Clue) = cells.filter {
        it.clue == c
    }

    fun dots(): List<Dot> {
        val ds = mutableListOf<Dot>()
        for (r in 0 until gridRows step 2) {
            for (c in 0 until gridCols step 2) {
                ds.add(dot(r, c))
            }
        }
        return ds
    }

    // human accessors

    fun humanCell(humanRow: Int, humanCol: Int) = cell(
        humanRow * 2 + 1,
        humanCol * 2 + 1
    )

    // end human accessors

    private var _solved = false

    val solved
        get(): Boolean {
            if (_solved) return true

            // unsatisfied clue?
            for (c in clueCells()) {
                val onCount = c.edges.count { it.on }
                if (onCount != c.clue) {
                    return false
                }
            }

            // branching?
            for (d in dots()) {
                val onCount = d.edges.count { it.on }
                if (onCount != 0 && onCount != 2) {
                    return false
                }
            }

            // multiple segments?
            var edge: Edge? = null
            var onCount = 0
            for (r in 0 until gridRows) {
                for (c in (1 - r % 2) until gridCols step 2) {
                    val e = edge(r, c)
                    if (e.on) {
                        onCount += 1
                        if (edge == null) {
                            edge = e
                        }
                    }
                }
            }
            if (edge == null) return false // zero edges set
            val (curr, prev) = edge.dots
            val stats = findOtherEndHelper(curr, prev, prev)
            if (stats.otherEnd != prev) {
                throw IllegalStateException("the other end of $curr's edge isn't $prev?!")
            }
            val length = stats.edgeCount + 1 // for the "base" edge
            if (onCount != length) {
                throw IllegalStateException("prematurely closed loop (w/ $edge)!")
            }

            _solved = true
            return true
        }

    override fun toString(): String {
        val sb = StringBuilder()
        for (r in 0 until gridRows) {
            if (r % 2 == 0) {
                // dot row
                for (c in 0 until gridCols) {
                    if (c % 2 == 0) {
                        // dot
                        sb.append(DOT)
                    } else {
                        // edge
                        val v = edge(r, c).state
                        if (v == ON) {
                            sb.append(HORIZ).append(HORIZ).append(HORIZ)
                        } else if (v == OFF) {
                            sb.append(' ').append(TICK).append(' ')
                        } else {
                            sb.append(' ').append(' ').append(' ')
                        }
                    }
                }
            } else {
                // cell row
                for (c in 0 until gridCols) {
                    if (c % 2 == 0) {
                        // edge
                        val v = edge(r, c).state
                        if (v == ON) {
                            sb.append(VERT)
                        } else if (v == OFF) {
                            sb.append(TICK)
                        } else {
                            sb.append(' ')
                        }
                    } else {
                        // cell
                        val cell = cell(r, c).clue
                        sb.append(' ')
                        if (cell == BLANK) {
                            sb.append(' ')
                        } else {
                            sb.append(cell)
                        }
                        sb.append(' ')
                    }
                }
            }
            sb.append('\n')
        }
        return sb.toString()
    }

}

