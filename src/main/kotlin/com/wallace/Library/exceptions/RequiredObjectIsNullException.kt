package com.wallace.Library.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(code = HttpStatus.NOT_FOUND)
class RequiredObjectIsNullException: RuntimeException {
    constructor() : super("It is not allowed to persist a null object")
    constructor(exception: String?) : super(exception)
}