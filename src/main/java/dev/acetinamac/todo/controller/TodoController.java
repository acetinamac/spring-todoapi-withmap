package dev.acetinamac.todo.controller;

import dev.acetinamac.todo.domain.Todo;
import dev.acetinamac.todo.repository.CommonRepository;
import dev.acetinamac.todo.validation.TodoValidationError;
import dev.acetinamac.todo.validation.TodoValidationErrorBuilder;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api")
public class TodoController {
    private CommonRepository<Todo> repository;

    @Autowired
    public TodoController(CommonRepository<Todo> repository) {
        this.repository = repository;
    }
    @GetMapping("/todo")
    public ResponseEntity<Iterable<Todo>> getTodos() {
        return ResponseEntity.ok(repository.findAll());
    }
    @GetMapping("/todo/{id}")
    public ResponseEntity<Todo> getTodoById(@PathVariable String id) {
        return ResponseEntity.ok(repository.findById(id));
    }
    @PatchMapping("/Todo/{id}")
    public ResponseEntity<Todo> setCompleted(@PathVariable String id) {
        Todo result = repository.findById(id);
        result.setCompleted(true);
        repository.save(result);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.ok().header("Location", location.toString()).build();
    }

    @RequestMapping(value="/todo", method = {RequestMethod.POST, RequestMethod.PUT})
    public ResponseEntity<?> createToDo(@Valid @RequestBody Todo todo, Errors errors)
    {
        if (errors.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(TodoValidationErrorBuilder.fromBindingErrors(errors));
        }

        Todo result = repository.save(todo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(location).build();
    }
    @DeleteMapping("/todo")
    public ResponseEntity<Todo> deleteTodo(@RequestBody Todo todo) {
        repository.delete(todo);
        return ResponseEntity.noContent().build();
    }
    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public TodoValidationError handlerException(Exception exception) {
        return new TodoValidationError(exception.getMessage());
    }
}
