package com.wallace.Library.services

import com.wallace.Library.controller.PersonController
import com.wallace.Library.data.vo.v1.PersonVO
import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.exceptions.ResourceNotFoundException
import com.wallace.Library.mapper.DozerMapper
import com.wallace.Library.model.Person
import com.wallace.Library.repository.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class PersonService {

    @Autowired
    private lateinit var personRepository: PersonRepository

    private val logger = Logger.getLogger(PersonService::class.java.name)

    fun findAll(): List<PersonVO> {
        logger.info("Trying to find all Persons")

        val people =  personRepository.findAll()

        val peopleVO: List<PersonVO> = DozerMapper.parseListObjects(people, PersonVO::class.java)

        for (personVO: PersonVO in peopleVO) {
            val withSelfRel = linkTo(PersonController::class.java)
                .slash(personVO.key).withSelfRel()
            personVO.add(withSelfRel)
        }

        return peopleVO
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

