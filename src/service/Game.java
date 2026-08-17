package service;

import model.Monster.BigMonster;
import model.Monster.SmallMonster;
import model.Monster.Monster;
import model.Position;
import java.util.*;

class Player {
    Position pos;
    int lives = 3;
    boolean alive = true;

    Player(Position pos) {
        this.pos = pos;
    }

    void loseLife() {
        if (--lives <= 0) alive = false;
    }

    void moveTo(Position newPos) {
        this.pos = newPos;
    }
}

class Castle {
    Position pos;
    boolean reached = false;

    Castle(Position pos) {
        this.pos = pos;
    }
}

public class Game {
    private static final int SIZE = 5;
    private char[][] field = new char[SIZE][SIZE];
    private Player player;
    private Castle castle;
    private List<Monster> monsters = new ArrayList<>();
    private int difficulty;
    private boolean gameOver = false;
    private boolean win = false;
    private Random rand = new Random();

    void init(int difficulty) {
        this.difficulty = difficulty;

        // Очистка поля
        for (char[] row : field) Arrays.fill(row, ' ');

        // Замок на первой строке
        castle = new Castle(new Position(0, rand.nextInt(SIZE)));
        field[castle.pos.row][castle.pos.col] = 'З';

        // Игрок на последней строке
        player = new Player(new Position(SIZE-1, rand.nextInt(SIZE)));
        field[player.pos.row][player.pos.col] = 'Г';

        // Монстры
        spawnMonsters(3, SmallMonster.class);
        spawnMonsters(4, BigMonster.class);
    }

    private void spawnMonsters(int count, Class<? extends Monster> type) {
        for (int i = 0; i < count; i++) {
            Position pos = getEmptyPos();
            if (pos == null) break;
            try {
                Monster m = type.getConstructor(Position.class).newInstance(pos);
                monsters.add(m);
                field[pos.row][pos.col] = m.symbol;
            } catch (Exception e) {}
        }
    }

    private Position getEmptyPos() {
        for (int attempt = 0; attempt < 100; attempt++) {
            int r = rand.nextInt(SIZE), c = rand.nextInt(SIZE);
            if (field[r][c] == ' ') return new Position(r, c);
        }
        return null;
    }

    boolean movePlayer(Position newPos) {
        // Проверка валидности
        if (newPos.row < 0 || newPos.row >= SIZE || newPos.col < 0 || newPos.col >= SIZE) {
            System.out.println("❌ Неверные координаты!");
            return false;
        }

        if (!player.pos.isAdjacent(newPos)) {
            System.out.println("❌ Можно ходить только на 1 клетку вверх/вниз/влево/вправо!");
            return false;
        }

        // Сохраняем содержимое клетки
        char cellContent = field[newPos.row][newPos.col];

        // Перемещаем игрока
        field[player.pos.row][player.pos.col] = ' ';
        player.moveTo(newPos);
        field[newPos.row][newPos.col] = 'Г';

        // Проверка на замок
        if (cellContent == 'З') {
            win = true;
            gameOver = true;
        }

        return true;
    }

    Monster getMonsterAt(Position pos) {
        for (Monster m : monsters) {
            if (m.pos.equals(pos)) return m;
        }
        return null;
    }

    void removeMonster(Monster m) {
        monsters.remove(m);
        field[m.pos.row][m.pos.col] = ' ';
    }

    // Геттеры
    char[][] getField() { return field; }
    Player getPlayer() { return player; }
    Castle getCastle() { return castle; }
    List<Monster> getMonsters() { return monsters; }
    int getSize() { return SIZE; }
    boolean isGameOver() { return gameOver; }
    boolean isWin() { return win; }
    void setGameOver(boolean over) { gameOver = over; }
    int getDifficulty() { return difficulty; }
}