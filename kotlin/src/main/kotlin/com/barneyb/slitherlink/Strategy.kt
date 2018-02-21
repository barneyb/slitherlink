package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */

interface Strategy {

    val name: String

    /**
     * I accept a Puzzle to attempt to make a some moves on it. If moves are
     * available they will be returned, otherwise null.
     *
     * @param p The Puzzle to attempt to move against
     * @return moves to make, or <code>null</code> if no move can be made
     */
    fun nextMoves(p: Puzzle): Collection<Move>?

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
    override fun nextMoves(p: Puzzle): Collection<Move>?

}

data class Move(
    val edge: Edge,
    val state: EdgeState
)
