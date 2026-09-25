package com.wallace.Library.unittests.mapper.mocks

import com.wallace.Library.data.vo.v1.BookVO
import com.wallace.Library.model.Book
import java.util.Date

class MockBook {

    fun mockEntity(): Book {
        return mockEntity(1)
    }

    fun mockVO(): BookVO {
        return mockVO(1)
    }

    fun mockEntityList(): ArrayList<Book> {
        val books: ArrayList<Book> = ArrayList<Book>()
        for (i in 0..13) {
            books.add(mockEntity(i))
        }

        return books
    }

    fun mockVOList(): ArrayList<BookVO> {
        val bookVOs: ArrayList<BookVO> = ArrayList<BookVO>()
        for (i in 0..13) {
            bookVOs.add(mockVO(i))
        }
        return bookVOs
    }

    fun mockEntity(number: Int): Book {
        val book = Book()
        book.id = number.toLong()
        book.author = "Author${number}"
        book.title = "Title${number}"
        book.launchDate = Date().toString()
        book.price = number.toDouble()

        return book
    }

    fun mockVO(number: Int): BookVO {
        val book = BookVO()
        book.key = number.toLong()
        book.author = "Author${number}"
        book.title = "Title${number}"
        book.launchDate = Date().toString()
        book.price = number.toDouble()

        return book
    }
}