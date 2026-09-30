package com.wallace.Library.integration.vo

import jakarta.xml.bind.annotation.XmlRootElement

@XmlRootElement
data class AccountCredentialsVO (
    var username: String? = null,
    var password: String? = null
)