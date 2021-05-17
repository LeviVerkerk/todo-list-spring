package org.leviverkerk.todolist.repository;

import com.sun.istack.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.leviverkerk.todolist.model.MyUserDetails;
import org.leviverkerk.todolist.model.TodoItem;
import org.leviverkerk.todolist.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.LinkedList;
import java.util.List;

@Repository
@Slf4j
public class TodoItemRepositoryHibernateImpl implements TodoItemRepository {

    private EntityManager entityManager;
    private UserRepository userRepository;

    @Autowired
    public TodoItemRepositoryHibernateImpl(EntityManager entityManager, UserRepository userRepository) {
        this.entityManager = entityManager;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public void addItem(@NotNull TodoItem toAdd) {
        try (Session session = entityManager.unwrap(Session.class)) {
            log.debug("[TodoItemService] Getting current user..");
            User user = session.get(User.class, ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId());
            log.debug("[TodoItemService] Current user is : {}", user);
            toAdd.setUser(user );
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

            User currentUser = getCurrentUser();

            int currentId = currentUser == null ? -1 : currentUser.getId();

            User user = session.get(User.class, currentId);
            if (user != null){
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

    public User getCurrentUser() {
        String currentUsername = ((org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();

        return userRepository.getUserByUsername(currentUsername);
    }
}
