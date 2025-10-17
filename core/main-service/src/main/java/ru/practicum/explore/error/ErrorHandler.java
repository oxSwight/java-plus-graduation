package ru.practicum.explore.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import ru.practicum.explore.exception.*;

@RestControllerAdvice
@Slf4j
public class ErrorHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidExceptions(MethodArgumentNotValidException e) {
        log.debug("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.debug("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({OperationFailedException.class, ConflictDataException.class, DuplicateException.class})
    public ErrorResponse handleConflictExceptions(RuntimeException e) {
        log.debug("Получен статус 409 CONFLICT {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({InvalidDateTimeException.class, NotFoundRecordInBDException.class,
            InvalidSortException.class, ValidationException.class})
    public ErrorResponse handleBadRequestExceptions(RuntimeException e) {
        log.debug("Получен статус 400 BAD_REQUEST {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFound(NotFoundException e) {
        log.debug("Получен статус 404 NOT_FOUND {}", e.getMessage(), e);
        return new ErrorResponse(e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorResponse handleAllExceptions(Exception e) {
        log.error("Получен статус 500 INTERNAL_SERVER_ERROR {}", e.getMessage(), e);
        return new ErrorResponse("Ой, у нас что-то сломалось :)");
    }
}
