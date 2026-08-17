package model.Monster;

import model.Position;
import java.util.Scanner;

public abstract class Monster {
    public Position pos;
    public char symbol;
    String name;

    Monster(Position pos, String name, char symbol) {
        this.pos = pos;
        this.name = name;
        this.symbol = symbol;
    }

    public abstract boolean encounter(Scanner scanner, int difficulty);
}