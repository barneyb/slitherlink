package com.barneyb.slitherlink

import com.barneyb.slitherlink.strat.AdjacentThrees
import com.barneyb.slitherlink.strat.ClueSatisfied
import com.barneyb.slitherlink.strat.KittyCornerThrees
import com.barneyb.slitherlink.strat.NeedAllRemaining
import com.barneyb.slitherlink.strat.NoBranching
import com.barneyb.slitherlink.strat.OneInCorner
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

        new NoBranching(),
        new ClueSatisfied(),
        new NeedAllRemaining(),
        new ReachThree(),
        new SingleIngress(),
        new SingleEgress(),
        new SingleLoop(),
        new ThreeWithEdgePair(),
    ].asImmutable()

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
                def m = s.nextMove(p)
                if (m != null) {
//                    println "$p\n- ${s.getClass().simpleName} ----------------"
//                    println s.getClass().simpleName + " : " + m
                    p.move(m)
                    moved = true
                    moveCount += 1
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
