package com.barneyb.slitherlink.grid

/**
 * I create a new empty Grid with the specified dimensions.
 */
interface GridFactory {

    Grid create(int width, int height)

}
