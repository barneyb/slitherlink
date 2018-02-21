package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

const val BLANK = -1

data class Puzzle(
        val humanRows: Int,
        val humanCols: Int
) {
    val rows = humanRows * 2 + 1
    val cols = humanCols * 2 + 1
    internal val grid: Array<Int>

    init {
        grid = generateSequence { BLANK }.take(rows * cols).toList().toTypedArray()
    }

    fun humanCell(r: Int, c: Int): Cell {
        return Cell(this, r * 2 + 1, c * 2 + 1)
    }

}

data class Cell(
        private val p: Puzzle,
        val r: Int,
        val c: Int
) {

    init {
        if (r < 0 || r >= p.rows || c < 0 || c >= p.cols) {
            throw IllegalArgumentException("$this isn't on the board")
        }
        if (r % 2 == 0) {
            throw IllegalArgumentException("$this isn't a valid cell (even row)")
        }
        if (c % 2 == 0) {
            throw IllegalArgumentException("$this isn't a valid cell (even col)")
        }
    }

    private fun index() = r * p.cols + c

    var clue
        get() = p.grid[index()]
        set(value) {
            p.grid[index()] = value
        }

}
