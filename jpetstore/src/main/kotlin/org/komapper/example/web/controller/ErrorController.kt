package org.komapper.example.web.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping

@Controller
class ErrorController {
    @RequestMapping("/404")
    fun notFound(): String {
        return "404"
    }
}
