package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

typealias EdgeState = Int

const val UNKNOWN: EdgeState = -1
const val OFF: EdgeState = 0
const val ON: EdgeState = 1

data class Edge(
        private val p: Puzzle,
        val r: Int,
        val c: Int
) {

    init {
        if (r < 0 || r >= p.gridRows || c < 0 || c > p.gridCols) {
            throw IllegalStateException("$this isn't on the board")
        }
        if (r % 2 == c % 2) {
            throw IllegalArgumentException("$this isn't a valid edge (same parity)")
        }
    }

    override fun toString(): String {
        return StringBuilder("Edge(")
                .append(r / 2)
                .append(", ")
                .append(c / 2)
                .append(", ")
                .append(if (r % 2 == 0) NORTH else WEST)
                .append(")")
                .toString()
    }

    private fun index() = r * p.gridCols + c

    var state: EdgeState
        get() = p.grid[index()]
        set(value) {
            val curr = this.state
            if (curr == value) return
            if (curr != UNKNOWN) {
                throw IllegalArgumentException("$this is $curr, you can't set it $value")
            }
            p.grid[index()] = value
        }

}
