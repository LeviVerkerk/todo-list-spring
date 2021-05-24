package org.leviverkerk.todolist.service;

import lombok.extern.slf4j.Slf4j;
import org.leviverkerk.todolist.repository.TodoItemRepository;
import org.leviverkerk.todolist.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<TodoItem> getItems(String sortField, String sortDir) {

        List<TodoItem> items = todoItemRepository.getItems();

        log.info(items.toString());

        switch (sortField) {
            case "title":
                if (sortDir.equalsIgnoreCase("asc")){
                    items.sort(Comparator.comparing(TodoItem::getTitle));
                }
                else {
                    items.sort(Comparator.comparing(TodoItem::getTitle).reversed());
                }
                break;
            case "deadline":
                if (sortDir.equalsIgnoreCase("asc")){
                    items.sort(Comparator.comparing(TodoItem::getDeadline));
                }
                else {
                    items.sort(Comparator.comparing(TodoItem::getDeadline).reversed());
                }
                break;
            default:
                items.sort(Comparator.comparing(TodoItem::getDeadline));
                break;
        }


        return items;
    }

    public Page<TodoItem> findPaginated(Pageable pageable, String sortField, String sortDir) {

        List<TodoItem> items = getItems(sortField, sortDir);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<TodoItem> list = new ArrayList<>();

        if (items.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, items.size());
            list = items.subList(startItem, toIndex);
        }

        Pageable page = PageRequest.of(currentPage, pageSize);

        Page<TodoItem> itemPage
                = new PageImpl<>(list, page, items.size());

        return itemPage;
    }
}
