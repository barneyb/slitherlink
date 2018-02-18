package com.barneyb.slitherlink

import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class PuzzleTest {

    private Puzzle initial() {
        new Puzzle(2, 2)
            .cell(0, 1, 3)
            .cell(1, 0, 1)
            .cell(1, 1, 1)
    }

    @Test
    void simpleConstructAndPrint() {
        def p = initial()
            .edge(0, 0, Dir.NORTH, EdgeState.ON)
            .edge(0, 0, Dir.WEST, EdgeState.ON)
            .edge(0, 0, Dir.SOUTH, EdgeState.ON)
            .edge(0, 1, Dir.NORTH, EdgeState.ON)
            .edge(0, 1, Dir.EAST, EdgeState.ON)
            .edge(0, 1, Dir.SOUTH, EdgeState.ON)
            .edge(2, 0, Dir.NORTH, EdgeState.OFF) // silly
            .edge(1, 0, Dir.WEST, EdgeState.OFF)
            .edge(1, 1, Dir.WEST, EdgeState.OFF) // silly
        println p
        // this looks sorta janky.
        assert "·───·───·\n" +
               "│     3 │\n" +
               "·───·───·\n" +
               "× 1 × 1  \n" +
               "· × ·   ·\n" == p.toString()
    }

    @Test
    void move() {
        def p = new Puzzle(2, 2)
        assert EdgeState.UNKNOWN == p.edge(0, 0, Dir.NORTH)
        p.move(new MoveImpl(new EdgeCoord(0, 0, Dir.NORTH), EdgeState.ON))
        assert EdgeState.ON == p.edge(0, 0, Dir.NORTH)
    }

    @Test
    void solveIt() {
        [
//            initial(),
            // KD_Slitherlink_7x7_d0_V1-B1-P1
//            new KrazyDadLoader(7, 7, "222331...02.3........33..33...2.2..3.2.22.3.2..3.").load(),
            // KD_Slitherlink_20x20_d0_V1-B1-P1
//            new KrazyDadLoader(20, 20, "12333.21..2...2.21.3....2..11.21.......2.......2.23222..21.2..20..2..1...122.2.23.3223..0.3.23212..33..323.33.1...1...03.....2.0.13.3.232.2......2.333..2.......22....2....23....2233..3.2.2..2.3....2..2..2112.211132...31312..3..231..31..2..333.23..2.2.1...2..23................221...333..2211..22..22310212...3.32..21...3.1..2222311...1.2..332222..03.3.2...13.331.2....21.2.....32.2..32.232..2222223.1").load(),
            // KD_Slitherlink_7x7_d1_V1-B1-P1
            new KrazyDadLoader(7, 7, "33.3222.12.2...2...12202.2....3...3.1.12.113....2").load(),
        ].each { p ->
            def strats = [
                new OneInCorner(),
                new TwoInCorner(),
                new ThreeInCorner(),
                new KittyCornerThrees(),
                new AdjacentThrees(),

                new NoBranching(),
                new ClueSatisfied(),
                new NeedAllRemaining(),
                new ReachThree(),

                new SingleIngress(),
                new SingleEgress(),

                new SingleLoop(),
            ]
            int moveCount = 0
            try {
                boolean moved = true
                while (moved) {
                    moved = false;
                    for (def itr = strats.iterator(); itr.hasNext();) {
                        def s = itr.next()
                        def m = s.nextMove(p)
                        if (m != null) {
//                            println "$p\n- ${s.getClass().simpleName} ----------------"
//                            println s.getClass().simpleName + " : " + m
                            p.move(m)
                            moved = true
                            moveCount += 1
                            break
                        }
                        if (s instanceof ClueOnly) {
                            itr.remove()
                        }
                    }
                }
                assert p.solved
            } finally {
                println p
                println("done ($moveCount)!")
            }
        }
    }

}
