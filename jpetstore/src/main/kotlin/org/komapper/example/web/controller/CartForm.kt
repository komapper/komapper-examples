package org.komapper.example.web.controller

import javax.validation.Valid
import javax.validation.constraints.NotNull

class CartForm {
    @Valid
    @NotNull
    val items: MutableMap<String, CartItemForm> = mutableMapOf()
}
