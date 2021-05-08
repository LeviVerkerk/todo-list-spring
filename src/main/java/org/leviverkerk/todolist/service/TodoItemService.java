package org.leviverkerk.todolist.service;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.leviverkerk.todolist.entity.TodoItem;

import java.util.List;

public interface TodoItemService {
    
    void addItem(TodoItem toAdd);

    void removeItem(int id);

    TodoItem getItem(int id);

    void updateItem(TodoItem toUpdate);

    List<TodoItem> getItems();
}
