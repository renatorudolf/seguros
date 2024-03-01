package br.com.itau.seguros.core.handler;

import br.com.itau.seguros.core.logger.LogHelper;
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
import java.util.logging.Level;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErroResponse> handleResponseStatusException(ResponseStatusException exception) {
        var erroResponse = new ErroResponse(String.valueOf(exception.getBody().getStatus()), exception.getBody().getDetail());
        LogHelper.printLog(Level.INFO, exception.getBody().getDetail(), null, ControllerExceptionHandler.class);
        return ResponseEntity.status(exception.getBody().getStatus()).body(erroResponse);
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        List<ErroResponse> dto = new ArrayList<>();
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        fieldErrors.forEach(e -> {
            String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            ErroResponse erroResponse = new ErroResponse(e.getField(), message);
            LogHelper.printLog(Level.INFO, message, null, ControllerExceptionHandler.class);
            dto.add(erroResponse);
        });
        return dto;
    }

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handle(Exception exception){
        var descricao = "Campo categoria invalido";
        var erroResponse = new ErroResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()), descricao);
        LogHelper.printLog(Level.INFO, descricao, null, ControllerExceptionHandler.class);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erroResponse);
    }
}
