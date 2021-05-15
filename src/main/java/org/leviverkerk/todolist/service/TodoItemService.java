package org.leviverkerk.todolist.service;

import org.leviverkerk.todolist.model.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TodoItemService {
    
    void addItem(TodoItem toAdd);

    void removeItem(int id);

    TodoItem getItem(int id);

    void updateItem(TodoItem toUpdate);

    List<TodoItem> getItems(String sortField, String sortDir);

    Page<TodoItem> findPaginated(Pageable pageable, String sortField, String sortDir);
}
