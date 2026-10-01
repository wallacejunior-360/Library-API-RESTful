package com.wallace.Library.controller

import com.wallace.Library.data.vo.v1.PersonVO
import com.wallace.Library.services.PersonService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoint for the given person")
class PersonController {

    @Autowired
    private lateinit var service: PersonService

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE])
    @Operation(summary = "Get all people", description = "Returns all people",
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
        @RequestParam(value = "limit", defaultValue = "12") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String
    ) : ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        val sortDirection: Sort.Direction = if("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC

        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findAll(pageable))
    }

    @GetMapping(
        value = ["/findPersonByName/{firstName}"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(summary = "Get person by name", description = "Returns person by name",
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
    fun findByName(
        @PathVariable("firstName") firstName: String,
        @RequestParam(value = "page", defaultValue = "0") page: Int,
        @RequestParam(value = "limit", defaultValue = "12") size: Int,
        @RequestParam(value = "direction", defaultValue = "asc") direction: String
    ) : ResponseEntity<PagedModel<EntityModel<PersonVO>>> {
        val sortDirection: Sort.Direction = if("desc".equals(direction, ignoreCase = true)) Sort.Direction.DESC else Sort.Direction.ASC
        val pageable: Pageable = PageRequest.of(page, size, Sort.by(sortDirection, "firstName"))
        return ResponseEntity.ok(service.findByName(firstName, pageable))
    }

    @GetMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Get a person by id", description = "Return person info",
        tags= ["People"], responses = [
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
    fun findById(@PathVariable id: Long): PersonVO {
        return service.findById(id)
    }

    @PostMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Adds a new person", description = "Return person info",
        tags= ["People"], responses = [
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
    fun create(@RequestBody person: PersonVO): PersonVO {
        return service.create(person)
    }

    @PutMapping(
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Update a person", description = "Return person info",
        tags= ["People"], responses = [
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
    fun update(@RequestBody person: PersonVO): PersonVO {
        return service.update(person)
    }

    @PatchMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Disable person by id", description = "Return person info",
        tags= ["People"], responses = [
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
    fun disablePersonById(@PathVariable id: Long): PersonVO {
        return service.disablePerson(   id)
    }

    @DeleteMapping(
        value = ["/{id}"],
        produces = [MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE]
    )
    @Operation(
        summary = "Delete a person by id", description = "Delete person",
        tags= ["People"], responses = [
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