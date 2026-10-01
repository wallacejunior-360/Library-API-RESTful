package com.wallace.Library.controller

import com.wallace.Library.data.vo.v1.BookVO
import com.wallace.Library.data.vo.v1.PersonVO
import com.wallace.Library.services.BookService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/books/v1")
@Tag(name = "Book", description = "Book API")
class BookController {

    @Autowired
    private lateinit var service: BookService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    @Operation(summary = "Get all books", description = "Returns all books",
        tags= ["People"], responses = [
            ApiResponse(
                description = "Success", responseCode = "200",
                content = [
                    Content(array = ArraySchema(schema = Schema(implementation = PersonVO::class)))
                ]
            ),
            ApiResponse(
                description = "No content", responseCode = "204",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Bad request", responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized request", responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not found", responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal server error", responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
        ]
    )
    fun findAll(
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "5") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String
    ): ResponseEntity<PagedModel<EntityModel<BookVO>>> {
        val sortDirection: Sort.Direction = if("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC

        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "title"))

        return ResponseEntity.ok(service.findAll(pageable))
    }

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Get a book by id", description = "Return book info",
        tags= ["Book"], responses = [
            ApiResponse(
                description = "Success", responseCode = "200",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
                ]
            ),
            ApiResponse(
                description = "No content", responseCode = "204",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Bad request", responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized request", responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not found", responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal server error", responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
        ]
    )
    fun findById(@PathVariable id: Long): BookVO {
        return service.findById(id)
    }

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Adds a new book", description = "Return book info",
        tags= ["Book"], responses = [
            ApiResponse(
                description = "Success", responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
                ]
            ),
            ApiResponse(
                description = "Bad request", responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized request", responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not found", responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal server error", responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
        ]
    )
    fun create(@RequestBody bookVO: BookVO): BookVO {
        return service.create(bookVO)
    }

    @PutMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Update a new book", description = "Return person info",
        tags= ["Book"], responses = [
            ApiResponse(
                description = "Success", responseCode = "201",
                content = [
                    Content(schema = Schema(implementation = PersonVO::class))
                ]
            ),
            ApiResponse(
                description = "Bad request", responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized request", responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not found", responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal server error", responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
        ]
    )
    fun update(@RequestBody bookVO: BookVO): BookVO {
        return service.update(bookVO)
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Delete a book by id", description = "Delete book",
        tags= ["Book"], responses = [
            ApiResponse(
                description = "No content", responseCode = "204",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Bad request", responseCode = "400",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Unauthorized request", responseCode = "401",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Not found", responseCode = "404",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
            ApiResponse(
                description = "Internal server error", responseCode = "500",
                content = [
                    Content(schema = Schema(implementation = Unit::class))
                ]
            ),
        ]
    )
    fun delete(@PathVariable id: Long): ResponseEntity<*> {
        service.delete(id)
        return ResponseEntity.noContent().build<Any>()
    }
}