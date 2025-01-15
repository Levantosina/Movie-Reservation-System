package com.movie.exceptions;



import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author DMITRII LEVKIN on 13/10/2024
 * @project MovieReservationSystem
 */
@ControllerAdvice
public class DefaultExceptionHandler {


//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<ApiError> handlerException (ResourceNotFoundException e, HttpServletRequest request){
//    ApiError apiError =  new ApiError(
//                request.getRequestURI(),
//                e.getMessage(),
//                HttpStatus.NOT_FOUND.value(),
//                LocalDateTime.now()
//        );
//
//    return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
//    }

//    @ExceptionHandler(InsufficientAuthenticationException.class)
//    public ResponseEntity<ApiError> handlerException (InsufficientAuthenticationException e, HttpServletRequest request){
//        ApiError apiError =  new ApiError(
//                request.getRequestURI(),
//                e.getMessage(),
//                HttpStatus.FORBIDDEN.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.FORBIDDEN);
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ApiError> handlerException (Exception e, HttpServletRequest request){
//        ApiError apiError =  new ApiError(
//                request.getRequestURI(),
//                e.getMessage(),
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public ResponseEntity<ApiError> handlerException (BadCredentialsException e, HttpServletRequest request){
//        ApiError apiError =  new ApiError(
//                request.getRequestURI(),
//                e.getMessage(),
//                HttpStatus.UNAUTHORIZED.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.UNAUTHORIZED);
//    }
//
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach(error -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(InvalidRoleException.class)
//    public ResponseEntity<ApiError> handleInvalidRoleException(InvalidRoleException e, HttpServletRequest request) {
//        ApiError apiError = new ApiError(
//                request.getRequestURI(),
//                e.getMessage(),
//                HttpStatus.BAD_REQUEST.value(),
//                LocalDateTime.now()
//        );
//
//        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResourceException(DuplicateResourceException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RequestValidationException.class)
    public ResponseEntity<String> handleSeatRequestValidationException(RequestValidationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
    @ExceptionHandler(AlreadyOccupiedException.class)
    public ResponseEntity<String> handleAlreadyOccupiedException(AlreadyOccupiedException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
    @ExceptionHandler(HandleRuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(HandleRuntimeException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }





    ///ADD NEW!!!!!!!!!!!!!!!!!!!!

}
