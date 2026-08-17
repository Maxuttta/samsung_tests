package service;

import model.Monster.Monster;
import model.Position;

import java.util.Scanner;

public class GameService {
    private Game game;
    private Scanner scanner = new Scanner(System.in);

    public void start() {
        System.out.println("\n==========================================");
        System.out.println("       ДОБРО ПОЖАЛОВАТЬ В ИГРУ-КВЕСТ!");
        System.out.println("==========================================");
        System.out.println("Цель: добраться до замка (З) на верхней строке");
        System.out.println("У вас 3 жизни. Осторожнее с монстрами!");
        System.out.println("==========================================\n");

        // Выбор сложности
        int diff = 0;
        while (diff < 1 || diff > 3) {
            System.out.print("Выберите сложность (1-легкий, 2-средний, 3-сложный): ");
            try { diff = Integer.parseInt(scanner.nextLine()); }
            catch (NumberFormatException e) { System.out.println("Введите число 1-3"); }
        }

        game = new Game();
        game.init(diff);
        System.out.println("\nИгра начата! Удачи! 🎮\n");

        // Игровой цикл
        while (!game.isGameOver()) {
            printField();
            printStatus();
            printControls();

            String input = scanner.nextLine().trim();
            if (input.equalsIgnoreCase("q")) {
                System.out.println("Выход из игры.");
                break;
            }

            try {
                String[] parts = input.split("\\s+");
                if (parts.length != 2) {
                    System.out.println("❌ Введите два числа через пробел!");
                    continue;
                }

                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                Position target = new Position(row, col);

                // Проверяем, есть ли монстр на клетке
                Monster monster = game.getMonsterAt(target);

                // Перемещаем игрока
                boolean moved = game.movePlayer(target);
                if (!moved) continue;

                // Встреча с монстром
                if (monster != null) {
                    boolean success = monster.encounter(scanner, game.getDifficulty());
                    if (success) {
                        game.removeMonster(monster);
                        System.out.println("💪 Монстр побеждён!");
                    } else {
                        game.getPlayer().loseLife();
                        System.out.println("💔 Осталось жизней: " + game.getPlayer().lives);
                        if (!game.getPlayer().alive) {
                            game.setGameOver(true);
                            System.out.println("\n💀 GAME OVER!");
                        }
                    }
                }

                // Проверка победы
                if (game.isWin()) {
                    printField();
                    System.out.println("\n🎉 ПОБЕДА! Вы добрались до замка!");
                    System.out.println("Осталось жизней: " + game.getPlayer().lives);
                    System.out.println("==========================================");
                    break;
                }

            } catch (NumberFormatException e) {
                System.out.println("❌ Введите числа или 'q' для выхода");
            }
        }

        scanner.close();
    }

    private void printField() {
        char[][] f = game.getField();
        int size = game.getSize();

        System.out.println("\nИгровое поле:");
        System.out.print("  ");
        for (int i = 0; i < size; i++) System.out.print(" " + i + "  ");
        System.out.println();

        for (int i = 0; i < size; i++) {
            System.out.print("  ");
            for (int j = 0; j < size; j++) System.out.print("+---");
            System.out.println("+");

            System.out.print(i + " ");
            for (int j = 0; j < size; j++) System.out.print("| " + f[i][j] + " ");
            System.out.println("|");
        }
        System.out.print("  ");
        for (int j = 0; j < size; j++) System.out.print("+---");
        System.out.println("+");
    }

    private void printStatus() {
        System.out.println("\n--- СТАТУС ---");
        System.out.println("❤️ Жизни: " + game.getPlayer().lives + "/3");
        System.out.println("📍 Игрок: " + game.getPlayer().pos);
        System.out.println("🏰 Замок: " + game.getCastle().pos);
        System.out.println("👾 Монстров: " + game.getMonsters().size());

        // Подсказка
        int dr = game.getPlayer().pos.row - game.getCastle().pos.row;
        int dc = game.getPlayer().pos.col - game.getCastle().pos.col;
        if (dr > 0) System.out.print("⬆️ Замок выше ");
        else if (dr < 0) System.out.print("⬇️ Замок ниже ");
        if (dc > 0) System.out.print("⬅️ Замок левее");
        else if (dc < 0) System.out.print("➡️ Замок правее");
        if (dr != 0 || dc != 0) System.out.println();
    }

    private void printControls() {
        System.out.println("\nКоманды:");
        System.out.println("  Введите 'строка столбец' (0-4) для хода");
        System.out.println("  Например: 2 3");
        System.out.println("  'q' - выход");
        System.out.print("> ");
    }
}