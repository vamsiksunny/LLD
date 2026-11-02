package com.example.lld.design_patterns.command;

/**
 * Command interface: wraps a request as an object so it can be stored, queued and undone.
 */
public interface Command {
    void execute();

    void undo();
}
