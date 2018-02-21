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
        return StringBuilder("Dot(")
                .append(r / 2)
                .append(", ")
                .append(c / 2)
                .append(")")
                .toString()
    }

    val northRow get() = r == 0
    val southRow get() = r == p.gridRows - 1
    val westCol get() = c == 0
    val eastCol get() = c == p.gridCols - 1

    val edges: List<Edge>
        get() {
            val es = mutableListOf<Edge>()
            if (!northRow) {
                es.add(p.edge(r - 1, c))
            }
            if (!westCol) {
                es.add(p.edge(r, c - 1))
            }
            if (!southRow) {
                es.add(p.edge(r + 1, c))
            }
            if (!eastCol) {
                es.add(p.edge(r, c + 1))
            }
            return es
        }

    fun edges(state: EdgeState)
            = edges.filter { it.state == state }

    fun otherEnd(e: Edge) =
            if (r == e.r) { // horiz edge
                p.dot(r, if (e.c > c) c + 2 else c - 2)
            } else { // vert edge
                p.dot(if (e.r > r) r + 2 else r - 2, c)
            }

    fun adjacent(d: Dot)
            = (r == d.r && Math.abs(c - d.c) == 2) || (Math.abs(r - d.r) == 2 && c == d.c)

    fun edgeTo(d: Dot): Edge {
        if (!adjacent(d)) {
            throw IllegalArgumentException("There isn't an edge between non-adjacent $this and $d")
        }
        return p.edge(
                r + (d.r - r) / 2,
                c + (d.c - c) / 2
        )
    }

}
