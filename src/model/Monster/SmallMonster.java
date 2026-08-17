package model.Monster;

import model.Position;
import model.Monster.Monster;
import java.util.*;

public class SmallMonster extends Monster {
    private static final String[][] RIDDLES = {
            {"Висит груша, нельзя скушать.", "Лампа"},
            {"Зимой и летом одним цветом.", "Ёлка"},
            {"Не лает, не кусает, а в дом не пускает.", "Замок"},
            {"Без рук, без ног, а ворота открывает.", "Ветер"},
            {"Что можно приготовить, но нельзя съесть?", "Уроки"},
            {"Всегда во рту, а не проглотишь.", "Язык"}
    };
    private Random rand = new Random();

    public SmallMonster(Position pos) {
        super(pos, "МАЛЕНЬКИЙ МОНСТР", 'М');
    }

    @Override
    public boolean encounter(Scanner scanner, int difficulty) {
        System.out.println("\n🐉 " + name + " задаёт загадку:");
        String[] riddle = RIDDLES[rand.nextInt(RIDDLES.length)];
        System.out.println("Загадка: " + riddle[0]);
        System.out.print("Ваш ответ: ");

        boolean correct = scanner.nextLine().trim().equalsIgnoreCase(riddle[1]);
        System.out.println(correct ? "✅ Правильно!" : "❌ Неправильно! Ответ: " + riddle[1]);
        return correct;
    }
}