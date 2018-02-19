package com.barneyb.slitherlink

/**
 * I am an extension of Strategy to indicate I do not rely on the state of the
 * board. I.e., they only rely on the puzzle itself (dimensions and clues, but
 * not edge state). Thus I may be run a single time without reducing their
 * capabilities.
 *
 * @author barneyb
 */
interface StaticStrategy {

}
