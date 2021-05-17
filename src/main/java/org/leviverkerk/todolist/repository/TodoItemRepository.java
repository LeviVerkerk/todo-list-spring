package org.leviverkerk.todolist.repository;

import org.leviverkerk.todolist.model.Tags;
import org.leviverkerk.todolist.model.TodoItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface TodoItemRepository {

    void addItem(TodoItem toAdd);

    void removeItem(int id);

    TodoItem getItem(int id);

    List<Tags> getTags(int id);

    void addTag(TodoItem item, Tags tag);

    void updateItem(TodoItem toUpdate);

    List<TodoItem> getItems();

}
