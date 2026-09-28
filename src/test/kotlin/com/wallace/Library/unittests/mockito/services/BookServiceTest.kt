package com.wallace.Library.unittests.mockito.services

import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.repository.BookRepository
import com.wallace.Library.services.BookService
import org.junit.jupiter.api.Assertions.*
import com.wallace.Library.unittests.mocks.MockBook
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class BookServiceTest {
    private lateinit var inputObject: MockBook

    @InjectMocks
    private lateinit var bookService: BookService

    @Mock
    private lateinit var repository: BookRepository

    @BeforeEach
    fun setup() {
        inputObject = MockBook()

        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun findAll() {
        val list = inputObject.mockEntityList()

        `when`(repository.findAll()).thenReturn(list)

        val result = bookService.findAll()

        assertNotNull(result)
        assertEquals(14, result.size)

        val bookOne = result[1]

        assertNotNull(bookOne)
        assertNotNull(bookOne.key)
        assertNotNull(bookOne.links)

        assertTrue(bookOne.links.toString().contains("/api/books/v1/1"))
        assertEquals("Title1", bookOne.title)
        assertEquals("Author1", bookOne.author)
        assertEquals(Date().toString(), bookOne.launchDate)
        assertEquals(1.0, bookOne.price)
    }

    @Test
    fun findById() {
        val book = inputObject.mockEntity(1)
        book.id = 1

        `when`(repository.findById(1)).thenReturn(Optional.of(book))

        val result = bookService.findById(1)

        assertNotNull(result)
        assertNotNull(result.key)
        assertNotNull(result.links)

        assertTrue(result.links.toString().contains("/api/books/v1/1>;rel=\"self\""))
        assertEquals("Title1", result.title)
        assertEquals("Author1", result.author)
        assertEquals(Date().toString(), result.launchDate)
        assertEquals(1.0, result.price)
    }

    @Test
    fun create() {
        val entity = inputObject.mockEntity(1)

        val persisted = entity.copy()
        persisted.id = 1L

        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)

        val result = bookService.create(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertEquals("Title1", result.title)
        assertEquals("Author1", result.author)
        //assertEquals(Date().toString(), result.launchDate)
        assertEquals(1.0, result.price)
    }

    @Test
    fun creteWithNullBook() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java)
        {
            bookService.create(null)
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

        `when`(repository.findById(1)).thenReturn(Optional.of(persisted))
        `when`(repository.save(entity)).thenReturn(persisted)

        val vo = inputObject.mockVO(1)

        val result = bookService.update(vo)

        assertNotNull(result)
        assertNotNull(result.key)
        assertEquals("Title1", result.title)
        assertEquals("Author1", result.author)
        assertEquals(Date().toString(), result.launchDate)
        assertEquals(1.0, result.price)
    }

    @Test
    fun updateWithNullBook() {
        val exception: Exception = assertThrows(RequiredObjectIsNullException::class.java)
        {
            bookService.update(null)
        }

        val expectedMessage = "It is not allowed to persist a null object"
        val actualMessage = exception.message
        assertTrue(actualMessage!!.contains(expectedMessage))
    }

    @Test
    fun delete() {
        val entity = inputObject.mockEntity(1)

        `when`(repository.findById(1)).thenReturn(Optional.of(entity))

        bookService.delete(1)
    }
}







