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
                .append(if (horizontal) NORTH else WEST)
                .append(")")
                .toString()
    }

    val horizontal get() = r % 2 == 0
    val vertical get() = ! horizontal

    val northRow get() = r == 0
    val southRow get() = r == p.gridRows - 1
    val westCol get() = c == 0
    val eastCol get() = c == p.gridCols - 1

    val cells: List<Cell>
    get() {
        val cs = mutableListOf<Cell>()
        if (horizontal) {
            if (!northRow) cs.add(p.cell(r - 1, c))
            if (!southRow) cs.add(p.cell(r + 1, c))
        } else {
            if (!westCol) cs.add(p.cell(r, c - 1))
            if (!eastCol) cs.add(p.cell(r, c + 1))
        }
        return cs
    }


    val dots
        get() =
            if (horizontal)
                listOf(
                        p.dot(r, c - 1),
                        p.dot(r, c + 1)
                )
            else
                listOf(
                        p.dot(r - 1, c),
                        p.dot(r + 1, c)
                )

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
