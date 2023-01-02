package org.komapper.example.web.controller

import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class PasswordValidator : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return AccountAddForm::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val form = target as AccountAddForm
        val password = form.password
        val repeatedPassword = form.repeatedPassword
        if (password == null) {
            return
        }
        if (password != repeatedPassword) {
            errors.rejectValue(
                "password",
                "PasswordValidator.addAccountForm.password",
                "password and repeated password must be same.",
            )
        }
    }
}
