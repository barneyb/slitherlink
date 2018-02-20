package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.EdgeState
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.io.KrazyDadSource
import org.junit.Test

import static org.junit.Assert.assertEquals

/**
 *
 * @author bboisvert
 */
class ReachClueOneShortOfSatisfiedTest {

    @Test
    void three() {
        def source = new KrazyDadSource(4, 4,
            "...." +
            "..3." +
            "...." +
            "...."
        )
        def p = source.load()
        p.humanEdgeCoord(0, 1, Dir.SOUTH).state = EdgeState.ON
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 1, Dir.EAST), EdgeState.OFF),
            new MoveImpl(p.humanEdgeCoord(1, 2, Dir.EAST), EdgeState.ON),
            new MoveImpl(p.humanEdgeCoord(1, 2, Dir.SOUTH), EdgeState.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

    @Test
    void threeOnEdgeExternal() {
        def source = new KrazyDadSource(4, 4,
            "..3." +
            "...." +
            "...." +
            "...."
        )
        def p = source.load()
        p.humanEdgeCoord(0, 1, Dir.NORTH).state = EdgeState.ON
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.EAST), EdgeState.ON),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.SOUTH), EdgeState.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

    @Test
    void threeOnEdgeInternal() {
        def source = new KrazyDadSource(4, 4,
            "..3." +
            "...." +
            "...." +
            "...."
        )
        def p = source.load()
        p.humanEdgeCoord(1, 1, Dir.EAST).state = EdgeState.ON
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 1, Dir.SOUTH), EdgeState.OFF),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.NORTH), EdgeState.ON),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.EAST), EdgeState.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

    @Test
    void two() {
        def source = new KrazyDadSource(4, 4,
            "...." +
            "..2." +
            "...." +
            "...."
        )
        def p = source.load()
        p.humanEdgeCoord(0, 1, Dir.SOUTH).state = EdgeState.ON
        p.humanEdgeCoord(1, 2, Dir.SOUTH).state = EdgeState.OFF
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 1, Dir.EAST), EdgeState.OFF),
            new MoveImpl(p.humanEdgeCoord(1, 2, Dir.EAST), EdgeState.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

}
