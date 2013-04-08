/* 
 * Copyright 2013 JIWHIZ Consulting Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jiwhiz.blog.config;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.springframework.web.servlet.view.tiles2.TilesConfigurer;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.extras.tiles2.spring.web.configurer.ThymeleafTilesConfigurer;
import org.thymeleaf.spring3.SpringTemplateEngine;
import org.thymeleaf.spring3.view.ThymeleafViewResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

/**
 * 
 * @author Yuan Ji
 *
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.jiwhiz.blog.web")
@ImportResource("classpath:security.xml")
public class WebConfig extends WebMvcConfigurerAdapter {

    @Inject
    private Environment environment;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        PageableArgumentResolver resolver = new PageableArgumentResolver();
        resolver.setFallbackPagable(new PageRequest(1, 10));
        argumentResolvers.add(new ServletWebArgumentResolverAdapter(resolver));
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/", "/",
                        "classpath:/META-INF/web-resources/");
        registry.addResourceHandler("/robots.txt").addResourceLocations("/");
    }

    public Validator getValidator() {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.setValidationMessageSource(messageSource());
        return factory;
    }

    /**
     * Messages to support internationalization/localization.
     * TODO: move messages and application text to property files.
     */
    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames(new String[] { "WEB-INF/i18n/messages", "WEB-INF/i18n/application" });
        return messageSource;
    }

    // Thymeleaf config
    @Bean
    public ITemplateResolver templateResolver() {
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver();
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML5");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
        templateEngine.setTemplateResolver(templateResolver());

        Set<IDialect> additionalDialects = new HashSet<IDialect>();
        additionalDialects.add(new org.thymeleaf.extras.springsecurity3.dialect.SpringSecurityDialect());
        additionalDialects.add(new org.thymeleaf.extras.tiles2.dialect.TilesDialect());
        additionalDialects.add(new org.thymeleaf.extras.conditionalcomments.dialect.ConditionalCommentsDialect());
        templateEngine.setAdditionalDialects(additionalDialects);
        return templateEngine;
    }

    @Bean
    public ViewResolver viewResolver() {
        ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setViewClass(org.thymeleaf.extras.tiles2.spring.web.view.ThymeleafTilesView.class);
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }
    
    @Bean
    public TilesConfigurer tilesConfigurer() {
        ThymeleafTilesConfigurer configurer = new ThymeleafTilesConfigurer();
        configurer.setDefinitions(new String[] { "/WEB-INF/templates/**/views.xml" });
        return configurer;
    }

}
