package org.komapper.example.web.controller

import javax.validation.constraints.Max
import javax.validation.constraints.NotNull

class CartItemForm {
    @Max(99)
    @NotNull
    var quantity: Int? = null
}
