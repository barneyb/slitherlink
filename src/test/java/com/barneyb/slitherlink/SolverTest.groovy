package com.barneyb.slitherlink

import com.barneyb.slitherlink.io.KrazyDadSource
import org.junit.Ignore
import org.junit.Test
/**
 *
 * @author bboisvert
 */
class SolverTest {

    @Test
    void initial() {
        solve(PuzzleTest.initial())
    }

    @Test
    void easy7x7() {
        // KD_Slitherlink_7x7_d0_V1-B1-P1
        solve(new KrazyDadSource(7, 7, "222331...02.3........33..33...2.2..3.2.22.3.2..3."))
    }

    @Test
    void easy20x20() {
        // KD_Slitherlink_20x20_d0_V1-B1-P1
        solve(new KrazyDadSource(20, 20, "12333.21..2...2.21.3....2..11.21.......2.......2.23222..21.2..20..2..1...122.2.23.3223..0.3.23212..33..323.33.1...1...03.....2.0.13.3.232.2......2.333..2.......22....2....23....2233..3.2.2..2.3....2..2..2112.211132...31312..3..231..31..2..333.23..2.2.1...2..23................221...333..2211..22..22310212...3.32..21...3.1..2222311...1.2..332222..03.3.2...13.331.2....21.2.....32.2..32.232..2222223.1"))
    }

    @Test
    void medium7x7() {
        // KD_Slitherlink_7x7_d1_V1-B1-P1
        solve(new KrazyDadSource(7, 7, "33.3222.12.2...2...12202.2....3...3.1.12.113....2"))
    }

    @Test
    @Ignore("not smart enough yet")
    void medium20x20() {
        // KD_Slitherlink_20x20_d1_V1-B1-P1
        solve(new KrazyDadSource(20, 20, ".3..3..223..2...33....2130....0...0.12.22.312.32..22..2....22...2.2.2.22.20..2..3..1..2..230..2..12.....3222.2..2...32231.2.3......21..2...3..3.....31..3.321...2203213..22...11.3....2...3..30232..2022.1..22..2..21...32.....3.2........33...12.2....1.2....1.1.1.2..22333313.3..2..3.2.....1.1....2.3.2.3....2..3..2.2...1....3..2.221.1.222..1.2322.......3.1.122.3....233.32.1233.3..1.3...1...23.1...1..31"))
    }

    SolveState solve(PuzzleSource ps) {
        solve(ps.load())
    }

    SolveState solve(Puzzle p) {
        def s = new Solver()
        SolveState stats
        try {
            stats = s.solve(p)
            if (stats.solved) {
                println stats.puzzle
                println("done ($stats.moveCount)!")
            }
            assert stats.solved : "Didn't solve the puzzle"
        } catch (AssertionError ae) {
            if (stats) {
                println stats.puzzle
            }
            throw ae
        } catch (Exception e) {
            println p
            throw e
        }
        stats
    }

}
