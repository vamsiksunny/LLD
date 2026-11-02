package com.example.lld.design_patterns.command;

/**
 * Receiver: does the actual work. It knows nothing about commands or undo.
 */
public class TextEditor {
    private final StringBuilder text = new StringBuilder();

    public void append(String s) {
        text.append(s);
    }

    public void deleteLast(int length) {
        text.setLength(text.length() - length);
    }

    public String getText() {
        return text.toString();
    }
}
