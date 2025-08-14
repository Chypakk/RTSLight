package chypakk.command;

import chypakk.model.Castle;

import java.util.Scanner;

public interface GameCommand {
    void execute(Castle castle);
}
