package com.wallace.Library.integration.controller.withjson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wallace.Library.integration.TestConfigs
import com.wallace.Library.integration.testcontainers.AbstractIntegrationTest
import com.wallace.Library.integration.vo.AccountCredentialsVO
import com.wallace.Library.integration.vo.PersonVO
import com.wallace.Library.integration.vo.TokenVO
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.builder.RequestSpecBuilder
import io.restassured.config.ObjectMapperConfig
import io.restassured.config.RestAssuredConfig
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonControllerJsonTest : AbstractIntegrationTest() {

    private lateinit var specification: RequestSpecification
    private lateinit var objectMapper: ObjectMapper
    private lateinit var personVO: PersonVO

    @BeforeAll
    fun setupTest() {
        objectMapper = ObjectMapper()
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)

        personVO = PersonVO()

        RestAssured.config = RestAssuredConfig.config().objectMapperConfig(
            ObjectMapperConfig.objectMapperConfig().jackson2ObjectMapperFactory { _, _ ->
                ObjectMapper().registerKotlinModule() // Garante o suporte ao construtor do Kotlin
            }
        )
    }


    @Test
    @Order(0)
    fun testLogin() {
        val user = AccountCredentialsVO(
            username = "admin",
            password = "admin",
        )

        val token = given()
            .basePath("/auth/signin")
            .port(TestConfigs.SERVER_PORT)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(user)
            .`when`()
            .post()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(TokenVO::class.java)
            .accessToken

        specification = RequestSpecBuilder()
            .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer $token")
                .setBasePath("/api/person/v1")
            .setPort(TestConfigs.SERVER_PORT)
                .addFilter(RequestLoggingFilter(LogDetail.ALL))
                .addFilter(ResponseLoggingFilter(LogDetail.ALL))
            .build()
    }

    @Test
    @Order(1)
    fun testCreatePerson() {
        mockPerson()

        val createdPerson = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(personVO)
            .`when`()
                .post()
                .then()
                    .statusCode(200)
                    .extract()
                    .body()
            .`as`(PersonVO::class.java)

        //val createdPerson = objectMapper.readValue(content, PersonVO::class.java)

        personVO = createdPerson

        assertNotNull(createdPerson.id)
        assertTrue(createdPerson.id > 0)
        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.gender)
        assertNotNull(createdPerson.address)

        assertEquals("teste", createdPerson.firstName)
        assertEquals("testado", createdPerson.lastName)
        assertEquals("teststreet", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(2)
    fun testUpdatePerson() {
        personVO.firstName = "Testinho"

        val createdPerson = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .body(personVO)
            .`when`()
            .put()
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java)

        //val createdPerson = objectMapper.readValue(content, PersonVO::class.java)

        personVO = createdPerson

        assertNotNull(createdPerson.id)

        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.gender)
        assertNotNull(createdPerson.address)

        assertEquals(personVO.id, createdPerson.id)
        assertEquals("Testinho", createdPerson.firstName)
        assertEquals("testado", createdPerson.lastName)
        assertEquals("teststreet", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(3)
    fun testFindById() {
        val createdPerson = given()
            .spec(specification)
            .contentType(TestConfigs.CONTENT_TYPE_JSON)
            .pathParam("id", personVO.id)
            .`when`()
            .get("{id}")
            .then()
            .statusCode(200)
            .extract()
            .body()
            .`as`(PersonVO::class.java)

        //val createdPerson = objectMapper.readValue(content, PersonVO::class.java)

        personVO = createdPerson

        assertNotNull(createdPerson.id)

        assertNotNull(createdPerson.firstName)
        assertNotNull(createdPerson.lastName)
        assertNotNull(createdPerson.gender)
        assertNotNull(createdPerson.address)

        assertEquals(personVO.id, createdPerson.id)
        assertEquals("Testinho", createdPerson.firstName)
        assertEquals("testado", createdPerson.lastName)
        assertEquals("teststreet", createdPerson.address)
        assertEquals("Male", createdPerson.gender)
    }

    @Test
    @Order(4)
    fun testDelete() {
        given()
            .spec(specification)
            .pathParam("id", personVO.id)
            .`when`()
            .delete("{id}")
            .then()
            .statusCode(204)

        //val createdPerson = objectMapper.readValue(content, PersonVO::class.java)

    }

    private fun mockPerson() {
        personVO.firstName = "teste"
        personVO.lastName = "testado"
        personVO.address = "teststreet"
        personVO.gender = "Male"
    }
}