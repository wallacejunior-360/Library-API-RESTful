package com.wallace.Library.data.vo.v1

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel


@JsonPropertyOrder("id", "address", "firstName", "lastName", "gender")
data class PersonVO (
    @Mapping("id")
    @field:JsonProperty("id")
    var key: Long = 0,
    @JsonProperty("first_name")
    var firstName: String = "",
    @JsonProperty("last_name")
    var lastName: String = "",
    var address: String = "",
    @JsonIgnore
    var gender: String = ""
) : RepresentationModel<PersonVO>()