package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.AdjacentOnesOnEdge
import com.barneyb.slitherlink.strat.AdjacentThrees
import com.barneyb.slitherlink.strat.ClueSatisfied
import com.barneyb.slitherlink.strat.ForcedToOne
import com.barneyb.slitherlink.strat.KittyCornerThrees
import com.barneyb.slitherlink.strat.NeedAllRemaining
import com.barneyb.slitherlink.strat.NoBranching
import com.barneyb.slitherlink.strat.OneInCorner
import com.barneyb.slitherlink.strat.OneWithEdgePair
import com.barneyb.slitherlink.strat.ReachThree
import com.barneyb.slitherlink.strat.SingleEgress
import com.barneyb.slitherlink.strat.SingleIngress
import com.barneyb.slitherlink.strat.SingleLoop
import com.barneyb.slitherlink.strat.ThreeInCorner
import com.barneyb.slitherlink.strat.ThreeWithEdgePair
import com.barneyb.slitherlink.strat.TwoInCorner

/**
 *
 * @author bboisvert
 */
class Solver {

    static final Collection<Strategy> STRATEGIES = [
        new OneInCorner(),
        new TwoInCorner(),
        new ThreeInCorner(), // unneeded cuzza ThreeWithEdgePair, though _slightly_ faster
        new KittyCornerThrees(),
        new AdjacentThrees(),
        new AdjacentOnesOnEdge(),

        new NoBranching(),
        new ClueSatisfied(),
        new NeedAllRemaining(),
        new SingleIngress(),
        new SingleEgress(),
        new SingleLoop(),
        new ForcedToOne(),
        new ReachThree(),
        new OneWithEdgePair(),
        new ThreeWithEdgePair(),
    ].asImmutable()

    static {
        Strategy.metaClass.nextMoves { Puzzle p ->
            def m = delegate.nextMove(p)
            m == null ? null : [m]
        }
    }

    SolveState solve(PuzzleSource ps) {
        solve(ps.load())
    }

    SolveState solve(Puzzle p) {
        def strats = new ArrayList<Strategy>(STRATEGIES)
        int moveCount = 0
        boolean moved = true
        while (moved) {
            moved = false;
            for (def itr = strats.iterator(); itr.hasNext();) {
                def s = itr.next()
                def ms = s.nextMoves(p)
                if (ms != null) {
                    if (ms.empty) {
                        throw new IllegalStateException("Strategy.nextMoves(Puzzle) may not return an empty Collection.")
                    }
//                    println "$p\n- ${s.getClass().simpleName} ----------------"
//                    println s.getClass().simpleName + " : " + m
                    for (m in ms) {
                        p.move(m)
                        moveCount += 1
                    }
                    moved = true
                    break
                }
                if (s instanceof StaticStrategy) {
                    itr.remove()
                }
            }
        }
        new SolveState(p, p.solved, moveCount)
    }

}
