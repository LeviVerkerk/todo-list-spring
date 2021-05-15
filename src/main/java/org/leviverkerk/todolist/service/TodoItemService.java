package org.leviverkerk.todolist.service;

import org.leviverkerk.todolist.model.TodoItem;

import java.util.List;

public interface TodoItemService {
    
    void addItem(TodoItem toAdd);

    void removeItem(int id);

    TodoItem getItem(int id);

    void updateItem(TodoItem toUpdate);

    List<TodoItem> getItems();
}
