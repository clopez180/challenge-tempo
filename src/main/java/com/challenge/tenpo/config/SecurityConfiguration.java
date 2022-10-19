package com.challenge.tenpo.config;

import com.challenge.tenpo.config.filter.TokenInspectorFilter;
import com.challenge.tenpo.service.ISessionSecurityService;
import com.challenge.tenpo.service.impl.JwtService;
import com.challenge.tenpo.service.impl.SessionService;
import com.challenge.tenpo.service.impl.UserCredentialService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final AccessDeniedHandlerImpl accessDeniedHandler;


    private final JwtService jwtService;


    private final ISessionSecurityService sessionService;


    private final UserCredentialService userCredentialService;

    @Lazy
    public SecurityConfiguration(AccessDeniedHandlerImpl accessDeniedHandler, JwtService jwtService,
                                 SessionService sessionService, UserCredentialService userCredentialService) {
        this.accessDeniedHandler = accessDeniedHandler;
        this.jwtService = jwtService;
        this.sessionService = sessionService;
        this.userCredentialService = userCredentialService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()
                .exceptionHandling().authenticationEntryPoint(createCustomAuthenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler)
                .and().authorizeRequests()
                .antMatchers("/auth/sign_in").permitAll()
                .antMatchers("/auth/login").permitAll()
                .antMatchers("/api/api-docs").permitAll()
                .antMatchers("/configuration/ui").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/configuration/security").permitAll()
                .antMatchers("/swagger-ui" + ".html").permitAll()
                .antMatchers("/swagger-ui" + "/*").permitAll()
                .antMatchers("/v2/api-docs").permitAll()
                .antMatchers("/webjars" + "/**").permitAll()
                .antMatchers("/api" + "/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterAfter(createTokenInspectorFilter(), UsernamePasswordAuthenticationFilter.class);

    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public TokenInspectorFilter createTokenInspectorFilter() {
        return new TokenInspectorFilter(userCredentialService, jwtService, sessionService);
    }

    @Bean
    public CustomAuthenticationEntryPoint createCustomAuthenticationEntryPoint() {
        CustomAuthenticationEntryPoint customAuthenticationEntryPoint = new CustomAuthenticationEntryPoint();
        return customAuthenticationEntryPoint;
    }

    @Bean
    public AccessDeniedHandlerImpl createAccessDeniedHandler() {
        return new AccessDeniedHandlerImpl();
    }

    @Bean
    public BCryptPasswordEncoder createPasswordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userCredentialService).passwordEncoder(createPasswordEncoder());
    }



    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
                                               String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }

    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(WebEndpointsSupplier webEndpointsSupplier, ServletEndpointsSupplier servletEndpointsSupplier, ControllerEndpointsSupplier controllerEndpointsSupplier, EndpointMediaTypes endpointMediaTypes, CorsEndpointProperties corsProperties, WebEndpointProperties webEndpointProperties, Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = this.shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes, corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath), shouldRegisterLinksMapping, null);
    }
}
