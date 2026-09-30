package com.wallace.Library

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder

/*
import org.springframework.security.crypto.password.DelegatingPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder
*/

@SpringBootApplication
class LibraryApplication

fun main(args: Array<String>) {
	runApplication<LibraryApplication>(*args)

	/*
	val encoders: MutableMap<String, PasswordEncoder> = HashMap()
	val pbkdf2 = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8()

	encoders["pbkdf2"] = pbkdf2

	val passwordEncoder = DelegatingPasswordEncoder("pbkdf2", encoders)
	passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2)

	val result = passwordEncoder.encode("admin")
	println(result)

	 */

}
