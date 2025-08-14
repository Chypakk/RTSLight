package org.chypakk;

import org.chypakk.model.Castle;
import org.chypakk.model.resources.generator.Forest;
import org.chypakk.model.resources.generator.GoldMine;
import org.chypakk.model.resources.generator.ResourceGenerator;

import java.util.Scanner;

public class GameEngine {

    private final Castle castle;

    public GameEngine() {
        this.castle = new Castle(100);
    }

    public void start() {

        GoldMine goldMine = new GoldMine(5, 10, 70, castle);
        Forest forest = new Forest(2, 5, 100, castle);
        castle.addGenerator(goldMine);
        castle.addGenerator(forest);

        startCommandLoop(castle);
    }

    private void startCommandLoop(Castle castle) {
        Scanner scanner = new Scanner(System.in);
        while (castle.isAlive()) {
            System.out.println("\nДоступные команды:");
            System.out.println("1 - Добавить золотую шахту (50 золота, 60 дерева)");
            System.out.println("2 - Добавить лес (70 дерева)");
            System.out.println("3 - Статус ресурсов");
            System.out.println("4 - Список генераторов");
            System.out.println("0 - Выход");
            System.out.print("Ваш выбор: ");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    if (castle.getResource("gold") >= 50 && castle.getResource("wood") >= 60) {

                        castle.removeResource("gold", 50);
                        castle.removeResource("wood", 60);
                        ResourceGenerator goldMine = new GoldMine(2, 10, 70, castle);
                        castle.addGenerator(goldMine);

                    } else {
                        System.out.println("Недостаточно ресурсов");
                    }
                    break;

                case 2:
                    if (castle.getResource("wood") >= 70) {
                        castle.removeResource("wood", 70);
                        ResourceGenerator forest = new Forest(1, 5, 100, castle);
                        castle.addGenerator(forest);
                    } else {
                        System.out.println("Недостаточно ресурсов");
                    }
                    break;

                case 3:
                    castle.printResources();
                    break;

                case 4:
                    castle.printGenerators();
                    break;

                case 0:
                    System.out.println("Выход из игры...");
                    stopAllGenerators(castle);
                    return;

                default:
                    System.out.println("Неверная команда!");
            }
        }
    }

    private static void stopAllGenerators(Castle castle) {
        synchronized (castle) {
            for (ResourceGenerator generator : castle.getGenerators()) {
                generator.stopGenerator();
            }
        }
        System.out.println("Все генераторы остановлены");
    }

}