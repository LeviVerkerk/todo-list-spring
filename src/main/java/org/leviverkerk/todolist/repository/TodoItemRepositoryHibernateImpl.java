package org.leviverkerk.todolist.repository;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.leviverkerk.todolist.model.MyUserDetails;
import org.leviverkerk.todolist.model.Tags;
import org.leviverkerk.todolist.model.TodoItem;
import org.leviverkerk.todolist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
public class TodoItemRepositoryHibernateImpl implements TodoItemRepository {

    private EntityManager entityManager;

    @Autowired
    public TodoItemRepositoryHibernateImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void addItem(@NotNull TodoItem toAdd) {
        try (Session session = entityManager.unwrap(Session.class)) {
            log.debug("[TodoItemService] Getting current user..");
            User user = session.get(User.class, ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
            log.debug("[TodoItemService] Current user is : {}", user);
            toAdd.setUser(user);
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
    public List<Tags> getTags(int id) {

        List<Tags> tags;
        TodoItem item = null;

        try (Session session = entityManager.unwrap(Session.class)) {
            log.debug("[TodoItemService] Getting todoItem with id : {}", id);
            item = session.get(TodoItem.class, id);

            if (item != null) {
                tags = item.getTags();
            } else {
                tags = Collections.emptyList();
            }

        }
        return tags;
    }

    @Override
    @Transactional
    public void addTag(TodoItem item, Tags tag) {

        try (Session session = entityManager.unwrap(Session.class)) {
            TodoItem savedItem = session.get(TodoItem.class, item.getId());

            if (savedItem != null){
                savedItem.addTag(tag);
                session.update(savedItem);
            } else {
                throw new NullPointerException("Item : " + item + " not found!");
            }

        }

    }

    @Override
    @Transactional
    public List<TodoItem> getItems() {
        try (Session session = entityManager.unwrap(Session.class)) {

            User user = session.get(User.class, ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
            if (user != null) {
                return user.getItems();
            }
            return new LinkedList<>();
        }
    }

    @Override
    @Transactional
    public void updateItem(TodoItem toUpdate) {
        try (Session session = entityManager.unwrap(Session.class)) {
            User user = session.get(User.class, ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
            toUpdate.setUser(user);
            log.info("[TodoItemService] updating todoItem to : {}", toUpdate);
            session.update(toUpdate);
        }
    }
}
