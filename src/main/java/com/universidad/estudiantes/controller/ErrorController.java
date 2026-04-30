package com.universidad.estudiantes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorController {

    @RequestMapping(value = "/error/403", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String accesoDenegado() {
        return "error/403";
    }
}
