package com.wallace.Library.integration.vo

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import jakarta.xml.bind.annotation.XmlRootElement
import org.springframework.hateoas.RepresentationModel

//@XmlRootElement
@JsonPropertyOrder("id", "address", "firstName", "lastName", "gender")
data class PersonVO (
    @Mapping("id")
    @field:JsonProperty("id")
    var id: Long = 0,
    @JsonProperty("first_name")
    var firstName: String = "",
    @JsonProperty("last_name")
    var lastName: String = "",
    var address: String = "",
    var gender: String = ""
)