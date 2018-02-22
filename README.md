slitherlink
===========

A simple datamodel for Slitherlink puzzles for playing with solving strategies.

[![Build Status](https://travis-ci.org/barneyb/slitherlink.svg?branch=master)](https://travis-ci.org/barneyb/slitherlink)

## tl;dr

    git clone git@github.com:barneyb/slitherlink.git
    cd slitherlink
    mvn install

## Background

I started building in Groovy, but after getting a ways into it, I switched
to Kotlin. Both source trees are available in modules named for the language,
and both modules will build in sequence using the parent/aggregator at the
root level.

At the point that the Kotlin reached feature parity, the test suite runs in
about 1/10th of the time and there are about 2/3rds as many lines of code.

The Kotlin solver uses coroutines to yield moves to make, rather than returning
collections of moves to make. The strategies are also simple functions (of
type `(Puzzle) -> Sequence<Move>`) rather than full classes like in Groovy.
Both of these carry runtime overhead. The original Kotlin looked very much
like the Groovy style w/ classes and collections. When I did the conversion,
the test suite's run time a little more than doubled. So if the moves were
passed out directly like Groovy, that 1/10th number up there would probably
be more like 1/25th.

