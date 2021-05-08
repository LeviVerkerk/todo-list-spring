package org.leviverkerk.todolist.dao;

import org.leviverkerk.todolist.entity.TodoItem;

import java.util.List;

public interface TodoItemDAO {

    void addItem(TodoItem toAdd);

    void removeItem(int id);

    TodoItem getItem(int id);

    void updateItem(TodoItem toUpdate);

    List<TodoItem> getItems();

}
