package com.barneyb.slitherlink

        /**
         * I accept a [Puzzle] to attempt to make a some [Move]s on it. If moves are
         * available they will be returned, otherwise null. Note that it's up to the
         * strategy to decide how best to batch moves. It's often possible to identify
         * several moves with a single check, so they should be returned as batch to
         * avoid having to reexecute the check on subsequent invocations.
         */
typealias Strategy = (Puzzle) -> Sequence<Move>

typealias Evidence = Map<String, Set<PuzzleItem>>

data class Move(
    val edge: Edge,
    val state: EdgeState,
    val evidence: Evidence = emptyMap()
) {

    override fun toString(): String {
        return StringBuilder("Move(")
            .append(edge)
            .append(": ")
            .append(humanState(state))
            .append(")")
            .toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Move) return false

        if (edge != other.edge) return false
        if (state != other.state) return false

        return true
    }

    override fun hashCode(): Int {
        var result = edge.hashCode()
        result = 31 * result + state
        return result
    }

    val off get() = state == OFF
    val on get() = state == ON

    val evidenceBased get() = evidence.isNotEmpty()
}

class IllegalMoveException(
    val move: Move,
    msg: String
) : IllegalArgumentException("$move would $msg")
