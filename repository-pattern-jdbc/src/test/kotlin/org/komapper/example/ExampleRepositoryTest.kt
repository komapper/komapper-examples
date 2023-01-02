package org.komapper.example

import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.InvocationInterceptor
import org.junit.jupiter.api.extension.ReflectiveInvocationContext
import org.komapper.core.dsl.QueryDsl
import org.komapper.jdbc.JdbcDatabase
import java.lang.reflect.Method
import java.math.BigDecimal
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ExampleRepositoryTest : InvocationInterceptor {

    private val db = JdbcDatabase("jdbc:h2:mem:example;DB_CLOSE_DELAY=-1")
    private val repo = ExampleRepository(db)

    @Test
    fun fetchAllEmployees() {
        val employees = repo.fetchAllEmployees()
        assertEquals(14, employees.size)
    }

    @Test
    fun fetchHighPerformers() {
        val employees = repo.fetchHighPerformers()
        assertEquals(4, employees.size)
    }

    @Test
    fun fetchDepartmentsContainingAnyHighPerformers() {
        val department = repo.fetchDepartmentsContainingAnyHighPerformers()
        assertEquals(2, department.size)
        val names = department.map { it.departmentName }
        assertTrue("ACCOUNTING" in names)
        assertTrue("SALES" in names)
    }

    @Test
    fun fetchEmployeeByEmployeeId() {
        val employee = repo.fetchEmployeeById(1)
        assertNotNull(employee)
    }

    @Test
    fun fetchEmployeesBySalary() {
        val employees = repo.fetchEmployees(salary = BigDecimal(3_000))
        println(employees)
        assertEquals(3, employees.size)
    }

    @Test
    fun fetchEmployeesByDepartmentName() {
        val employees = repo.fetchEmployees(departmentName = "SALES")
        assertEquals(6, employees.size)
    }

    @Test
    fun fetchEmployeesBySalaryAndDepartmentName() {
        val employees = repo.fetchEmployees(salary = BigDecimal(3_000), departmentName = "SALES")
        assertEquals(1, employees.size)
    }

    @Test
    fun fetchDepartmentNameAndEmployeeSize() {
        val list = repo.fetchDepartmentNameAndEmployeeSize()
        assertEquals(4, list.size)
    }

    @Test
    fun fetchDepartmentEmployees() {
        val departmentEmployees = repo.fetchDepartmentEmployees()
        assertEquals(4, departmentEmployees.size)
    }

    @Test
    fun fetchManagerEmployees() {
        val managerEmployees = repo.fetchManagerEmployees()
        assertEquals(6, managerEmployees.size)
    }

    @Test
    fun fetchEmployeeAddress() {
        val employeeAddress = repo.fetchEmployeeAddress()
        assertEquals(14, employeeAddress.size)
    }

    @Test
    fun fetchAllAssociations() {
        val (deptEmp, empAddr, mgrEmp) = repo.fetchAllAssociations()
        assertEquals(4, deptEmp.size)
        assertEquals(14, empAddr.size)
        assertEquals(6, mgrEmp.size)
    }

    @Test
    fun updateEmployee() {
        val employee = repo.fetchEmployeeById(1)
        assertNotNull(employee)
        val employee2 = employee.copy(salary = employee.salary * BigDecimal(2))
        repo.updateEmployee(employee2)
    }

    @Test
    fun updateSalaryOfHighPerformers() {
        val count = repo.updateSalaryOfHighPerformers(BigDecimal(100))
        assertEquals(4L, count)
    }

    @Test
    fun insertAddress() {
        val address = Address(street = "new street")
        val address2 = repo.insertAddress(address)
        assertNotEquals(address.addressId, address2.addressId)
        println(address2.addressId)
    }

    @Test
    fun upsertAddress() {
        // UPDATE
        val address1 = repo.fetchAddressById(1)!!
        repo.upsertAddress(address1.copy(street = "tokyo street")).let {
            val tokyo = repo.fetchAddressById(it.addressId)
            assertEquals("tokyo street", tokyo?.street)
            println(tokyo)
        }

        // INSERT
        repo.upsertAddress(Address(street = "osaka street")).let {
            val osaka = repo.fetchAddressById(it.addressId)
            assertEquals("osaka street", osaka?.street)
            println(osaka)
        }
    }

    @BeforeTest
    fun before() {
        db.runQuery {
            QueryDsl.executeScript(script).options {
                it.copy(suppressLogging = true)
            }
        }
    }

    @AfterTest
    fun after() {
        db.runQuery {
            QueryDsl.executeScript("drop all objects").options {
                it.copy(suppressLogging = true)
            }
        }
    }

    override fun interceptTestMethod(
        invocation: InvocationInterceptor.Invocation<Void>?,
        invocationContext: ReflectiveInvocationContext<Method>?,
        extensionContext: ExtensionContext?,
    ) {
        db.config.session.transactionOperator.required {
            it.isRollbackOnly()
            super.interceptTestMethod(invocation, invocationContext, extensionContext)
        }
    }

    private val script = """
        CREATE TABLE IF NOT EXISTS DEPARTMENT(DEPARTMENT_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 100) NOT NULL PRIMARY KEY, DEPARTMENT_NO INTEGER NOT NULL UNIQUE,DEPARTMENT_NAME VARCHAR(20),LOCATION VARCHAR(20) DEFAULT 'TOKYO', VERSION INTEGER);
        CREATE TABLE IF NOT EXISTS ADDRESS(ADDRESS_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 100) NOT NULL PRIMARY KEY, STREET VARCHAR(20) UNIQUE, VERSION INTEGER);
        CREATE TABLE IF NOT EXISTS EMPLOYEE(EMPLOYEE_ID INTEGER GENERATED BY DEFAULT AS IDENTITY (START WITH 100) NOT NULL PRIMARY KEY, EMPLOYEE_NO INTEGER NOT NULL ,EMPLOYEE_NAME VARCHAR(20),MANAGER_ID INTEGER,HIREDATE DATE,SALARY NUMERIC(7,2),DEPARTMENT_ID INTEGER,ADDRESS_ID INTEGER,VERSION INTEGER, CONSTRAINT FK_DEPARTMENT_ID FOREIGN KEY(DEPARTMENT_ID) REFERENCES DEPARTMENT(DEPARTMENT_ID), CONSTRAINT FK_ADDRESS_ID FOREIGN KEY(ADDRESS_ID) REFERENCES ADDRESS(ADDRESS_ID));

        INSERT INTO DEPARTMENT VALUES(1,10,'ACCOUNTING','NEW YORK',1);
        INSERT INTO DEPARTMENT VALUES(2,20,'RESEARCH','DALLAS',1);
        INSERT INTO DEPARTMENT VALUES(3,30,'SALES','CHICAGO',1);
        INSERT INTO DEPARTMENT VALUES(4,40,'OPERATIONS','BOSTON',1);
        INSERT INTO ADDRESS VALUES(1,'STREET 1',1);
        INSERT INTO ADDRESS VALUES(2,'STREET 2',1);
        INSERT INTO ADDRESS VALUES(3,'STREET 3',1);
        INSERT INTO ADDRESS VALUES(4,'STREET 4',1);
        INSERT INTO ADDRESS VALUES(5,'STREET 5',1);
        INSERT INTO ADDRESS VALUES(6,'STREET 6',1);
        INSERT INTO ADDRESS VALUES(7,'STREET 7',1);
        INSERT INTO ADDRESS VALUES(8,'STREET 8',1);
        INSERT INTO ADDRESS VALUES(9,'STREET 9',1);
        INSERT INTO ADDRESS VALUES(10,'STREET 10',1);
        INSERT INTO ADDRESS VALUES(11,'STREET 11',1);
        INSERT INTO ADDRESS VALUES(12,'STREET 12',1);
        INSERT INTO ADDRESS VALUES(13,'STREET 13',1);
        INSERT INTO ADDRESS VALUES(14,'STREET 14',1);
        INSERT INTO ADDRESS VALUES(15,'STREET 15',1);
        INSERT INTO EMPLOYEE VALUES(1,7369,'SMITH',13,'1980-12-17',800,2,1,1);
        INSERT INTO EMPLOYEE VALUES(2,7499,'ALLEN',6,'1981-02-20',1600,3,2,1);
        INSERT INTO EMPLOYEE VALUES(3,7521,'WARD',6,'1981-02-22',3000,3,3,1);
        INSERT INTO EMPLOYEE VALUES(4,7566,'JONES',9,'1981-04-02',2975,1,4,1);
        INSERT INTO EMPLOYEE VALUES(5,7654,'MARTIN',6,'1981-09-28',1250,3,5,1);
        INSERT INTO EMPLOYEE VALUES(6,7698,'BLAKE',9,'1981-05-01',2850,3,6,1);
        INSERT INTO EMPLOYEE VALUES(7,7782,'CLARK',9,'1981-06-09',2450,1,7,1);
        INSERT INTO EMPLOYEE VALUES(8,7788,'SCOTT',4,'1982-12-09',3000.0,1,8,1);
        INSERT INTO EMPLOYEE VALUES(9,7839,'KING',NULL,'1981-11-17',5000,1,9,1);
        INSERT INTO EMPLOYEE VALUES(10,7844,'TURNER',6,'1981-09-08',1500,3,10,1);
        INSERT INTO EMPLOYEE VALUES(11,7876,'ADAMS',8,'1983-01-12',1100,2,11,1);
        INSERT INTO EMPLOYEE VALUES(12,7900,'JAMES',6,'1981-12-03',950,3,12,1);
        INSERT INTO EMPLOYEE VALUES(13,7902,'FORD',4,'1981-12-03',3000,1,13,1);
        INSERT INTO EMPLOYEE VALUES(14,7934,'MILLER',7,'1982-01-23',1300,1,14,1);
    """.trimIndent()
}
