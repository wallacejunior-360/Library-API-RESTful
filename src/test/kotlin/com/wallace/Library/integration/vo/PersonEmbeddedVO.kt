package com.wallace.Library.integration.vo

import com.fasterxml.jackson.annotation.JsonProperty

class PersonEmbeddedVO {

    @JsonProperty("personVOList")
    var persons: List<PersonVO> ?= null
}
