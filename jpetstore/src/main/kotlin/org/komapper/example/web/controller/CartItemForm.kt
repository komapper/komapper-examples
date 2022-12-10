package org.komapper.example.web.controller

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.NotNull

class CartItemForm {
    @Max(99)
    @NotNull
    var quantity: Int? = null
}
