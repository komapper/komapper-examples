package org.komapper.example.service

import org.komapper.example.entity.Account
import org.komapper.example.entity.Profile
import org.komapper.example.entity.SignOn
import org.komapper.example.model.AccountAggregate
import org.komapper.example.repository.AccountRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AccountService(private val accountRepository: AccountRepository) : UserDetailsService {

    fun getAccountAggregate(username: String): AccountAggregate {
        return accountRepository.fetchAccountAggregate(username)
    }

    fun insertAccount(account: Account) {
        accountRepository.insertAccount(account)
    }

    fun insertProfile(profile: Profile) {
        accountRepository.insertProfile(profile)
    }

    fun insertSignOn(signOn: SignOn) {
        accountRepository.insertSignOn(signOn)
    }

    fun updateAccount(account: Account) {
        accountRepository.updateAccount(account)
    }

    fun updateProfile(profile: Profile) {
        accountRepository.updateProfile(profile)
    }

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val signOn = accountRepository.fetchSignOn(username)
            ?: throw UsernameNotFoundException("$username not found")
        return User(signOn.username, signOn.password, emptyList())
    }
}
