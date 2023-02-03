package dev.acetinamac.todo.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

public class TodoValidationErrorBuilder {
    public static TodoValidationError fromBindingErrors(Errors errors) {
        TodoValidationError error = new TodoValidationError("Validation failed." + errors.getErrorCount() + " error(s)");
        for (ObjectError objectError: errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }
}
