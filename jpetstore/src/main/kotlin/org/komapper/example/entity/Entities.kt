package org.komapper.example.entity

import java.io.Serializable
import java.math.BigDecimal

data class Account(
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val status: String?,
    val address1: String,
    val address2: String?,
    val city: String,
    val state: String,
    val zip: String,
    val country: String,
    val phone: String,
) : Serializable

data class BannerData(
    val favouriteCategoryId: String,
    val bannerName: String?,
) : Serializable

data class Category(
    val categoryId: String,
    val name: String?,
    val description: String?,
) : Serializable

data class Inventory(
    val itemId: String,
    val quantity: Int,
) : Serializable

data class Item(
    val itemId: String,
    val productId: String,
    val listPrice: BigDecimal,
    val unitCost: BigDecimal,
    val supplierId: Int?,
    val status: String?,
    val attribute1: String?,
    val attribute2: String?,
    val attribute3: String?,
    val attribute4: String?,
    val attribute5: String?,
) : Serializable

data class LineItem(
    val orderId: Int,
    val lineNumber: Int,
    val itemId: String,
    val quantity: Int,
    val unitPrice: BigDecimal,
) : Serializable {
    val total: BigDecimal
        get() = unitPrice.multiply(BigDecimal(quantity))
}

data class Order(
    val orderId: Int = 0,
    val username: String,
    val orderDate: java.time.LocalDateTime,
    val shipAddress1: String,
    val shipAddress2: String?,
    val shipCity: String,
    val shipState: String,
    val shipZip: String,
    val shipCountry: String,
    val billAddress1: String,
    val billAddress2: String?,
    val billCity: String,
    val billState: String,
    val billZip: String,
    val billCountry: String,
    val courier: String,
    val totalPrice: BigDecimal,
    val billToFirstName: String,
    val billToLastName: String,
    val shipToFirstName: String,
    val shipToLastName: String,
    val creditCard: String,
    val expiryDate: String,
    val cardType: String,
    val locale: String,
) : Serializable

data class OrderStatus(
    val orderId: Int,
    val lineNumber: Int,
    val timestamp: java.time.LocalDate,
    val status: String,
) : Serializable

data class Product(
    val productId: String,
    val categoryId: String,
    val name: String?,
    val description: String?,
) : Serializable

data class Profile(
    val username: String,
    val languagePreference: String,
    val favouriteCategoryId: String?,
    val listOption: Int?,
    val bannerOption: Int?,
) : Serializable

data class SignOn(
    val username: String,
    val password: String,
) : Serializable
