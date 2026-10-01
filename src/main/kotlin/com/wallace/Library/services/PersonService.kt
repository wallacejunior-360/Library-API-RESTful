package com.wallace.Library.services

import com.wallace.Library.controller.PersonController
import com.wallace.Library.data.vo.v1.PersonVO
import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.exceptions.ResourceNotFoundException
import com.wallace.Library.mapper.DozerMapper
import com.wallace.Library.model.Person
import com.wallace.Library.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PagedResourcesAssembler
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.PagedModel
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var personRepository: PersonRepository

    @Autowired
    private lateinit var assembler: PagedResourcesAssembler<Person>

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Trying to find all Persons")

        // 1. Busca a página de entidades do repositório
        val people = personRepository.findAll(pageable)

        // 2. Utiliza o assembler para converter a Page de Entidade diretamente para PagedModel<EntityModel<PersonVO>>
        return assembler.toModel(people) { person ->
            // Converte a entidade individual para VO
            val vo = DozerMapper.parseObject(person, PersonVO::class.java)

            // Cria o EntityModel e adiciona o link HATEOAS individual
            EntityModel.of(vo).apply {
                add(linkTo(PersonController::class.java).slash(vo.key).withSelfRel())
            }
        }
    }

    fun findByName(firstName: String, pageable: Pageable): PagedModel<EntityModel<PersonVO>> {
        logger.info("Trying to find all Persons")

        // 1. Busca a página de entidades do repositório
        val people = personRepository.findByName(firstName, pageable)

        // 2. Utiliza o assembler para converter a Page de Entidade diretamente para PagedModel<EntityModel<PersonVO>>
        return assembler.toModel(people) { person ->
            // Converte a entidade individual para VO
            val vo = DozerMapper.parseObject(person, PersonVO::class.java)

            // Cria o EntityModel e adiciona o link HATEOAS individual
            EntityModel.of(vo).apply {
                add(linkTo(PersonController::class.java).slash(vo.key).withSelfRel())
            }
        }
    }

    fun findById(id: Long): PersonVO {
        logger.info("Trying to find Person with id: $id")

        var person = personRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Person with id: $id not found") }

        val personVO: PersonVO =  DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java)
            .slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    @Transactional
    fun disablePerson(id: Long): PersonVO {
        logger.info("Disabling one Person with id: $id")

        personRepository.disablePerson(id)

        var person = personRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Person with id: $id not found") }

        val personVO: PersonVO =  DozerMapper.parseObject(person, PersonVO::class.java)
        val withSelfRel = linkTo(PersonController::class.java)
            .slash(personVO.key).withSelfRel()
        personVO.add(withSelfRel)

        return personVO
    }

    fun create(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()

        logger.info("Trying to create Person with name: ${person.firstName}")

        var entity: Person = DozerMapper.parseObject(person, Person::class.java)

        return DozerMapper.parseObject(personRepository.save(entity), PersonVO::class.java)
    }

    fun update(person: PersonVO?): PersonVO {
        if (person == null) throw RequiredObjectIsNullException()

        logger.info("Trying to update Person with id: ${person.key}")

        val entity = personRepository
            .findById(person.key)
            .orElseThrow { ResourceNotFoundException("Person with id: ${person.key} not found") }

        entity.firstName = person.firstName
        entity.lastName = person.lastName
        entity.address = person.address
        entity.gender = person.gender

        return DozerMapper.parseObject(personRepository.save(entity), PersonVO::class.java)
    }

    fun delete(id: Long) {
        logger.info("Trying to delete Person with id: $id")

        val person = personRepository
            .findById(id)
            .orElseThrow { ResourceNotFoundException("Person with id: $id not found") }

        personRepository.delete(person)
    }

}

