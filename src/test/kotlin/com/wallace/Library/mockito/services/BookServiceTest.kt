package com.wallace.Library.mockito.services

import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.repository.BookRepository
import com.wallace.Library.services.BookService
import org.junit.jupiter.api.Assertions.*
import com.wallace.Library.unittests.mapper.mocks.MockBook
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertNotNull
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date

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
}







