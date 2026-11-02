package com.example.lld.design_patterns.command;

public class CommandDemo {
    public static void main(String[] args) {
        TextEditor editor = new TextEditor();
        CommandInvoker invoker = new CommandInvoker();

        invoker.run(new TypeCommand(editor, "Hello"));
        invoker.run(new TypeCommand(editor, " World"));
        System.out.println(editor.getText());   // Hello World

        invoker.run(new DeleteCommand(editor, 6));
        System.out.println(editor.getText());   // Hello

        invoker.undo();
        System.out.println(editor.getText());   // Hello World

        invoker.undo();
        System.out.println(editor.getText());   // Hello

        invoker.redo();
        System.out.println(editor.getText());   // Hello World
    }
}
