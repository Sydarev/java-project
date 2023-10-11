package ru.netology.graphics.image;

public class Schema implements TextColorSchema {
    protected char[] symbols;

    public Schema(char[] symbols) {
        this.symbols = symbols;
    }

    public Schema() {
        this.symbols = new char[]{'#', '$', '@', '%', '*', '+', '-', '\\'};
    }


    @Override
    public char convert(int color) {
        return symbols[(int) Math.floor(color / 256. * symbols.length)];
    }
}
