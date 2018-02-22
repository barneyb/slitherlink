package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

interface Strategy {

    val name
        get() = this.javaClass.simpleName!!

    /**
     * I accept a Puzzle to attempt to make a some moves on it. If moves are
     * available they will be returned, otherwise null.
     *
     * @param p The Puzzle to attempt to move against
     * @return moves to make, or <code>null</code> if no move can be made
     */
    fun nextMoves(p: Puzzle): Moves

}

/**
 * I mark a Strategy to indicate it doesn't rely on the state of the
 * board. I.e., it only relies on the puzzle itself (dimensions and clues, but
 * not edge state).
 */
interface StatelessStrategy : Strategy {

    /**
     * I accept a Puzzle and return all moves that can be made against it, or
     * null if no moves are available. StatelessStrategy implementations MUST
     * return all possible moves at once.
     *
     * @param p The Puzzle to attempt to move against
     * @return all moves to make, or <code>null</code> if no move can be made
     */
    override fun nextMoves(p: Puzzle): Moves

}

data class Move(
        val edge: Edge,
        val state: EdgeState
)

typealias Moves = MutableSet<Move>?

fun Moves.edge(edge: Edge, state: EdgeState)
        = this.edgeUnless(edge, state, state)

fun Moves.edgeIf(edge: Edge, state: EdgeState, check: EdgeState)
        = this.edgeIf(edge, state, { it == check })

fun Moves.edgeUnless(edge: Edge, state: EdgeState, check: EdgeState)
        = this.edgeIf(edge, state, { it != check })

fun Moves.edgeIf(edge: Edge, state: EdgeState, test: (EdgeState) -> Boolean): Moves {
    var ms = this
    if (test(edge.state)) {
        if (ms == null) {
            ms = mutableSetOf()
        }
        ms.add(Move(edge, state))
    }
    return ms
}

fun Moves.edges(edges: Collection<Edge>, state: EdgeState)
        = this.edgesUnless(edges, state, state)

fun Moves.edgesIf(edges: Collection<Edge>, state: EdgeState, check: EdgeState)
        = this.edgesIf(edges, state, { it == check })

fun Moves.edgesUnless(edges: Collection<Edge>, state: EdgeState, check: EdgeState)
        = this.edgesIf(edges, state, { it != check })

private fun Moves.edgesIf(edges: Collection<Edge>, state: EdgeState, test: (EdgeState) -> Boolean): Moves {
    var ms = this
    for (e in edges) {
        ms = ms.edgeIf(e, state, test)
    }
    return ms
}
