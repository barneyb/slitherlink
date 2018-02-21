package com.barneyb.slitherlink

fun main(args: Array<String>) {
    println(greet(if (args.isEmpty()) "World" else args[0]))
}

fun greet(name: String): String {
    return "Hello, $name"
}

