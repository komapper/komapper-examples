package org.komapper.example.web.controller

import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/signin")
@Transactional
class SigninController {
    @GetMapping
    fun signin(): String {
        return "signin/signin"
    }
}
