package com.barneyb.slitherlink.grid
/**
 * I will create a square grid of the specified dimensions.
 */
class SquareGridFactory implements GridFactory {

    @Override
    Grid create(int width, int height) {
        if (width <= 0) {
            throw new IllegalArgumentException("Invalid width: $width")
        }
        if (height <= 0) {
            throw new IllegalArgumentException("Invalid height: $height")
        }
        def g = new Grid()
        // todo: finish me!
        g
    }

}
