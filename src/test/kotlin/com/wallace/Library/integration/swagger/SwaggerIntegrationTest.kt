package com.wallace.Library.integration.swagger

import com.wallace.Library.integration.TestConfigs
import com.wallace.Library.integration.testcontainers.AbstractIntegrationTest
import io.restassured.RestAssured
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest(): AbstractIntegrationTest() {

	@Test
	fun shouldDisplaySwaggerUIPage() {
		val content = RestAssured.given()
			.basePath("/swagger-ui/index.html")
			.port(TestConfigs.SERVER_PORT)
				.`when`()
			.get()
			.then()
				.statusCode(200)
			.extract()
			.body()
				.asString()

		assertTrue(content.contains("swagger-ui"))
	}

}
