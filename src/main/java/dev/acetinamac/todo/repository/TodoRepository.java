package dev.acetinamac.todo.repository;

import dev.acetinamac.todo.domain.Todo;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class TodoRepository implements CommonRepository<Todo> {
    private Map<String,Todo> todos = new HashMap<>();
    @Override
    public Todo save(Todo domain) {
        Todo result = todos.get(domain.getId());
        if (result != null) {
            result.setModified(LocalDateTime.now());
            result.setDescription(domain.getDescription());
            result.setCompleted(domain.isCompleted());
            domain = result;
        }
        todos.put(domain.getId(), domain);
        return todos.get(domain.getId());
    }

    @Override
    public Iterable<Todo> save(Collection<Todo> domains) {
        domains.forEach(this::save);
        return findAll();
    }

    @Override
    public void delete(Todo domain) {
        todos.remove(domain.getId());
    }

    @Override
    public Todo findById(String id) {
        return todos.get(id);
    }

    @Override
    public Iterable<Todo> findAll() {
        return todos.entrySet().stream().sorted(entryComparator).map(Map.Entry::getValue).collect(Collectors.toList());
    }

    private Comparator<Map.Entry<String,Todo>> entryComparator = (Map.Entry<String, Todo> o1, Map.Entry<String, Todo> o2) -> {
        return o1.getValue().getCreated().compareTo(o2.getValue().getCreated());
    };
}
