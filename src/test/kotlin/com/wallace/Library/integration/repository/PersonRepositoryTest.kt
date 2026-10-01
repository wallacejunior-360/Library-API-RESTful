package com.wallace.Library.integration.repository

import com.wallace.Library.integration.testcontainers.AbstractIntegrationTest
import com.wallace.Library.model.Person
import com.wallace.Library.repository.PersonRepository
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.MethodOrderer
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestMethodOrder
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit.jupiter.SpringExtension
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

@ExtendWith(SpringExtension::class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PersonRepositoryTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var repository: PersonRepository
    private lateinit var person: Person

    @BeforeAll
    fun setup() {
        person = Person()
    }

    @Test
    @Order(0)
    fun testFindByName() {
        var pageable: Pageable = PageRequest.of(0, 12, Sort.by(Sort.Direction.ASC, "firstName"))

        val page = repository.findByName("Ailis", pageable)

        assertFalse(page.content.isEmpty(), "A lista não deveria estar vazia!")

        val person = page.content[0]

        assertNotNull(person.id)
        assertNotNull(person.firstName)
        assertNotNull(person.lastName)
        assertNotNull(person.gender)
        assertNotNull(person.address)

        assertEquals("Ailis", person.firstName)
        assertEquals("Linnock", person.lastName)
        assertEquals("942 Mcguire Plaza", person.address)
        assertEquals("Female", person.gender)
        assertEquals(true, person.enabled)
    }

    @Test
    @Order(1)
    fun testDisablePerson() {
        repository.disablePerson(person.id)

        person = repository.findById(person.id).get()

        assertNotNull(person.id)

        assertNotNull(person.firstName)
        assertNotNull(person.lastName)
        assertNotNull(person.gender)
        assertNotNull(person.address)

        assertEquals("Ailis", person.firstName)
        assertEquals("Linnock", person.lastName)
        assertEquals("942 Mcguire Plaza", person.address)
        assertEquals("Female", person.gender)
        assertEquals(false, person.enabled)
    }
}