package org.komapper.example.web

import org.komapper.example.formatter.BigDecimalFormatter
import org.komapper.example.formatter.LocalDateTimeFormatter
import org.springframework.boot.web.server.ErrorPage
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebMvcConfiguration : WebMvcConfigurer {
    override fun addFormatters(registry: FormatterRegistry) {
        registry.addFormatter(BigDecimalFormatter())
        registry.addFormatter(LocalDateTimeFormatter())
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/")
            .resourceChain(false)
    }

    @Bean
    fun webServerFactoryCustomizer(): WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        return ServletContainerCustomizer()
    }

    class ServletContainerCustomizer :
        WebServerFactoryCustomizer<ConfigurableServletWebServerFactory> {
        override fun customize(factory: ConfigurableServletWebServerFactory) {
            factory.addErrorPages(ErrorPage(HttpStatus.NOT_FOUND, "/404"))
        }
    }
}
