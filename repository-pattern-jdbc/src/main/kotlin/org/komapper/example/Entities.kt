package org.komapper.example

import org.komapper.annotation.KomapperAutoIncrement
import org.komapper.annotation.KomapperEntityDef
import org.komapper.annotation.KomapperId
import org.komapper.annotation.KomapperVersion
import java.math.BigDecimal
import java.time.LocalDate

data class Address(
    val addressId: Int = 0,
    val street: String,
    val version: Int = 0,
)

@KomapperEntityDef(Address::class)
data class AddressDef(
    @KomapperId
    @KomapperAutoIncrement
    val addressId: Nothing,
    @KomapperVersion
    val version: Nothing,
)

data class Employee(
    val employeeId: Int,
    val employeeNo: Int,
    val employeeName: String,
    val managerId: Int?,
    val hiredate: LocalDate,
    val salary: BigDecimal,
    val departmentId: Int,
    val addressId: Int,
    val version: Int,
)

@KomapperEntityDef(Employee::class, ["employee", "manager"])
data class EmployeeDef(
    @KomapperId
    @KomapperAutoIncrement
    val employeeId: Int,
    @KomapperVersion
    val version: Int,
)

data class Department(
    val departmentId: Int,
    val departmentNo: Int,
    val departmentName: String,
    val location: String,
    val version: Int,
)

@KomapperEntityDef(Department::class)
data class DepartmentDef(
    @KomapperId
    @KomapperAutoIncrement
    val departmentId: Nothing,
    @KomapperVersion
    val version: Nothing,
)
