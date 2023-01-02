package org.komapper.example.web

import org.komapper.example.service.AccountService
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository
import javax.sql.DataSource

@Configuration
class WebSecurityConfiguration(private val dataSource: DataSource, private val accountService: AccountService) {
    @Bean
    fun persistentTokenRepository(): PersistentTokenRepository {
        val tokenRepository = JdbcTokenRepositoryImpl()
        tokenRepository.dataSource = dataSource
        return tokenRepository
    }

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http.authorizeHttpRequests { authz ->
            authz
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .requestMatchers(
                    "/",
                    "/cart/**",
                    "/category/**",
                    "/product/**",
                    "/item/**",
                    "/search/**",
                    "/account/add",
                ).permitAll()
                .anyRequest().authenticated()
        }
        http.formLogin { login ->
            login.loginPage("/signin")
                .loginProcessingUrl("/signin")
                .permitAll()
        }.logout { logout ->
            logout.logoutUrl("/signout")
                .logoutSuccessUrl("/")
        }
        http.rememberMe().tokenRepository(persistentTokenRepository())
        http.getSharedObject(AuthenticationManagerBuilder::class.java)
            .userDetailsService<UserDetailsService>(accountService)
            .passwordEncoder(passwordEncoder())
        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}
