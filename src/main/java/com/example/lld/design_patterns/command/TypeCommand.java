package com.example.lld.design_patterns.command;

public class TypeCommand implements Command {
    private final TextEditor editor;
    private final String text;

    public TypeCommand(TextEditor editor, String text) {
        this.editor = editor;
        this.text = text;
    }

    @Override
    public void execute() {
        editor.append(text);
    }

    @Override
    public void undo() {
        editor.deleteLast(text.length());
    }
}
