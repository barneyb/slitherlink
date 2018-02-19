package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
interface MultiMoveStrategy extends Strategy {

    /**
     * I accept a Puzzle to attempt to make a some moves on it. If moves are
     * available they will be returned, otherwise null.
     *
     * @param p The Puzzle to attempt to move against
     * @return moves to make, or <code>null</code> if no move can be made
     */
    List<Move> nextMoves(Puzzle p)

}
