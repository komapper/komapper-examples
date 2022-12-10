package org.komapper.example.web.controller

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

abstract class AccountForm {
    @NotBlank
    @Size(max = 30)
    var email: String? = null

    @NotBlank
    @Size(max = 30)
    var firstName: String? = null

    @NotBlank
    @Size(max = 30)
    var lastName: String? = null

    @NotBlank
    @Size(max = 30)
    var address1: String? = null

    @NotBlank
    @Size(max = 30)
    var address2: String? = null

    @NotBlank
    @Size(max = 30)
    var city: String? = null

    @NotBlank
    @Size(max = 30)
    var state: String? = null

    @NotBlank
    @Size(max = 20)
    var zip: String? = null

    @NotBlank
    @Size(max = 20)
    var country: String? = null

    @NotBlank
    @Size(max = 20)
    var phone: String? = null

    @NotBlank
    @Size(max = 30)
    var favouriteCategoryId: String? = null

    @NotBlank
    @Size(max = 30)
    var languagePreference: String? = null
    var listOption = false
    var bannerOption = false
}

class AccountAddForm : AccountForm() {
    @NotBlank
    @Size(max = 20)
    var username: String? = null

    @NotBlank
    @Size(min = 8)
    var password: String? = null

    @NotBlank
    var repeatedPassword: String? = null
}

class AccountEditForm : AccountForm()
