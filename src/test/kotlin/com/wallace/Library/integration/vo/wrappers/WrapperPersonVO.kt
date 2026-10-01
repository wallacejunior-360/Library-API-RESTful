package com.wallace.Library.integration.vo.wrappers

import com.fasterxml.jackson.annotation.JsonProperty
import com.wallace.Library.integration.vo.PersonEmbeddedVO

class WrapperPersonVO {

    @JsonProperty("_embedded")
    var embedded: PersonEmbeddedVO ?= null
}