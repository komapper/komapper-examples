package org.komapper.example.web.controller

import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

class CartForm {
    @Valid
    @NotNull
    val items: MutableMap<String, CartItemForm> = mutableMapOf()
}
