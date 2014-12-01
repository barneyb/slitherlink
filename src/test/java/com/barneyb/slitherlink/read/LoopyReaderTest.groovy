package com.barneyb.slitherlink.read

import com.barneyb.slitherlink.grid.GridType
import org.junit.Test

class LoopyReaderTest {

    @Test(expected = IllegalArgumentException)
    void emptyParams() {
        new LoopyReader("", "")
    }

    @Test(expected = IllegalArgumentException)
    void nullParams() {
        new LoopyReader(null, "")
    }

    @Test(expected = IllegalArgumentException)
    void malformedParms() {
        new LoopyReader("adfasdf", "")
    }

    @Test(expected = IllegalArgumentException)
    void unknownTypeParam() {
        new LoopyReader("7x7t1234", "")
    }

    @Test
    void goodParams() {
        def r = new LoopyReader("7x3t0", "u")
        assert r.type == GridType.SQUARE
        assert r.width == 7
        assert r.height == 3
        assert r.descriptor == "u"
    }

    @Test
    void goodParamsWithDifficulty() {
        def r = new LoopyReader("7x3t0de", "u")
        assert r.type == GridType.SQUARE
        assert r.width == 7
        assert r.height == 3
        assert r.descriptor == "u"
    }

}
