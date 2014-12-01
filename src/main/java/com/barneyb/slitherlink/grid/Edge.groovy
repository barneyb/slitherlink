package com.barneyb.slitherlink.grid
import groovy.transform.Canonical
/**
 *
 *
 * @author barneyb
 */
@Canonical
final class Edge {

    Dot start
    Dot end
    Face left
    Face right

}
