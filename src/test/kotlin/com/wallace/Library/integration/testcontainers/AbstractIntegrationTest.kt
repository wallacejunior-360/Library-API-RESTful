package com.wallace.Library.integration.testcontainers

import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.core.env.MapPropertySource
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.lifecycle.Startables
import org.testcontainers.mysql.MySQLContainer
import java.util.stream.Stream

@ContextConfiguration(initializers = [AbstractIntegrationTest.Initializer::class])
open class AbstractIntegrationTest {

    internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            startContainers()

            val environment = applicationContext.environment
            val testcontainers = MapPropertySource(
                "testcontainers", createConnectionConfiguration()
            )

            environment.propertySources.addFirst(testcontainers)
        }

        companion object {

            private val mysql: MySQLContainer = MySQLContainer("mysql:8.0.36")

            private fun startContainers() {
                Startables.deepStart(Stream.of(mysql)).join()
            }

            private fun createConnectionConfiguration(): MutableMap<String, Any> {
                return mutableMapOf(
                    "spring.datasource.url" to mysql.jdbcUrl, // Kotlin mapeia getJdbcUrl() para property se o import estiver correto
                    "spring.datasource.username" to mysql.username,
                    "spring.datasource.password" to mysql.password
                )
            }
        }
    }
}