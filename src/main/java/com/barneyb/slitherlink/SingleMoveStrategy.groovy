package com.barneyb.slitherlink
/**
 *
 *
 * @author barneyb
 */
interface SingleMoveStrategy {

    /**
     * I accept a Puzzle to attempt to make a single move on it. If a move is
     * available it will be returned, otherwise null.
     *
     * @param p The Puzzle to attempt a move against
     * @return a move to make, or <code>null</code> if no move can be made
     */
    Move nextMove(Puzzle p)

    // List<Move> nextMoves(Puzzle p)
}
