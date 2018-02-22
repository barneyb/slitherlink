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

    val blank get() = clue == BLANK

    val northRow get() = r == 1
    val southRow get() = r == p.gridRows - 2
    val westCol get() = c == 1
    val eastCol get() = c == p.gridCols - 2

    val cellToNorth get() = p.cell(r - 2, c)
    val cellToSouth get() = p.cell(r + 2, c)
    val cellToWest get() = p.cell(r, c - 2)
    val cellToEast get() = p.cell(r, c + 2)

    val edgeToNorth get() = p.edge(r - 1, c)
    val edgeToSouth get() = p.edge(r + 1, c)
    val edgeToWest get() = p.edge(r, c - 1)
    val edgeToEast get() = p.edge(r, c + 1)

    val dotToNorthWest get() = p.dot(r - 1, c - 1)
    val dotToNorthEast get() = p.dot(r - 1, c + 1)
    val dotToSouthWest get() = p.dot(r + 1, c - 1)
    val dotToSouthEast get() = p.dot(r + 1, c + 1)

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
                edgeToNorth,
                edgeToWest,
                edgeToSouth,
                edgeToEast
        )

    fun edges(state: EdgeState)
            = edges.filter { it.state == state }

    fun internalEdges(d: Dot) = edges.intersect(d.edges)

    fun externalEdges(d: Dot) = d.edges.minus(edges)

    fun opposedEdges(d: Dot) = edges.minus(d.edges)

    val dots
        get() = listOf(
                dotToNorthWest,
                dotToNorthEast,
                dotToSouthEast,
                dotToSouthWest
        )


}
