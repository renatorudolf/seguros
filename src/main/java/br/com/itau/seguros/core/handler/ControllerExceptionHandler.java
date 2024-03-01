package br.com.itau.seguros.core.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private MessageSource messageSource;
    private static final Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> handleResponseStatusException(ResponseStatusException exception) {
        log.warn(exception.getReason());
        return ResponseEntity.status(exception.getBody().getStatus()).body(new ErroResponse(String.valueOf(exception.getBody().getStatus()), exception.getBody().getDetail()));
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<ErroResponse> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroResponse erroDeFormularioDTO = new ErroResponse(e.getField(), message);
            dto.add(erroDeFormularioDTO);
        });
        return dto;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handle(Exception exception){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErroResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Campo categoria invalido"));
    }
}
