package com.example.lld.design_patterns.command;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Invoker: runs commands and keeps history for undo/redo.
 * It works with any Command without knowing what the command does.
 */
public class CommandInvoker {
    private final Deque<Command> undoStack = new ArrayDeque<>();
    private final Deque<Command> redoStack = new ArrayDeque<>();

    public void run(Command command) {
        command.execute();
        undoStack.push(command);
        redoStack.clear();   // a new action invalidates the redo history
    }

    public void undo() {
        if (undoStack.isEmpty()) {
            return;
        }
        Command command = undoStack.pop();
        command.undo();
        redoStack.push(command);
    }

    public void redo() {
        if (redoStack.isEmpty()) {
            return;
        }
        Command command = redoStack.pop();
        command.execute();
        undoStack.push(command);
    }
}
