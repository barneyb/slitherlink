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

fun Moves.edge(edge: Edge, state: EdgeState) = this.edgeUnless(edge, state, state)

fun Moves.edgeIf(edge: Edge, state: EdgeState, check: EdgeState) = this.edgeIf(edge, state, { it == check })

fun Moves.edgeUnless(edge: Edge, state: EdgeState, check: EdgeState) = this.edgeIf(edge, state, { it != check })

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

fun Moves.edges(edges: Collection<Edge>, state: EdgeState) = this.edgesUnless(edges, state, state)

fun Moves.edgesIf(edges: Collection<Edge>, state: EdgeState, check: EdgeState) = this.edgesIf(edges, state, { it == check })

fun Moves.edgesUnless(edges: Collection<Edge>, state: EdgeState, check: EdgeState) = this.edgesIf(edges, state, { it != check })

private fun Moves.edgesIf(edges: Collection<Edge>, state: EdgeState, test: (EdgeState) -> Boolean): Moves {
    var ms = this
    for (e in edges) {
        ms = ms.edgeIf(e, state, test)
    }
    return ms
}
