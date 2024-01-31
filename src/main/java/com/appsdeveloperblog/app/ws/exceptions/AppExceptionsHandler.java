package com.appsdeveloperblog.app.ws.exceptions;

import com.appsdeveloperblog.app.ws.ui.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

//@ControllerAdvice allows you to handle exceptions across the whole application, not just to an individual controller.
@ControllerAdvice
public class AppExceptionsHandler
{
    //let this method to handles specific exception type (class) by passing in list of value
    //this method only handles UserServiceException:
    @ExceptionHandler(value = {UserServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(UserServiceException ex,
                                                             WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //this method only handles other undefined Exceptions except form above already defined Exceptions:
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleUserServiceException(Exception ex,
                                                             WebRequest request)
    {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
