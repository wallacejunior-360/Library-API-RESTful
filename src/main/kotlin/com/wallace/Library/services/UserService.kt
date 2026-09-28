package com.wallace.Library.services

import com.wallace.Library.controller.BookController
import com.wallace.Library.controller.PersonController
import com.wallace.Library.data.vo.v1.BookVO
import com.wallace.Library.exceptions.RequiredObjectIsNullException
import com.wallace.Library.exceptions.ResourceNotFoundException
import com.wallace.Library.mapper.DozerMapper
import com.wallace.Library.model.Book
import com.wallace.Library.repository.BookRepository
import com.wallace.Library.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.logging.Logger

@Service
class UserService(@field:Autowired private val repository: UserRepository) : UserDetailsService {

    private val logger = Logger.getLogger(UserService::class.java.name)

    override fun loadUserByUsername(username: String): UserDetails {
        logger.info("Trying to find User with username: $username")

        var user = repository.findByUsername(username)
        return user ?: throw UsernameNotFoundException("Username $username not found")
    }
}








