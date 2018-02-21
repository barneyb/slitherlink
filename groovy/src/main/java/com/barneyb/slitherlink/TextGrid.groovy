package com.barneyb.slitherlink

/**
 *
 *
 * @author barneyb
 */
class TextGrid {

    private static final String[] EMPTY_STRING_ARRAY = new String[0]

    private final int indent
    private List<String[]> rows
    private String[] types
    private int colCount

    TextGrid(int indent) {
        this.indent = indent
    }

    void leftShift(List<Object> items) {
        if (rows == null) {
            if (items.size() == 0) {
                throw new IllegalArgumentException("There must be at least one column")
            }
            rows = []
            colCount = items.size()
        } else {
            if (items.size() != colCount) {
                throw new IllegalArgumentException("This TextGrid has $colCount column(s), not ${items.size()}")
            }
            if (types == null) {
                types = new char[colCount]
                for (int c = 0; c < colCount; c++) {
                    types[c] = items[c] instanceof Number ? 'n' : 's'
                }
            }
        }
        rows << items*.toString().toArray(EMPTY_STRING_ARRAY)
    }


    @Override
    String toString() {
        def ls = new int[colCount]
        rows.each {
            for (int c = 0; c < it.length; c++) {
                ls[c] = Math.max(ls[c], it[c].length() + 2)
            }
        }
        def margin = " " * indent
        def sb = new StringBuilder()
        rows.collect {
            sb.append(margin)
            for (int c = 0; c < it.length; c++) {
                def v = it[c],
                    t = types[c],
                    l = ls[c]
                sb.append(t == 'n' ? v.padLeft(l) : v.padRight(l))
            }
            sb.append("\n")
        }
        sb
    }
}
