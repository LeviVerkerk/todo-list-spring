package org.leviverkerk.todolist.service;

import lombok.extern.slf4j.Slf4j;
import org.leviverkerk.todolist.repository.TodoItemRepository;
import org.leviverkerk.todolist.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TodoItemServiceImpl implements TodoItemService {

    private TodoItemRepository todoItemRepository;

    @Autowired
    public TodoItemServiceImpl(TodoItemRepository todoItemRepository) {
        this.todoItemRepository = todoItemRepository;
    }

    @Override
    @Transactional
    public void addItem(TodoItem toAdd) {
        todoItemRepository.addItem(toAdd);
    }

    @Override
    @Transactional
    public void removeItem(int id) {
        todoItemRepository.removeItem(id);
    }

    @Override
    @Transactional
    public TodoItem getItem(int id) {
        return todoItemRepository.getItem(id);
    }

    @Override
    @Transactional
    public void updateItem(TodoItem toUpdate) {
        todoItemRepository.updateItem(toUpdate);
    }

    @Override
    @Transactional
    public List<TodoItem> getItems() {

        List<TodoItem> items = todoItemRepository.getItems();

        log.info(items.toString());

        Collections.sort(items, new Comparator<TodoItem>() {
            @Override
            public int compare(TodoItem o1, TodoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });

        return items;
    }
}
