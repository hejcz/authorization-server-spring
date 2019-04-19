package io.github.hejcz.authorizationserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.provider.code.InMemoryAuthorizationCodeServices
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController


@SpringBootApplication
class AuthorizationServerApplication

fun main(args: Array<String>) {
    runApplication<AuthorizationServerApplication>(*args)
}

@RestController
class RedirectHandler {

    @RequestMapping("/redirect")
    fun redirect(@RequestParam("code") code: String): Unit = println("$code")

}

@Configuration
@EnableAuthorizationServer
class AuthorizationServer

@Component
class CustomAuthorizationServerConfigurer : AuthorizationServerConfigurerAdapter() {
    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.run {
            inMemory()
                .withClient("julian")
                .scopes("write", "read")
                .authorizedGrantTypes("authorization_code")
                .redirectUris("http://localhost:8080/redirect")
        }
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints?.authorizationCodeServices(InMemoryAuthorizationCodeServices())
    }
}


@Component
class WebSecurityConfig : WebSecurityConfigurerAdapter() {
    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.run {
            inMemoryAuthentication()
                .withUser("julian")
                .password("{noop}rubin")
                .authorities("mother")
        }
    }
}
