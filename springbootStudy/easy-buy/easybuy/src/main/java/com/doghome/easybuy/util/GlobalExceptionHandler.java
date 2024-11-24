package com.doghome.easybuy.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    public static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = {RuntimeException.class})

    public AjaxResult ExceptionHandler(RuntimeException e) {
        log.error(e.getMessage(), e);
        return AjaxResult.error(500, e.getMessage());
    }
}
