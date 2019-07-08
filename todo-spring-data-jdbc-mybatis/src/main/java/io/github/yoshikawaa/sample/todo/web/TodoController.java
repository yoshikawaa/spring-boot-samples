package io.github.yoshikawaa.sample.todo.web;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.yoshikawaa.sample.todo.domain.Todo;
import io.github.yoshikawaa.sample.todo.exception.BusinessException;
import io.github.yoshikawaa.sample.todo.service.TodoService;
import io.github.yoshikawaa.sample.todo.web.TodoForm.TodoCreate;
import io.github.yoshikawaa.sample.todo.web.TodoForm.TodoDelete;
import io.github.yoshikawaa.sample.todo.web.TodoForm.TodoFinish;

@Controller
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @Autowired
    ModelMapper modelMapper;

    @GetMapping("list")
    public String list(TodoForm form, Model model) {
        Iterable<Todo> todos = todoService.findAll();
        model.addAttribute("todos", todos);
        return "list";
    }

    @PostMapping("create")
    public String create(@Validated(TodoCreate.class) TodoForm form, BindingResult bindingResult, Model model,
            RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(form, model);
        }

        Todo todo = modelMapper.map(form, Todo.class);
        try {
            todoService.create(todo);
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return list(form, model);
        }

        attributes.addFlashAttribute("successMessage", "Created successfully!");
        return "redirect:/list";
    }

    @PostMapping("finish")
    public String finish(@Validated({ TodoFinish.class }) TodoForm form, BindingResult bindingResult, Model model,
            RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(form, model);
        }

        try {
            todoService.finish(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return list(form, model);
        }

        attributes.addFlashAttribute("successMessage", "Finished successfully!");
        return "redirect:/list";
    }

    @PostMapping("delete")
    public String delete(@Validated({ TodoDelete.class }) TodoForm form, BindingResult bindingResult, Model model,
            RedirectAttributes attributes) {

        if (bindingResult.hasErrors()) {
            return list(form, model);
        }

        try {
            todoService.delete(form.getTodoId());
        } catch (BusinessException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return list(form, model);
        }

        attributes.addFlashAttribute("successMessage", "Deleted successfully!");
        return "redirect:/list";
    }

}
