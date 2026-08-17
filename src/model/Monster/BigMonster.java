package model.Monster;

import model.Position;
import java.util.*;

public class BigMonster extends Monster {
    private Random rand = new Random();

    public BigMonster(Position pos) {
        super(pos, "БОЛЬШОЙ МОНСТР", 'Б');
    }

    @Override
    public boolean encounter(Scanner scanner, int difficulty) {
        System.out.println("\n⚔️ " + name + " требует решить пример!");

        int a, b, answer;
        String op;

        // Генерация примера по сложности
        switch (difficulty) {
            case 1:
                a = rand.nextInt(10)+1;
                b = rand.nextInt(10)+1;
                op = "+";
                answer = a+b;
                break;

            case 2:
                a = rand.nextInt(20)+5;
                b = rand.nextInt(15)+5;
                if (rand.nextBoolean()) {
                    op = "+";
                    answer = a+b;
                } else {
                    op = "-";
                    if (a < b) { int t=a; a=b; b=t; }
                    answer = a-b;
                }
                break;

            default: // 3 - сложный
                int choice = rand.nextInt(3);
                if (choice == 0) {
                    a = rand.nextInt(30)+10;
                    b = rand.nextInt(20)+5;
                    op = "+";
                    answer = a+b;
                } else if (choice == 1) {
                    a = rand.nextInt(30)+10;
                    b = rand.nextInt(20)+5;
                    if (a < b) { int t=a; a=b; b=t; }
                    op = "-";
                    answer = a-b;
                } else {
                    a = rand.nextInt(10)+2;
                    b = rand.nextInt(9)+2;
                    op = "*";
                    answer = a*b;
                }
                break;
        }

        System.out.println("Решите: " + a + " " + op + " " + b + " = ?");
        System.out.print("Ваш ответ: ");

        try {
            int userAns = Integer.parseInt(scanner.nextLine().trim());
            boolean correct = userAns == answer;
            System.out.println(correct ? "✅ Правильно!" : "❌ Неправильно! Ответ: " + answer);
            return correct;
        } catch (NumberFormatException e) {
            System.out.println("❌ Некорректный ввод! Ответ: " + answer);
            return false;
        }
    }
}