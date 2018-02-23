package com.barneyb.slitherlink.strat

import com.barneyb.slitherlink.Dir
import com.barneyb.slitherlink.MoveImpl
import com.barneyb.slitherlink.Puzzle
import com.barneyb.slitherlink.io.KrazyDadSource
import org.junit.Test

import static org.junit.Assert.*

/**
 *
 * @author bboisvert
 */
class ReachClueOneShortOfSatisfiedTest {

    @Test
    void three() {
        //@formatter:off
        def source = new KrazyDadSource(
            "...." +
            "..3." +
            "...." +
            "...."
        )
        //@formatter:on
        def p = source.load()
        p.humanEdgeCoord(0, 1, Dir.SOUTH).state = Puzzle.ON
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 1, Dir.EAST), Puzzle.OFF),
            new MoveImpl(p.humanEdgeCoord(1, 2, Dir.EAST), Puzzle.ON),
            new MoveImpl(p.humanEdgeCoord(1, 2, Dir.SOUTH), Puzzle.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

    @Test
    void threeOnEdgeExternal() {
        //@formatter:off
        def source = new KrazyDadSource(
            "..3." +
            "...." +
            "...." +
            "...."
        )
        //@formatter:on
        def p = source.load()
        p.humanEdgeCoord(0, 1, Dir.NORTH).state = Puzzle.ON
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.EAST), Puzzle.ON),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.SOUTH), Puzzle.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

    @Test
    void threeOnEdgeInternal() {
        //@formatter:off
        def source = new KrazyDadSource(
            "..3." +
            "...." +
            "...." +
            "...."
        )
        //@formatter:on
        def p = source.load()
        p.humanEdgeCoord(1, 1, Dir.EAST).state = Puzzle.ON
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 1, Dir.SOUTH), Puzzle.OFF),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.NORTH), Puzzle.ON),
            new MoveImpl(p.humanEdgeCoord(0, 2, Dir.EAST), Puzzle.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

    @Test
    void two() {
        //@formatter:off
        def source = new KrazyDadSource(
            "...." +
            "..2." +
            "...." +
            "...."
        )
        //@formatter:on
        def p = source.load()
        p.humanEdgeCoord(0, 1, Dir.SOUTH).state = Puzzle.ON
        p.humanEdgeCoord(1, 2, Dir.SOUTH).state = Puzzle.OFF
        assertEquals([
            new MoveImpl(p.humanEdgeCoord(0, 1, Dir.EAST), Puzzle.OFF),
            new MoveImpl(p.humanEdgeCoord(1, 2, Dir.EAST), Puzzle.ON),
        ], new ReachClueOneShortOfSatisfied().nextMoves(p))
    }

}
