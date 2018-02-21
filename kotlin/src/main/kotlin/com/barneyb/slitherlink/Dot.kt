package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
data class Dot(
        private val p: Puzzle,
        val r: Int,
        val c: Int
) {

    init {
        if (r < 0 || r >= p.gridRows || c < 0 || c >= p.gridCols) {
            throw IllegalStateException("$this isn't on the board")
        }
        if (r % 2 == 1) {
            throw IllegalArgumentException("$this isn't a valid dot (odd row)")
        }
        if (c % 2 == 1) {
            throw IllegalArgumentException("$this isn't a valid dot (odd col)")
        }
    }

    override fun toString(): String {
        return StringBuilder("Cell(")
                .append(r / 2)
                .append(", ")
                .append(c / 2)
                .append(")")
                .toString()
    }

    val edges
        get() = listOf(
                p.edge(r - 1, c),
                p.edge(r, c - 1),
                p.edge(r + 1, c),
                p.edge(r, c + 1)
        )

}
