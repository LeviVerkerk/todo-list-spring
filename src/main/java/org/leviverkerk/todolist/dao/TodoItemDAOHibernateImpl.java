package org.leviverkerk.todolist.dao;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.leviverkerk.todolist.entity.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@Slf4j
public class TodoItemDAOHibernateImpl implements TodoItemDAO{

    private EntityManager entityManager;

    @Autowired
    public TodoItemDAOHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void addItem(@NotNull TodoItem toAdd) {
        try (Session session = entityManager.unwrap(Session.class)) {
            log.debug("[TodoItemService] Adding new item : {}", toAdd);
            session.save(toAdd);
            log.info("[TodoItemService] Saved : {}", toAdd);
        }
    }

    @Override
    @Transactional
    public void removeItem(int id) {
        try (Session session = entityManager.unwrap(Session.class)) {
            log.debug("[TodoItemService] Retrieving todoItem with id : {}", id);
            TodoItem todoItem = session.get(TodoItem.class, id);

            log.debug("[TodoItemService] Removing todoItem : {}", todoItem);
            session.delete(todoItem);
            log.info("[TodoItemService] Removed todoItem : {}", todoItem);
        }
    }

    @Override
    @Transactional
    public TodoItem getItem(int id) {
        TodoItem item = null;
        try (Session session = entityManager.unwrap(Session.class)) {
            log.debug("[TodoItemService] Getting todoItem with id : {}", id);
            item = session.get(TodoItem.class, id);

            if (item == null) {
                log.warn("[TodoItemService] todoItem not found with id : {}", id);
            }
        }

        return item;
    }

    @Override
    @Transactional
    public List<TodoItem> getItems() {
        try (Session session = entityManager.unwrap(Session.class)){
            List<TodoItem> output = session.createQuery("from TodoItem ", TodoItem.class).getResultList();
            return output;
        }
    }

    @Override
    @Transactional
    public void updateItem(TodoItem toUpdate) {
        try (Session session = entityManager.unwrap(Session.class)) {
            log.info("[TodoItemService] updating todoItem to : {}", toUpdate);
            session.update(toUpdate);
        }
    }
}
