package org.komapper.example.web

import org.komapper.example.service.AccountService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import javax.sql.DataSource

@Configuration
class WebSecurityConfiguration(private val dataSource: DataSource, private val accountService: AccountService) :
    WebSecurityConfigurerAdapter() {
    @Bean
    fun persistentTokenRepository(): PersistentTokenRepository {
        val tokenRepository = JdbcTokenRepositoryImpl()
        tokenRepository.dataSource = dataSource
        return tokenRepository
    }

    override fun configure(http: HttpSecurity) {
        http.authorizeRequests()
            .antMatchers(
                "/",
                "/cart/**",
                "/category/**",
                "/product/**",
                "/item/**",
                "/search/**",
                "/account/add",
                "/images/**",
                "/css/**",
                "/js/**",
                "/webjars/**"
            )
            .permitAll()
            .anyRequest()
            .authenticated()
        http.formLogin()
            .loginPage("/signin")
            .permitAll()
            .and()
            .logout()
            .logoutUrl("/signout")
            .permitAll()
        http.rememberMe().tokenRepository(persistentTokenRepository())
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(accountService).passwordEncoder(passwordEncoder())
    }
}
