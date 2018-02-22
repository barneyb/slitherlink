package com.barneyb.slitherlink

import com.barneyb.slitherlink.io.krazydad
import org.junit.Test

class SolverTest {

    @Test
    fun initial() {
        assertSolve(twoByTwo())
    }

    @Test
    fun easy7x7() {
        // KD_Slitherlink_7x7_d0_V1-B1-P1 : 69970A5CE4516DBE233ED26CC7ED
        assertSolve(krazydad("222331...02.3........33..33...2.2..3.2.22.3.2..3."))
    }

    @Test
    fun easy20x20() {
        // KD_Slitherlink_20x20_d0_V1-B1-P1 : 4E97A46AC435B9EDAC96959E9AC35BCB455232AD062A6CBA8193B1A3D77ED89822B4151BC3EA4B5ADD8615CA2C453E2E2B57AA795137741198BAC4A17517656CFECAA0F2B252655721713BBB213F8A42AE179A37CE05FC019937588CD5562BBAA24F22E8CDDD9A7E93
        assertSolve(krazydad("12333.21..2...2.21.3....2..11.21.......2.......2.23222..21.2..20..2..1...122.2.23.3223..0.3.23212..33..323.33.1...1...03.....2.0.13.3.232.2......2.333..2.......22....2....23....2233..3.2.2..2.3....2..2..2112.211132...31312..3..231..31..2..333.23..2.2.1...2..23................221...333..2211..22..22310212...3.32..21...3.1..2222311...1.2..332222..03.3.2...13.331.2....21.2.....32.2..32.232..2222223.1"))
    }

    @Test
    fun medium7x7() {
        // KD_Slitherlink_7x7_d1_V1-B1-P1 : BEC599999693E44747AE5019277E
        assertPartialSolve(77, krazydad("33.3222.12.2...2...12202.2....3...3.1.12.113....2"))
        // KD_Slitherlink_7x7_d1_V1-B1-P2 : D9E53C293DC94B4763266AA8277E
        assertPartialSolve(66, krazydad("3..3.31......2.0323..3..13....212.3.....1.13.322."))
        // KD_Slitherlink_7x7_d1_V1-B1-P3 : BE9A389837C9CC4E4BD816CA3ED0
        assertSolve(krazydad("33..3..2.....23.223032.....321..2..1.1...3..233.."))
    }

    @Test
    fun medium20x20() {
        // KD_Slitherlink_20x20_d1_V1-B1-P1 : 637EA67EC4997EC88989272AABF8A4F0F455137A27A8AEDC44B4C354BEC15638E1313323534F4F9523A7384293E8140FC9569527E88A3EC3F01B2A59AAADDDD4C454C4455CDA677C9A69A435D245F47B9AED9AAAAA15554555CCE88C92267B4FE64AB351BAAA7AAAA3
        assertPartialSolve(292, krazydad(".3..3..223..2...33....2130....0...0.12.22.312.32..22..2....22...2.2.2.22.20..2..3..1..2..230..2..12.....3222.2..2...32231.2.3......21..2...3..3.....31..3.321...2203213..22...11.3....2...3..30232..2022.1..22..2..21...32.....3.2........33...12.2....1.2....1.1.1.2..22333313.3..2..3.2.....1.1....2.3.2.3....2..3..2.2...1....3..2.221.1.222..1.2322.......3.1.122.3....233.32.1233.3..1.3...1...23.1...1..31"))
        // KD_Slitherlink_20x20_d1_V1-B11-P4 : 9D6EA6B9E5D4BD3AC9861A5A8E3D2BB92C361699287169DB9466CAACC22619557C9ABA3DA2C3BAC986DD927C2D2B415F899E9587AA7A55DA2155579AE517A22A655BB11BB942A9ACD9223B84D351BB473222AD94E3CABBC0D11951AEC5AE51BA013344A3FDDD9777AE
        assertPartialSolve(346, krazydad("...132...12..31...3.22..3....2..12...121..31..32323...123..3...22..2....2.222.23.3331...3...21...22.....0.222.3.3..3.....2332.3.....2..22.3..2.2....2223...3....1..3.2..2...021.32.21...3..22....3...2..2..2...2.231.2......22..2.2.2.3.3.2..1223.....1.23...2.333...2.2....1.2.332..2..22.2.....23..0..33.3213.1..1.0....3...2.3..133.3..32...31.....3.12..2.3..3.2232.112.2.2...3....21.3..2..2...3...33..21.."))
    }

    @Test
    fun hard7x7() {
        // KD_Slitherlink_7x7_d2_V1-B1-P1 : 63560AB8B6517EA6A53A3135D7AE
        assertPartialSolve(32, krazydad(".3.22.131212..3....2......2.3.3..1.20.13....32223"))
    }

    @Test
    fun hard20x20() {
        // KD_Slitherlink_20x20_d2_V1-B1-P1 : 9D6A37A9EC997ECA65857AA883BAA254DE25ED5163E89E55EA391B449611A7CD437B2E4D645CC771AE8967E29B44A031595467CD178BB13C31FC3D165185A88ECBA3B2CA25B69AD575923F0907CA1FC2795A536A4FCC6C3D3DC4EC832A8EC15238AC256E7A7A7E9E93
        assertPartialSolve(334, krazydad("2....2.2.1.2.22.33....2.3...3.32....1.1212..223..2...22.32..1.2..2..22.......22.2..1...33....213.1..1..22...13....31.3...32.....2..32..10..33.3..21..3...3.23.2.2..2.2.1..1....2.1....2..22331..33..2...213..2...2.....3..32....3...11.2..0.1..3..3.232..32...2.2.2..3.2.2.232.3.....2.3.3...12..2.....123..2.2..21....33.2.3.02..3...3.2.2..2.2..2..1.....2..0.21.2......1.3121..232.3..2..3.21....22...22..32."))
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
        val maxMoves = p.humanRows * (p.humanCols + 1) + (p.humanRows + 1) * p.humanCols
        if (stats.moveCount > maxMoves) {
            throw AssertionError("A ${p.humanRows}x${p.humanCols} grid only has $maxMoves available moves, but ${stats.moveCount} were made?!")
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
