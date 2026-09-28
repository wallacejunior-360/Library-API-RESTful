package com.wallace.Library.integration.controller.cors.withjson

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wallace.Library.integration.TestConfigs
import com.wallace.Library.integration.testcontainers.AbstractIntegrationTest
import com.wallace.Library.integration.vo.PersonVO
import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import io.restassured.filter.log.LogDetail
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.specification.RequestSpecification
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonContollerCorsWithJson(): AbstractIntegrationTest() {

	private lateinit var specification: RequestSpecification
	private lateinit var objectMapper: ObjectMapper
	private lateinit var personVO: PersonVO

	@BeforeAll
	fun setupTest() {
		objectMapper = ObjectMapper().apply {
			disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
			registerKotlinModule()
			propertyNamingStrategy = PropertyNamingStrategies.SNAKE_CASE
		}
		personVO = PersonVO()
	}

	@Test
	@Order(1)
	fun testCreatePerson() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_LOCALHOST
			)
				.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
				.addFilter(RequestLoggingFilter(LogDetail.ALL))
				.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = RestAssured.given()
			.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(objectMapper.writeValueAsString(personVO))
				.`when`()
			.post()
			.then()
				.statusCode(200)
			.extract()
			.body()
				.asString()

		val createdPerson = objectMapper.readValue(content, PersonVO::class.java)
		personVO = createdPerson

		assertNotNull(createdPerson.id)
		assertNotNull(createdPerson.firstName)
		assertNotNull(createdPerson.lastName)
		assertNotNull(createdPerson.address)
		assertNotNull(createdPerson.gender)

		assertTrue(createdPerson.id > 0)

		assertEquals("Wallace", createdPerson.firstName)
		assertEquals("Junior", createdPerson.lastName)
		assertEquals("Rua teste, 123", createdPerson.address)
		assertEquals("Male", createdPerson.gender)
	}

	@Test
	@Order(2)
	fun testCreatePersonWithWrongOrigin() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_WALLACE
			)
				.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
				.addFilter(RequestLoggingFilter(LogDetail.ALL))
				.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = RestAssured.given()
			.spec(specification)
				.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.body(objectMapper.writeValueAsString(personVO))
				.`when`()
			.post()
			.then()
				.statusCode(403)
			.extract()
			.body()
				.asString()

		assertEquals("Invalid CORS request", content)
	}


	@Test
	@Order(3)
	fun testFindByIdPerson() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_LOCALHOST
			)
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = RestAssured.given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", personVO.id)
			.`when`()["{id}"]
			.then()
			.statusCode(200)
			.extract()
			.body()
			.asString()

		val createdPerson = objectMapper.readValue(content, PersonVO::class.java)

		assertNotNull(createdPerson.id)
		assertNotNull(createdPerson.firstName)
		assertNotNull(createdPerson.lastName)
		assertNotNull(createdPerson.address)
		assertNotNull(createdPerson.gender)

		assertTrue(createdPerson.id > 0)

		assertEquals("Wallace", createdPerson.firstName)
		assertEquals("Junior", createdPerson.lastName)
		assertEquals("Rua teste, 123", createdPerson.address)
		assertEquals("Male", createdPerson.gender)
	}

	@Test
	@Order(3)
	fun testFindByIdPersonWithWrongOrigin() {
		mockPerson()

		specification = RequestSpecBuilder()
			.addHeader(
				TestConfigs.HEADER_PARAM_ORIGIN,
				TestConfigs.ORIGIN_WALLACE
			)
			.setBasePath("/api/person/v1")
			.setPort(TestConfigs.SERVER_PORT)
			.addFilter(RequestLoggingFilter(LogDetail.ALL))
			.addFilter(ResponseLoggingFilter(LogDetail.ALL))
			.build()

		val content = RestAssured.given()
			.spec(specification)
			.contentType(TestConfigs.CONTENT_TYPE_JSON)
			.pathParam("id", personVO.id)
			.`when`()["{id}"]
			.then()
			.statusCode(403)
			.extract()
			.body()
			.asString()


		assertEquals("Invalid CORS request", content)
	}


	private fun mockPerson() {
		personVO.firstName = "Wallace"
		personVO.lastName = "Junior"
		personVO.address = "Rua teste, 123"
		personVO.gender = "Male"
	}

}
