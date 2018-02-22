package com.barneyb.slitherlink

/**
 * I accept a Puzzle to attempt to make a some moves on it. If moves are
 * available they will be returned, otherwise null. Note that it's up to the
 * strategy to decide how best to batch moves. It's often possible to identify
 * several moves with a single check, so they should be returned as batch to
 * avoid having to reexecute the check on subsequent invocations.
 */
typealias Strategy = (Puzzle) -> Moves

data class Move(
        val edge: Edge,
        val state: EdgeState
) {

    override fun toString(): String {
        return StringBuilder("Move(")
                .append(edge)
                .append(": ")
                .append(state)
                .append(")")
                .toString()
    }

    val off get() = state == OFF
    val on get() = state == ON
}

typealias Moves = MutableSet<Move>?
