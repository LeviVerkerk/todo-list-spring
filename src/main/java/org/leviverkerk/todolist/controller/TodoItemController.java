package org.leviverkerk.todolist.controller;

import lombok.extern.slf4j.Slf4j;
import org.leviverkerk.todolist.model.MyUserDetails;
import org.leviverkerk.todolist.model.Tags;
import org.leviverkerk.todolist.model.TodoItem;
import org.leviverkerk.todolist.model.User;
import org.leviverkerk.todolist.model.UserDto;
import org.leviverkerk.todolist.service.IUserService;
import org.leviverkerk.todolist.service.TodoItemService;
import org.leviverkerk.todolist.util.AttributeNames;
import org.leviverkerk.todolist.util.Mappings;
import org.leviverkerk.todolist.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class TodoItemController {

    //  == fields ==
    private final TodoItemService todoItemService;
    private final IUserService userService;

    //  == constructors ==
    @Autowired
    public TodoItemController(TodoItemService todoItemService, IUserService userService) {
        this.todoItemService = todoItemService;
        this.userService = userService;
    }

    //  == handler methods ==

    //  http://localhost:8080/items
    @RequestMapping(value = Mappings.ITEMS, method = RequestMethod.GET)
    public String items(@RequestParam("page") Optional<Integer> page,
                        @RequestParam("size") Optional<Integer> size,
                        @RequestParam("sort-field") Optional<String> field,
                        @RequestParam("sort-dir") Optional<String> dir,
                        @RequestParam(required = false, defaultValue = "false") boolean isDeleted,
                        Model model){

        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);

        String sortField = field.orElse("deadline");
        String sortDir = dir.orElse("asc");

        log.info("Requesting todoItemPage with sortField : {} and sortDir : {}", sortField, sortDir);

        Page<TodoItem> todoItemPage = todoItemService.findPaginated(PageRequest.of(currentPage - 1, pageSize), sortField, sortDir);

        model.addAttribute("isDeleted", isDeleted);

        model.addAttribute("itemPage", todoItemPage);

//        model.addAttribute("username",  userService.getCurrentUser().getFirstName() );

        model.addAttribute("username", "test");

        model.addAttribute("currentPage", currentPage);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);

        int totalPages = todoItemPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        return ViewNames.ITEMS_LIST;
    }

    @GetMapping(Mappings.ADD_ITEM)
    public String addEditItem(@RequestParam(required = false, defaultValue = "-1") int id, Model model) {

        log.info("editing id = {}", id);
        TodoItem todoItem = todoItemService.getItem(id);

        if (todoItem == null) {
            todoItem = new TodoItem("", "", LocalDate.now(), new Tags[0]);
        }

        log.info("[EDITING] TodoItem: {}", todoItem);
        model.addAttribute(AttributeNames.TODO_ITEM, todoItem);
        model.addAttribute("tags", Tags.values());

        return ViewNames.ADD_ITEM;
    }

    @PostMapping(Mappings.ADD_ITEM)
    public String processItem(@ModelAttribute(AttributeNames.TODO_ITEM) TodoItem todoItem,
                              @ModelAttribute("tags") Tags[] tags,
                              @RequestParam(value = "action", required = true) String action) {
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

    @GetMapping(Mappings.REGISTER)
    public String register(WebRequest request, Model model) {
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return ViewNames.REGISTER;
    }

    @PostMapping(Mappings.REGISTER)
    public ModelAndView register (
            @ModelAttribute("user") @Valid UserDto userDto,
            HttpServletRequest request,
            Errors errors) {

        try {
            User registered = userService.registerNewUserAccount(userDto);
        } catch (Exception e){
            e.printStackTrace();
        }

        return new ModelAndView(ViewNames.LOGIN, "user", userDto);
    }
}
