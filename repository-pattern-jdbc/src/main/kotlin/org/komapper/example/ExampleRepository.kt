package org.komapper.example

import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.operator.count
import org.komapper.core.dsl.operator.plus
import org.komapper.core.dsl.query.firstOrNull
import org.komapper.core.dsl.query.on
import org.komapper.core.dsl.query.where
import org.komapper.jdbc.JdbcDatabase
import java.math.BigDecimal

class ExampleRepository(private val db: JdbcDatabase) {

    private val d = Meta.department
    private val e = Meta.employee
    private val m = Meta.manager
    private val a = Meta.address
    private val onEmployeeDepartment = on { e.departmentId eq d.departmentId }
    private val onEmployeeManager = on { e.managerId eq m.employeeId }
    private val onEmployeeAddress = on { e.addressId eq a.addressId }
    private val isHighPerformer = where { e.salary greaterEq BigDecimal(3_000) }

    fun fetchEmployeeById(employeeId: Int): Employee? {
        val query = QueryDsl.from(e).where { e.employeeId eq employeeId }.firstOrNull()
        return db.runQuery(query)
    }

    fun fetchAddressById(addressId: Int): Address? {
        val query = QueryDsl.from(a).where { a.addressId eq addressId }.firstOrNull()
        return db.runQuery(query)
    }

    fun fetchHighPerformers(): List<Employee> {
        val query = QueryDsl.from(e).where(isHighPerformer).orderBy(e.employeeId)
        return db.runQuery(query)
    }

    fun fetchDepartmentsContainingAnyHighPerformers(): List<Department> {
        val subquery = QueryDsl.from(e).where(isHighPerformer).select(e.departmentId)
        val query = QueryDsl.from(d).where {
            d.departmentId inList { subquery }
        }.orderBy(d.departmentId)
        return db.runQuery(query)
    }

    fun fetchAllEmployees(): List<Employee> {
        val query = QueryDsl.from(e).orderBy(e.employeeId)
        return db.runQuery(query)
    }

    fun fetchEmployees(salary: BigDecimal? = null, departmentName: String? = null): List<Employee> {
        val query = QueryDsl.from(e)
            .innerJoin(d, onEmployeeDepartment)
            .where {
                e.salary eq salary
                d.departmentName eq departmentName
            }.orderBy(e.employeeId)
        return db.runQuery(query)
    }

    fun fetchDepartmentNameAndEmployeeSize(): List<Pair<String?, Long?>> {
        val query = QueryDsl.from(d)
            .leftJoin(e, onEmployeeDepartment)
            .orderBy(d.departmentId)
            .groupBy(d.departmentName)
            .select(d.departmentName, count(e.employeeId))
        return db.runQuery(query)
    }

    fun fetchDepartmentEmployees(): Map<Department, Set<Employee>> {
        val query = QueryDsl.from(d)
            .leftJoin(e, onEmployeeDepartment)
            .orderBy(d.departmentId)
            .includeAll()
        val store = db.runQuery(query)
        return store.oneToMany(d, e)
    }

    fun fetchManagerEmployees(): Map<Employee, Set<Employee>> {
        val query = QueryDsl.from(e)
            .leftJoin(m, onEmployeeManager)
            .orderBy(e.managerId)
            .includeAll()
        val store = db.runQuery(query)
        return store.oneToMany(m, e)
    }

    fun fetchEmployeeAddress(): Map<Employee, Address?> {
        val query = QueryDsl.from(e)
            .leftJoin(a, onEmployeeAddress)
            .orderBy(e.employeeId)
            .includeAll()
        val store = db.runQuery(query)
        return store.oneToOne(e, a)
    }

    fun fetchAllAssociations(): Triple<Map<Department, Set<Employee>>, Map<Employee, Address?>, Map<Employee, Set<Employee>>> {
        val query = QueryDsl.from(d)
            .leftJoin(e, onEmployeeDepartment)
            .leftJoin(a, onEmployeeAddress)
            .leftJoin(m, onEmployeeManager)
            .orderBy(d.departmentId)
            .includeAll()
        val store = db.runQuery(query)
        val deptEmp = store.oneToMany(d, e)
        val empAddr = store.oneToOne(e, a)
        val mgrEmp = store.oneToMany(m, e)
        return Triple(deptEmp, empAddr, mgrEmp)
    }

    fun updateEmployee(employee: Employee): Employee {
        val query = QueryDsl.update(e).single(employee)
        return db.runQuery(query)
    }

    fun updateSalaryOfHighPerformers(raise: BigDecimal): Long {
        val query = QueryDsl.update(e).set {
            e.salary eq e.salary + raise
        }.where(isHighPerformer)
        return db.runQuery(query)
    }

    fun insertAddress(address: Address): Address {
        val query = QueryDsl.insert(a).single(address)
        return db.runQuery(query)
    }

    fun upsertAddress(address: Address): Address {
        val query = QueryDsl.insert(a).onDuplicateKeyUpdate().executeAndGet(address)
        return db.runQuery(query)
    }
}
