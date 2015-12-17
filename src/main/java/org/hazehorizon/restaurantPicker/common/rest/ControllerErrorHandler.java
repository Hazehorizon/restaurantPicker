package org.hazehorizon.restaurantPicker.common.rest;

import org.hazehorizon.restaurantPicker.common.service.ServiceException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice(annotations = RestController.class)
public class ControllerErrorHandler extends AbstractController {

	@ExceptionHandler(ServiceException.class)
    public ResponseEntity<ResponseWrapper<Void>> onNotFoundException(final ServiceException e)
    {
        logger.warn("Service error", e);
        return new ResponseEntity<>(
        		createErrorResponse(ResponseWrapper.GENERAL_ERROR, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseWrapper<Void>> onDateException(final DataIntegrityViolationException e)
    {
        logger.warn("Data error", e);
        return new ResponseEntity<>(
        		createErrorResponse(ResponseWrapper.GENERAL_ERROR, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
	
	@ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseWrapper<Void>> onRuntimeException(final RuntimeException e)
    {
        logger.error("Unexpected runtime error", e);
        return new ResponseEntity<>(
        		createErrorResponse(ResponseWrapper.GENERAL_ERROR, e.getMessage()), HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseWrapper<Void>> onValidationException(MethodArgumentNotValidException e)
    {
    	logger.debug("Validation error", e);
        String[] messages = e.getBindingResult().getAllErrors().stream().map(
                error -> {
                    if (error instanceof FieldError) {
                        return String.format("Field %s is wrong, %s", ((FieldError)error).getField(), error.getDefaultMessage());
                    }
                    else {
                        return error.getDefaultMessage();
                    }
                })
                .toArray(size -> new String[size]);
        return new ResponseEntity<>(createErrorResponse(ResponseWrapper.VALIDATION_ERROR, messages), HttpStatus.BAD_REQUEST);
    }
}
