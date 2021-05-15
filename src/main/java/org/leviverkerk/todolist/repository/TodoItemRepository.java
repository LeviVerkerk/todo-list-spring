package org.leviverkerk.todolist.repository;

import org.leviverkerk.todolist.model.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TodoItemRepository {

    void addItem(TodoItem toAdd);

    void removeItem(int id);

    TodoItem getItem(int id);

    void updateItem(TodoItem toUpdate);

    List<TodoItem> getItems();

}
