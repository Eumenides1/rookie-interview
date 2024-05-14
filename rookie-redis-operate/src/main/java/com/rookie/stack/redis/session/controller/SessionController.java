package com.rookie.stack.redis.session.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @author eumenides
 * @description
 * @date 2024/5/14
 */
@RestController
@RequestMapping("/session")
public class SessionController {

    @GetMapping("/set")
    public String setSessionAttribute(HttpSession session) {
        session.setAttribute("testAttribute", "testValue");
        return "Attribute set in session";
    }

    @GetMapping("/get")
    public String getSessionAttribute(HttpSession session) {
        return "Attribute from session: " + session.getAttribute("testAttribute");
    }
}

