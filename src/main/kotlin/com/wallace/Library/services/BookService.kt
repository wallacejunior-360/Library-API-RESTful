package com.wallace.Library.services

import com.wallace.Library.controller.BookController
import com.wallace.Library.controller.PersonController
import com.wallace.Library.data.vo.v1.BookVO
import com.wallace.Library.data.vo.v1.PersonVO
import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.exceptions.ResourceNotFoundException
import com.wallace.Library.mapper.DozerMapper
import com.wallace.Library.model.Book
import com.wallace.Library.repository.BookRepository
import com.wallace.Library.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class BookService {

    @Autowired
    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var assembler: PagedResourcesAssembler<Book>

    private val logger = Logger.getLogger(BookService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<BookVO>> {
        logger.info("Trying to find all Books")

        val books = bookRepository.findAll(pageable)

        return assembler.toModel(books) { book ->
            val vo = DozerMapper.parseObject(book, BookVO::class.java)

            EntityModel.of(vo).apply {
                add(linkTo(BookController::class.java).slash(vo.key).withSelfRel())
            }
        }
    }

    fun findById(id: Long): BookVO {
        logger.info("Trying to find Book with id: $id")

        var book = bookRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Book with id: $id not found") }

        val bookVO: BookVO = DozerMapper.parseObject(book, BookVO::class.java)
        val withSelfRel = linkTo(BookController::class.java)
            .slash(bookVO.key).withSelfRel()
        bookVO.add(withSelfRel)

        return bookVO
    }

    fun create(book: BookVO?): BookVO {
        logger.info("Trying to create Book")

        if (book == null) throw RequiredObjectIsNullException()

        var book: Book = DozerMapper.parseObject(book, Book::class.java)

        return DozerMapper.parseObject(bookRepository.save(book), BookVO::class.java)
    }

    fun update(book: BookVO?): BookVO {
        logger.info("Trying to update Book")

        if (book == null) throw RequiredObjectIsNullException()

        val entity = bookRepository
            .findById(book.key)
            .orElseThrow { ResourceNotFoundException("Book with key: ${book.key} not found") }

        entity.author = book.author
        entity.title = book.title
        entity.price = book.price
        entity.launchDate = book.launchDate

        return DozerMapper.parseObject(bookRepository.save(entity), BookVO::class.java)
    }

    fun delete(id: Long) {
        logger.info("Trying to delete Book with id: $id")

        val entity = bookRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Book with id: $id not found") }

        bookRepository.delete(entity)
    }
}








