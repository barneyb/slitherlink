package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

typealias Clue = Int

const val BLANK: Clue = -1
const val ZERO: Clue = 0
const val ONE: Clue = 1
const val TWO: Clue = 2
const val THREE: Clue = 3

data class Cell(
        private val p: Puzzle,
        val r: Int,
        val c: Int
) {

    init {
        if (r < 0 || r >= p.gridRows || c < 0 || c >= p.gridCols) {
            throw IllegalArgumentException("$this isn't on the board")
        }
        if (r % 2 == 0) {
            throw IllegalArgumentException("$this isn't a valid cell (even row)")
        }
        if (c % 2 == 0) {
            throw IllegalArgumentException("$this isn't a valid cell (even col)")
        }
    }

    override fun toString(): String {
        return StringBuilder("Cell(")
                .append((r - 1) / 2)
                .append(", ")
                .append((c - 1) / 2)
                .append(")")
                .toString()
    }

    val blank
        get() = clue == BLANK

    private fun index() = r * p.gridCols + c

    var clue
        get() = p.grid[index()]
        set(value) {
            if (!(value in ZERO..THREE)) {
                throw IllegalArgumentException("$value is not a valid clue (for $this)")
            }
            if (!blank) {
                throw IllegalStateException("$this is already set to ${this.clue}")
            }
            p.grid[index()] = value
        }

    val edges
        get() = listOf(
                p.edge(r - 1, c),
                p.edge(r, c - 1),
                p.edge(r + 1, c),
                p.edge(r, c + 1)
        )

}
