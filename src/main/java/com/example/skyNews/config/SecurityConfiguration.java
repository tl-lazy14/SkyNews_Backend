package com.example.skyNews.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeHttpRequests()
                .requestMatchers(
                        "/auth/login/**",
                        "/auth/refresh/**",
                        "/auth/register",
                        "/auth/generate-verification-code",
                        "/category-topic",
                        "/category-topic/get-list-category",
                        "/category-topic/get-list-topic/**",
                        "/article/articles-homepage",
                        "/article/articles-category-page",
                        "/article/articles-search-page",
                        "/article/newest",
                        "/article/articles-most-view",
                        "/article/articles-hot-news",
                        "/article/detail/**",
                        "/article/list-comment/**",
                        "/article/add-view/**"
                ).permitAll()

                .requestMatchers(
                        "/auth/change-password/**",
                        "/article/edit-article/**"
                ).hasAnyAuthority("ROLE_JOURNALIST", "ROLE_EDITOR", "ROLE_USER")

                .requestMatchers(
                        "/category-topic/add-category",
                        "/category-topic/add-topic/**",
                        "/category-topic/rename-category/**",
                        "/category-topic/rename-topic/**",
                        "/category-topic/delete-category/**",
                        "/category-topic/delete-topic/**",
                        "/category-topic/get-list-category",
                        "/user/admin/get-list-user",
                        "/user/admin/delete/**"
                ).hasAuthority("ROLE_SENIOR_ADMIN")

                .requestMatchers(
                        "/article/create-article",
                        "/article/get-my-articles/**",
                        "/article/delete-not-send/**",
                        "/article/send-editor/**"
                ).hasAuthority("ROLE_JOURNALIST")

                .requestMatchers(
                        "/article/pending-articles/**",
                        "/article/approve/**",
                        "/article/get-approved-articles/**",
                        "/article/delete-posted/**"
                ).hasAuthority("ROLE_EDITOR")

                .requestMatchers(
                        "/user/change-username/**",
                        "/article/check-save/**",
                        "/article/handle-save/**",
                        "/article/add-comment/**",
                        "/article/handle-like-comment/**",
                        "/article/saved-news/**",
                        "/article/un-save/**",
                        "/article/viewed-news/**",
                        "/notification/get-list-notification/**",
                        "/notification/read-notification/**"
                ).hasAuthority("ROLE_USER")

                .anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
