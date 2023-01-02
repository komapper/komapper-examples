package org.komapper.example.web.controller

import org.komapper.example.entity.Account
import org.komapper.example.entity.Profile
import org.komapper.example.entity.SignOn
import org.komapper.example.model.AccountAggregate
import org.komapper.example.service.AccountService
import org.komapper.example.web.Constants
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.User
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Controller
import org.springframework.transaction.annotation.Transactional
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.InitBinder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/account")
@Transactional
class AccountController(
    private val accountService: AccountService,
    private val passwordEncoder: PasswordEncoder,
    private val passwordValidator: PasswordValidator,
) {

    @InitBinder("accountAddForm")
    fun initBinder(binder: WebDataBinder) {
        binder.addValidators(passwordValidator)
    }

    @GetMapping("add")
    fun add(model: Model): String {
        return modelAndViewForAdd(model, AccountAddForm())
    }

    @PostMapping("add")
    fun add(
        @Validated accountForm: AccountAddForm,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        if (bindingResult.hasErrors()) {
            return modelAndViewForAdd(model, accountForm)
        }
        val userName = accountForm.username!!
        val account = createAccount(userName, accountForm)
        val profile = createProfile(userName, accountForm)
        val signOn = createSingOn(userName, accountForm)
        accountService.insertAccount(account)
        accountService.insertProfile(profile)
        accountService.insertSignOn(signOn)
        return "redirect:/signin"
    }

    private fun modelAndViewForAdd(model: Model, accountForm: AccountAddForm): String {
        model.addAttribute(accountForm)
        model.addAttribute("languageList", Constants.LANGUAGE_LIST)
        model.addAttribute("categoryList", Constants.CATEGORY_LIST)
        return "account/add"
    }

    @GetMapping("/edit")
    fun edit(model: Model, @AuthenticationPrincipal user: User): String {
        val aggregate = accountService.getAccountAggregate(user.username)
        val accountForm = createAccountEditForm(aggregate)
        return modelAndViewForEdit(model, accountForm, user)
    }

    @PostMapping("/edit")
    fun edit(
        @Validated accountForm: AccountEditForm,
        bindingResult: BindingResult,
        model: Model,
        @AuthenticationPrincipal user: User,
    ): String {
        if (bindingResult.hasErrors()) {
            return modelAndViewForEdit(model, accountForm, user)
        }
        val account = createAccount(user.username, accountForm)
        val profile = createProfile(user.username, accountForm)
        accountService.updateAccount(account)
        accountService.updateProfile(profile)
        return "redirect:/"
    }

    private fun modelAndViewForEdit(model: Model, accountForm: AccountEditForm, user: User): String {
        model.addAttribute(accountForm)
        model.addAttribute("username", user.username)
        model.addAttribute("languageList", Constants.LANGUAGE_LIST)
        model.addAttribute("categoryList", Constants.CATEGORY_LIST)
        return "account/edit"
    }

    private fun createAccount(username: String, form: AccountForm): Account {
        return Account(
            username = username,
            email = form.email!!,
            firstName = form.firstName!!,
            lastName = form.lastName!!,
            status = null,
            address1 = form.address1!!,
            address2 = form.address2,
            city = form.city!!,
            state = form.state!!,
            zip = form.zip!!,
            country = form.country!!,
            phone = form.phone!!,
        )
    }

    private fun createProfile(username: String, form: AccountForm): Profile {
        return Profile(
            username = username,
            languagePreference = form.languagePreference ?: "english",
            favouriteCategoryId = form.favouriteCategoryId,
            listOption = if (form.listOption) 1 else 0,
            bannerOption = if (form.bannerOption) 1 else 0,
        )
    }

    private fun createSingOn(username: String, form: AccountAddForm): SignOn {
        val rawPassword = form.password!!
        val encodedPassword = passwordEncoder.encode(rawPassword)
        return SignOn(username, encodedPassword)
    }

    private fun createAccountEditForm(aggregate: AccountAggregate): AccountEditForm {
        val account = aggregate.account
        val profile = aggregate.profile
        return AccountEditForm().apply {
            email = account.email
            firstName = account.firstName
            lastName = account.lastName
            address1 = account.address1
            address2 = account.address2
            city = account.city
            state = account.state
            zip = account.zip
            country = account.country
            phone = account.phone
            languagePreference = profile.languagePreference
            favouriteCategoryId = profile.favouriteCategoryId
            listOption = profile.listOption == 1
            bannerOption = profile.bannerOption == 1
        }
    }
}
