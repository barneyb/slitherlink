package com.barneyb.slitherlink

/**
 * I am a marker interface for Strategy implementations which do not rely on
 * the state of the board, they only rely on the puzzle itself (dimensions and
 * clues, but not edge state). Thus they may be run a single time without
 * limitting their capabilities.
 *
 * @author barneyb
 */
interface Idempotent {

}
