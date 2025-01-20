package org.komapper.example.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.komapper.core.dsl.Meta
import org.komapper.core.dsl.QueryDsl
import org.komapper.core.dsl.query.singleOrNull
import org.komapper.example.entity.Account
import org.komapper.example.entity.Profile
import org.komapper.example.entity.SignOn
import org.komapper.example.entity.account
import org.komapper.example.entity.profile
import org.komapper.example.entity.signOn
import org.komapper.example.model.AccountAggregate
import org.komapper.jdbc.JdbcDatabase
import org.komapper.spring.boot.test.autoconfigure.jdbc.KomapperJdbcTest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase

@KomapperJdbcTest
@AutoConfigureTestDatabase
class AccountRepositoryTest(
    @Autowired
    private val accountRepository: AccountRepository,
    @Autowired
    private val db: JdbcDatabase,
) {
    @Test
    fun fetchAccountAggregate() {
        assertThat(accountRepository.fetchAccountAggregate("komapper")).isEqualTo(
            AccountAggregate(
                Account(
                    username = "komapper",
                    email = "komapper@yourdomain.com",
                    firstName = "ABC",
                    lastName = "XYX",
                    status = "OK",
                    address1 = "901 San Antonio Road",
                    address2 = "MS UCUP02-206",
                    city = "Palo Alto",
                    state = "CA",
                    zip = "94303",
                    country = "USA",
                    phone = "555-555-5555",
                ),
                Profile(
                    username = "komapper",
                    languagePreference = "english",
                    favouriteCategoryId = "DOGS",
                    listOption = 1,
                    bannerOption = 1,
                ),
                SignOn(
                    username = "komapper",
                    password = "\$2a\$10\$rEpBZe1kO3VxlUq.rHs1Auo.QlSo4pzNpcLkuE92ss4oT76r35XHe",
                ),
            )
        )
    }

    @Test
    fun insertAccount() {
        val account = Account(
            username = "komapper01",
            email = "komapper01@komapper.org",
            firstName = "Komapper",
            lastName = "01",
            status = "!!",
            address1 = "Line#1",
            address2 = "line#2",
            city = "City",
            state = "CA",
            zip = "90000",
            country = "USA",
            phone = "111-222-3333",
        )

        accountRepository.insertAccount(account)

        val query = QueryDsl.from(Meta.account).where { Meta.account.username eq "komapper01" }
        assertThat(db.runQuery(query)).containsExactly(account)
    }

    @Test
    fun insertProfile() {
        val profile = Profile(
            username = "komapper01",
            languagePreference = "spanish",
            favouriteCategoryId = "BIRDS",
            listOption = 1,
            bannerOption = 0,
        )

        accountRepository.insertProfile(profile)

        val query = QueryDsl.from(Meta.profile).where { Meta.profile.username eq "komapper01" }
        assertThat(db.runQuery(query)).containsExactly(profile)
    }

    @Test
    fun insertSignOn() {
        val signOn = SignOn("username", "password")

        accountRepository.insertSignOn(signOn)

        val query = QueryDsl.from(Meta.signOn).where { Meta.signOn.username eq "username" }
        assertThat(db.runQuery(query)).containsExactly(signOn)
    }

    @Test
    fun updateAccount() {
        val query = QueryDsl.from(Meta.account)
            .where { Meta.account.username eq "komapper" }
            .select(Meta.account.phone)
            .singleOrNull()
        assertThat(db.runQuery(query)).isEqualTo("555-555-5555")

        val account = Account(
            username = "komapper",
            email = "komapper@yourdomain.com",
            firstName = "ABC",
            lastName = "XYX",
            status = "OK",
            address1 = "901 San Antonio Road",
            address2 = "MS UCUP02-206",
            city = "Palo Alto",
            state = "CA",
            zip = "94303",
            country = "USA",
            phone = "888-888-8888",
        )

        accountRepository.updateAccount(account)

        assertThat(db.runQuery(query)).isEqualTo("888-888-8888")
    }

    @Test
    fun updateProfile() {
        val query = QueryDsl.from(Meta.profile)
            .where { Meta.profile.username eq "komapper" }
            .select(Meta.profile.favouriteCategoryId)
            .singleOrNull()
        assertThat(db.runQuery(query)).isEqualTo("DOGS")

        val profile = Profile(
            username = "komapper",
            languagePreference = "english",
            favouriteCategoryId = "CATS",
            listOption = 1,
            bannerOption = 1,
        )

        accountRepository.updateProfile(profile)

        assertThat(db.runQuery(query)).isEqualTo("CATS")
    }
}
