package com.wallace.Library.unittests.mockito.services

import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.repository.PersonRepository
import com.wallace.Library.services.PersonService
import com.wallace.Library.unittests.mapper.MockPerson
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class PersonServiceTest {
    private lateinit var inputObject: MockPerson

    @InjectMocks
    private lateinit var personService: PersonService

    @Mock
    private lateinit var repository: PersonRepository

    @BeforeEach
    fun setUpMock() {
        inputObject = MockPerson()

        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findAll() {
        val list = inputObject.mockEntityList()

        `when`(repository.findAll()).thenReturn(list)

        val result = personService.findAll()

        assertNotNull(result)
        assertEquals(14, result.size)

        val personOne = result[1]

        assertNotNull(personOne)
        assertNotNull(personOne.key)
        assertNotNull(personOne.links)
        // println(result.links)
        assertTrue(personOne.links.toString().contains("</api/person/v1/1>;rel=\"self\""))
        assertEquals("Address Test1", personOne.address)
        assertEquals("First Name Test1", personOne.firstName)
        assertEquals("Last Name Test1", personOne.lastName)
        assertEquals("Female", personOne.gender)

        val personFour = result[4]

        assertNotNull(personFour)
        assertNotNull(personFour.key)
        assertNotNull(personFour.links)
        // println(result.links)
        assertTrue(personFour.links.toString().contains("</api/person/v1/4>;rel=\"self\""))
        assertEquals("Address Test4", personFour.address)
        assertEquals("First Name Test4", personFour.firstName)
        assertEquals("Last Name Test4", personFour.lastName)
        assertEquals("Male", personFour.gender)
    }

    @Test
    fun findById() {
        val person = inputObject.mockEntity(1)
        person.id = 1L
        `when`(repository.findById(1)).thenReturn(Optional.of(person))

        val result = personService.findById(1)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)
        // println(result.links)
        assertTrue(result.links.toString().contains("</api/person/v1/1>;rel=\"self\""))
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)

    }

    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1L

        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)

        val result = personService.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun createWithNullPerson() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java)
        {
            personService.create(null)
        }

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message

        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun update() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1L

        `when`(repository.findById(1)).thenReturn(Optional.of(entity))
        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)

        val result = personService.update(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertEquals("Address Test1", result.address)
        assertEquals("First Name Test1", result.firstName)
        assertEquals("Last Name Test1", result.lastName)
        assertEquals("Female", result.gender)
    }

    @Test
    fun updateWithNullPerson() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java)
        {
            personService.update(null)
        }

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message

        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun delete() {
        val entity = inputObject.mockEntity(1)

        `when`(repository.findById(1)).thenReturn(Optional.of(entity))

        personService.delete(1)
    }

}