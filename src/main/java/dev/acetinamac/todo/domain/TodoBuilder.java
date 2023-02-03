package dev.acetinamac.todo.domain;

public class TodoBuilder {
    private static TodoBuilder instance = new TodoBuilder();
    private String id = null;
    private String description = "";

    public TodoBuilder() {
    }
    public static TodoBuilder create() {
        return instance;
    }
    public TodoBuilder withDescription(String description) {
        this.description = description;
        return instance;
    }

    public TodoBuilder withId(String id) {
        this.id = id;
        return instance;
    }

    public Todo build() {
        Todo result = new Todo(this.description);
        if (this.id != null)
            result.setId(this.id);
        return result;
    }
}
