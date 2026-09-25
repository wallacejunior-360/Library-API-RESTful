package com.wallace.Library.data.vo.v1

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.github.dozermapper.core.Mapping
import org.springframework.hateoas.RepresentationModel
import java.util.Date

@JsonPropertyOrder("id", "title", "author", "price", "launch_date")
data class BookVO (
    @Mapping("id")
    @field:JsonProperty("id")
    var key: Long = 0,

    var author: String = "",

    @JsonProperty("launch_date")
    var launchDate: Date = Date(),

    var price: Double = 0.0,

    var title: String = "",
) : RepresentationModel<BookVO>()