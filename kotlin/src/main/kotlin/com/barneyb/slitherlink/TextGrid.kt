package com.barneyb.slitherlink

class TextGrid(
        private val indent: Int
) {

    private val rows: MutableList<Array<String>> = mutableListOf()
    private var types: CharArray? = null
    private var colCount: Int = -1

    operator fun plusAssign(items: List<*>) {
        if (rows.isEmpty()) {
            if (items.size == 0) {
                throw IllegalArgumentException("There must be at least one column")
            }
            colCount = items.size
        } else {
            if (items.size != colCount) {
                throw IllegalArgumentException("This TextGrid has $colCount column(s), not ${items.size}")
            }
            if (types == null) {
                types = CharArray(colCount) {
                    if (items[it] is Number) 'n' else 's'
                }
            }
        }
        rows.add(
                items.map {
                    it.toString()
                }.toTypedArray()
        )
    }

    override fun toString(): String {
        val ls = IntArray(colCount)
        for (r in rows) {
            for (c in 0 until r.size) {
                ls[c] = Math.max(ls[c], r[c].length + 2)
            }
        }
        val margin = CharArray(indent, { ' ' }).joinToString("")
        val sb = StringBuilder()
        for (it in rows) {
            sb.append(margin)
            for (c in 0 until it.size) {
                val v = it[c]
                val t = if (types == null) 's' else types!![c]
                val l = ls[c]
                if (t == 'n') {
                    sb.append(v.padStart(l))
                } else {
                    sb.append(v.padEnd(l))
                }
            }
            sb.append("\n")
        }
        return sb.toString()
    }

}
