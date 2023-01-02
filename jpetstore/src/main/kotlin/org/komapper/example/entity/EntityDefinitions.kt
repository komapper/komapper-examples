package org.komapper.example.entity

import org.komapper.annotation.KomapperColumn
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperSequence
import org.komapper.annotation.KomapperTable

@KomapperEntityDef(Account::class)
@KomapperTable("ACCOUNT")
data class AccountDef(
    @KomapperId
    @KomapperColumn("USERID")
    val username: Nothing,
    @KomapperColumn("EMAIL") val email: Nothing,
    @KomapperColumn("FIRSTNAME") val firstName: Nothing,
    @KomapperColumn("LASTNAME") val lastName: Nothing,
    @KomapperColumn("STATUS") val status: Nothing,
    @KomapperColumn("ADDR1") val address1: Nothing,
    @KomapperColumn("ADDR2") val address2: Nothing,
    @KomapperColumn("CITY") val city: Nothing,
    @KomapperColumn("STATE") val state: Nothing,
    @KomapperColumn("ZIP") val zip: Nothing,
    @KomapperColumn("COUNTRY") val country: Nothing,
    @KomapperColumn("PHONE") val phone: Nothing,
)

@KomapperEntityDef(BannerData::class)
@KomapperTable("BANNERDATA")
data class BannerDataDef(
    @KomapperId
    @KomapperColumn("FAVCATEGORY")
    val favouriteCategoryId: Nothing,
    @KomapperColumn("BANNERNAME") val bannerName: Nothing,
)

@KomapperEntityDef(Category::class)
@KomapperTable("CATEGORY")
data class CategoryDef(
    @KomapperId
    @KomapperColumn("CATID")
    val categoryId: Nothing,
    @KomapperColumn("NAME") val name: Nothing,
    @KomapperColumn("DESCN") val description: Nothing,
)

@KomapperEntityDef(Inventory::class)
@KomapperTable("INVENTORY")
data class InventoryDef(
    @KomapperId
    @KomapperColumn("ITEMID")
    val itemId: Nothing,
    @KomapperColumn("QTY") val quantity: Nothing,
)

@KomapperEntityDef(Item::class)
@KomapperTable("ITEM")
data class ItemDef(
    @KomapperId
    @KomapperColumn("ITEMID")
    val itemId: Nothing,
    @KomapperColumn("PRODUCTID") val productId: Nothing,
    @KomapperColumn("LISTPRICE") val listPrice: Nothing,
    @KomapperColumn("UNITCOST") val unitCost: Nothing,
    @KomapperColumn("SUPPLIER") val supplierId: Nothing,
    @KomapperColumn("STATUS") val status: Nothing,
    @KomapperColumn("ATTR1") val attribute1: Nothing,
    @KomapperColumn("ATTR2") val attribute2: Nothing,
    @KomapperColumn("ATTR3") val attribute3: Nothing,
    @KomapperColumn("ATTR4") val attribute4: Nothing,
    @KomapperColumn("ATTR5") val attribute5: Nothing,
)

@KomapperEntityDef(LineItem::class)
@KomapperTable("LINEITEM")
data class LineItemDef(
    @KomapperId
    @KomapperColumn("ORDERID")
    val orderId: Nothing,
    @KomapperId
    @KomapperColumn("LINENUM")
    val lineNumber: Nothing,
    @KomapperColumn("ITEMID") val itemId: Nothing,
    @KomapperColumn("QUANTITY") val quantity: Nothing,
    @KomapperColumn("UNITPRICE") val unitPrice: Nothing,
)

@KomapperEntityDef(Order::class)
@KomapperTable("ORDERS")
data class OrderDef(
    @KomapperId
    @KomapperColumn("ORDERID")
    @KomapperSequence("ORDERS_SEQ", startWith = 1001, incrementBy = 10)
    val orderId: Nothing,
    @KomapperColumn("USERID") val username: Nothing,
    @KomapperColumn("ORDERDATE") val orderDate: Nothing,
    @KomapperColumn("SHIPADDR1") val shipAddress1: Nothing,
    @KomapperColumn("SHIPADDR2") val shipAddress2: Nothing,
    @KomapperColumn("SHIPCITY") val shipCity: Nothing,
    @KomapperColumn("SHIPSTATE") val shipState: Nothing,
    @KomapperColumn("SHIPZIP") val shipZip: Nothing,
    @KomapperColumn("SHIPCOUNTRY") val shipCountry: Nothing,
    @KomapperColumn("BILLADDR1") val billAddress1: Nothing,
    @KomapperColumn("BILLADDR2") val billAddress2: Nothing,
    @KomapperColumn("BILLCITY") val billCity: Nothing,
    @KomapperColumn("BILLSTATE") val billState: Nothing,
    @KomapperColumn("BILLZIP") val billZip: Nothing,
    @KomapperColumn("BILLCOUNTRY") val billCountry: Nothing,
    @KomapperColumn("COURIER") val courier: Nothing,
    @KomapperColumn("TOTALPRICE") val totalPrice: Nothing,
    @KomapperColumn("BILLTOFIRSTNAME") val billToFirstName: Nothing,
    @KomapperColumn("BILLTOLASTNAME") val billToLastName: Nothing,
    @KomapperColumn("SHIPTOFIRSTNAME") val shipToFirstName: Nothing,
    @KomapperColumn("SHIPTOLASTNAME") val shipToLastName: Nothing,
    @KomapperColumn("CREDITCARD") val creditCard: Nothing,
    @KomapperColumn("EXPRDATE") val expiryDate: Nothing,
    @KomapperColumn("CARDTYPE") val cardType: Nothing,
    @KomapperColumn("LOCALE") val locale: Nothing,
)

@KomapperEntityDef(OrderStatus::class)
@KomapperTable("ORDERSTATUS")
data class OrderStatusDef(
    @KomapperId
    @KomapperColumn("ORDERID")
    val orderId: Nothing,
    @KomapperId
    @KomapperColumn("LINENUM")
    val lineNumber: Nothing,
    @KomapperColumn("TIMESTAMP") val timestamp: Nothing,
    @KomapperColumn("STATUS") val status: Nothing,
)

@KomapperEntityDef(Product::class)
@KomapperTable("PRODUCT")
data class ProductDef(
    @KomapperId
    @KomapperColumn("PRODUCTID")
    val productId: Nothing,
    @KomapperColumn("CATEGORY") val categoryId: Nothing,
    @KomapperColumn("NAME") val name: Nothing,
    @KomapperColumn("DESCN") val description: Nothing,
)

@KomapperEntityDef(Profile::class)
@KomapperTable("PROFILE")
data class ProfileDef(
    @KomapperId
    @KomapperColumn("USERID")
    val username: Nothing,
    @KomapperColumn("LANGPREF") val languagePreference: Nothing,
    @KomapperColumn("FAVCATEGORY") val favouriteCategoryId: Nothing,
    @KomapperColumn("MYLISTOPT") val listOption: Nothing,
    @KomapperColumn("BANNEROPT") val bannerOption: Nothing,
)

@KomapperEntityDef(SignOn::class)
@KomapperTable("SIGNON")
data class SignOnDef(
    @KomapperId
    @KomapperColumn("USERNAME")
    val username: Nothing,
    @KomapperColumn("PASSWORD", masking = true) val password: Nothing,
)
