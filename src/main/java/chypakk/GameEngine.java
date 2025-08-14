package chypakk;

import chypakk.command.ExitCommand;
import chypakk.command.GameCommand;
import chypakk.command.factory.CommandFactory;
import chypakk.model.Castle;
import chypakk.model.resources.generator.Forest;
import chypakk.model.resources.generator.GoldMine;
import chypakk.model.resources.generator.ResourceGenerator;

import java.util.Scanner;

//todo переписать на lanterna
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

    //todo внедрить паттерн Factory для юнитов (возможно ещё для генераторов)
    private void startCommandLoop(Castle castle) {
        Scanner scanner = new Scanner(System.in);
        CommandFactory commandFactory = new CommandFactory();

        while (castle.isAlive()) {
            printMenu();

            int choice = scanner.nextInt();
            GameCommand command = commandFactory.getCommand(choice);
            command.execute(castle);

            if (command instanceof ExitCommand) {
                return;
            }
        }
    }

    private void printMenu(){
        System.out.println("\nДоступные команды:");
        System.out.println("1 - Добавить золотую шахту (50 золота, 60 дерева)");
        System.out.println("2 - Добавить лес (70 дерева)");
        System.out.println("3 - Статус ресурсов");
        System.out.println("4 - Список генераторов");
        System.out.println("0 - Выход");
        System.out.print("Ваш выбор: ");
    }

}