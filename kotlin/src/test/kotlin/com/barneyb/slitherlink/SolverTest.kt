package com.barneyb.slitherlink

import com.barneyb.slitherlink.io.krazydad
import org.junit.Test

/**
 *
 *
 * @author barneyb
 */
class SolverTest {

    @Test
    fun initial() {
        assertSolve(twoByTwo())
    }

    @Test
    fun easy7x7() {
        // KD_Slitherlink_7x7_d0_V1-B1-P1
        assertSolve(krazydad("222331...02.3........33..33...2.2..3.2.22.3.2..3."))
    }

    @Test
    fun easy20x20() {
        // KD_Slitherlink_20x20_d0_V1-B1-P1
        assertSolve(krazydad("12333.21..2...2.21.3....2..11.21.......2.......2.23222..21.2..20..2..1...122.2.23.3223..0.3.23212..33..323.33.1...1...03.....2.0.13.3.232.2......2.333..2.......22....2....23....2233..3.2.2..2.3....2..2..2112.211132...31312..3..231..31..2..333.23..2.2.1...2..23................221...333..2211..22..22310212...3.32..21...3.1..2222311...1.2..332222..03.3.2...13.331.2....21.2.....32.2..32.232..2222223.1"))
    }

    @Test
    fun medium7x7() {
        // KD_Slitherlink_7x7_d1_V1-B1-P1
        assertPartialSolve(51, krazydad("33.3222.12.2...2...12202.2....3...3.1.12.113....2"))
        // KD_Slitherlink_7x7_d1_V1-B1-P2
        assertPartialSolve(41, krazydad("3..3.31......2.0323..3..13....212.3.....1.13.322."))
        // KD_Slitherlink_7x7_d1_V1-B1-P3
        assertPartialSolve(54, krazydad("33..3..2.....23.223032.....321..2..1.1...3..233.."))
    }

    @Test
    fun medium20x20() {
        // KD_Slitherlink_20x20_d1_V1-B1-P1
        assertPartialSolve(263, krazydad(".3..3..223..2...33....2130....0...0.12.22.312.32..22..2....22...2.2.2.22.20..2..3..1..2..230..2..12.....3222.2..2...32231.2.3......21..2...3..3.....31..3.321...2203213..22...11.3....2...3..30232..2022.1..22..2..21...32.....3.2........33...12.2....1.2....1.1.1.2..22333313.3..2..3.2.....1.1....2.3.2.3....2..3..2.2...1....3..2.221.1.222..1.2322.......3.1.122.3....233.32.1233.3..1.3...1...23.1...1..31"))
        // KD_Slitherlink_20x20_d1_V1-B11-P4
        assertPartialSolve(343, krazydad("...132...12..31...3.22..3....2..12...121..31..32323...123..3...22..2....2.222.23.3331...3...21...22.....0.222.3.3..3.....2332.3.....2..22.3..2.2....2223...3....1..3.2..2...021.32.21...3..22....3...2..2..2...2.231.2......22..2.2.2.3.3.2..1223.....1.23...2.333...2.2....1.2.332..2..22.2.....23..0..33.3213.1..1.0....3...2.3..133.3..32...31.....3.12..2.3..3.2232.112.2.2...3....21.3..2..2...3...33..21.."))
    }

    fun assertPartialSolve(moves: Int, p: Puzzle) {
        val stats = run(p)
        if (stats.moveCount < moves) {
            throw AssertionError("expected $moves moves to be made, but only ${stats.moveCount} were")
        } else if (stats.moveCount > moves) {
            println("[EXCEED] ${stats.moveCount} moves were made, but only $moves were expected")
        }
    }

    fun assertSolve(p: Puzzle) {
        val stats = run(p)
        if (!stats.solved) {
            throw AssertionError("Didn't solve the puzzle")
        }
    }

    private fun run(p: Puzzle): SolveState {
        val stats = try {
            solve(p)
        } catch (e: Exception) {
            println(p)
            throw e
        }

        println(stats.puzzle)
        println("done (${stats.moveCount} / ${stats.batchCount} / ${stats.callCount}) ${stats.strategyElapsed} : ${stats.overhead} ms!")
        val grid = TextGrid(2)
        grid += listOf(""
                , "move", "per"
                , "batch", "per"
                , "call", "per"
                , "total", "waste"
        )
        var trace = stats.byStrategy
        trace += StrategyState("overall", stats.trace)
        for (it in trace) {
            val mc = it.moveCount
            val bc = it.batchCount
            val be = it.batchElapsed
            val cc = it.callCount
            val te = it.totalElapsed
            grid += listOf(it.source
                    , if (mc > 0) mc else '-', if (mc > 0) be / mc else '-'
                    , if (bc > 0) bc else '-', if (bc > 0) be / bc else '-'
                    , if (cc > 0) cc else '-', if (cc > 0) te / cc else '-'
                    , te, (te - be)
            )
        }
        println(grid)
        return stats
    }

}
