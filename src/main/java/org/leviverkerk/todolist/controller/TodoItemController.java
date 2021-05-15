package org.leviverkerk.todolist.controller;

import lombok.extern.slf4j.Slf4j;
import org.leviverkerk.todolist.model.MyUserDetails;
import org.leviverkerk.todolist.model.TodoItem;
import org.leviverkerk.todolist.model.User;
import org.leviverkerk.todolist.service.TodoItemService;
import org.leviverkerk.todolist.util.AttributeNames;
import org.leviverkerk.todolist.util.Mappings;
import org.leviverkerk.todolist.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.time.LocalDate;

@Slf4j
@Controller
public class TodoItemController {

    //  == fields ==
    private final TodoItemService todoItemService;

    //  == constructors ==
    @Autowired
    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    //  == handler methods ==

    //  http://localhost:8080/items
    @GetMapping(Mappings.ITEMS)
    public String items(@RequestParam(required = false, defaultValue = "false") boolean isDeleted, Model model){

        model.addAttribute("isDeleted", isDeleted);

        model.addAttribute("items", todoItemService.getItems());

        model.addAttribute("username",  ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getFirstName() );

        return ViewNames.ITEMS_LIST;
    }

    @GetMapping(Mappings.ADD_ITEM)
    public String addEditItem(@RequestParam(required = false, defaultValue = "-1") int id, Model model) {

        log.info("editing id = {}", id);
        TodoItem todoItem = todoItemService.getItem(id);

        if (todoItem == null) {
            todoItem = new TodoItem("", "", LocalDate.now());
        }

        log.info("[EDITING] TodoItem: {}", todoItem);
        model.addAttribute(AttributeNames.TODO_ITEM, todoItem);

        return ViewNames.ADD_ITEM;
    }

    @PostMapping(Mappings.ADD_ITEM)
    public String processItem(@ModelAttribute(AttributeNames.TODO_ITEM) TodoItem todoItem, @RequestParam(value = "action", required = true) String action) {
        log.info("todoItem from form {}", todoItem);

        if (action.equals("save")){
            if (todoItem.getId() == 0){
                log.info("[ADD_ITEM] Adding Item : {}", todoItem);
                todoItemService.addItem(todoItem);
            } else {
                log.info("[ADD_ITEM] Updating Item : {}", todoItem);
                todoItemService.updateItem(todoItem);
            }
        }
        return "redirect:/" + Mappings.ITEMS;
    }

    @GetMapping(Mappings.DELETE_ITEM)
    public RedirectView deleteItem(@RequestParam int id, Model model) {
        log.info("removing item with id: {}", id);
        todoItemService.removeItem(id);

        RedirectView rv = new RedirectView();

        rv.setUrl("/" + Mappings.ITEMS + "?isDeleted=true");

        return rv;
    }

    @GetMapping(Mappings.SHOW_ITEM)
    public String viewItem(@RequestParam int id, Model model) {
        log.info("viewing details from item with id : {}", id );
        model.addAttribute(AttributeNames.TODO_ITEM, todoItemService.getItem(id));
        return ViewNames.SHOW_ITEM;
    }

    @GetMapping(Mappings.LOGIN)
    public String login() {
        return ViewNames.LOGIN;
    }
}
