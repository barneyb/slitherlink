package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
interface Move {

    EdgeCoord getEdge()
    EdgeState getState()
    Strategy getStrategy()

}
