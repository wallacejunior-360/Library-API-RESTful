package com.wallace.Library.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer{

    @Value("\${cors.originPatterns:default}")
    private val corsOriginPatterns: String = ""

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
//  Content Negotiation via query params -> permite json ou xml
//        configurer.favorParameter(true)
//            .parameterName("mediaType")
//            .ignoreAcceptHeader(true)
//            .useRegisteredExtensionsOnly(false)
//            .defaultContentType(MediaType.APPLICATION_JSON)
//                .mediaType("json", MediaType.APPLICATION_JSON)
//                .mediaType("xml", MediaType.APPLICATION_XML)


        // Content Negotiation via Header
        configurer.favorParameter(false)
            .ignoreAcceptHeader(false)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON)
            .mediaType("json", MediaType.APPLICATION_JSON)
            .mediaType("xml", MediaType.APPLICATION_XML)
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        val allowedOrigins = corsOriginPatterns.split(",").toTypedArray()
        registry.addMapping("/**")
            .allowedOrigins(*allowedOrigins)
            .allowedMethods("*")
            .allowCredentials(true)
    }

}