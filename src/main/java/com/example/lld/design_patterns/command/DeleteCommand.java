package com.example.lld.design_patterns.command;

public class DeleteCommand implements Command {
    private final TextEditor editor;
    private final int length;
    private String deleted = "";

    public DeleteCommand(TextEditor editor, int length) {
        this.editor = editor;
        this.length = length;
    }

    @Override
    public void execute() {
        String current = editor.getText();
        int n = Math.min(length, current.length());
        deleted = current.substring(current.length() - n);   // remember what we removed, for undo
        editor.deleteLast(n);
    }

    @Override
    public void undo() {
        editor.append(deleted);
    }
}
