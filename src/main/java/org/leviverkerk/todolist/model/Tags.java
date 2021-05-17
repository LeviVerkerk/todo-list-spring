package org.leviverkerk.todolist.model;

public enum Tags {
    BUSINESS ("business"),
    PRIVATE ("private"),
    SCHOOL ("school");

    public final String string;

    Tags(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
