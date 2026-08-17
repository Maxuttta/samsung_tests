package model;

public class Position {
    public int row;
    public int col;

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public boolean isAdjacent(Position other) {
        return (Math.abs(this.row - other.row) == 1 && this.col == other.col) ||
                (Math.abs(this.col - other.col) == 1 && this.row == other.row);
    }

    boolean equals(Position other) {
        return this.row == other.row && this.col == other.col;
    }

    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}