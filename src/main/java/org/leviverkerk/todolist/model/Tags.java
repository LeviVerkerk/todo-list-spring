package org.leviverkerk.todolist.model;

public enum Tags {
    BUSINESS ("Business"),
    PRIVATE ("Private"),
    SCHOOL ("School");

    public final String string;

    Tags(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
