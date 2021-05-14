package org.leviverkerk.todolist.service;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.leviverkerk.todolist.dao.TodoItemDAO;
import org.leviverkerk.todolist.entity.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
public class TodoItemServiceImpl implements TodoItemService {

    private TodoItemDAO todoItemDAO;

    @Autowired
    public TodoItemServiceImpl(TodoItemDAO todoItemDAO) {
        this.todoItemDAO = todoItemDAO;
    }

    @Override
    @Transactional
    public void addItem(TodoItem toAdd) {
        todoItemDAO.addItem(toAdd);
    }

    @Override
    @Transactional
    public void removeItem(int id) {
        todoItemDAO.removeItem(id);
    }

    @Override
    @Transactional
    public TodoItem getItem(int id) {
        return todoItemDAO.getItem(id);
    }

    @Override
    @Transactional
    public void updateItem(TodoItem toUpdate) {
        todoItemDAO.updateItem(toUpdate);
    }

    @Override
    @Transactional
    public List<TodoItem> getItems() {

        List<TodoItem> items = todoItemDAO.getItems();

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
