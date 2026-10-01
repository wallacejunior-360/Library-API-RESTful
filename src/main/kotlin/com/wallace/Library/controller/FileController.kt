package com.wallace.Library.controller

import com.wallace.Library.data.vo.v1.UploadFileResponseVO
import com.wallace.Library.services.FileStorageService
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.logging.Level
import java.util.logging.Logger

@Tag(name = "File Endpoint")
@RestController
@RequestMapping("/api/file/v1")
class FileController {

    private val logger = Logger.getLogger(FileController::class.java.name)

    @Autowired
    private lateinit var fileStorageService: FileStorageService

    @PostMapping("/upload")
    fun uploadFile(@RequestParam("file") file: MultipartFile): UploadFileResponseVO {
        val fileName = fileStorageService.storageFile(file)

        val fileDownloadUri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/api/file/v1/upload/")
            .path(fileName)
            .toUriString()
        return UploadFileResponseVO(fileName, fileDownloadUri, file.contentType!!, file.size)
    }

    @PostMapping("/uploadMultipleFiles")
    fun uploadMultipleFiles(@RequestParam("files") files: Array<MultipartFile>): List<UploadFileResponseVO> {
        val uploadFIleResponseVOs = arrayListOf<UploadFileResponseVO>()

        for (file in files) {
            var uploadFIleResponseVO: UploadFileResponseVO = uploadFile(file)
            uploadFIleResponseVOs.add(uploadFIleResponseVO)
        }
        return uploadFIleResponseVOs
    }

    @GetMapping("/downloadFile/{fileName:.+}")
    fun uploadFile(@PathVariable fileName: String, request: HttpServletRequest): ResponseEntity<Resource> {
        val resource = fileStorageService.loadFileAsResource(fileName)

        var contentType = ""

        try {
            contentType = request.servletContext.getMimeType(resource.filename)
        } catch (e: Exception) {
            logger.log(Level.SEVERE, "Error while downloading file ${fileName}", e)
        }

        if (contentType.isBlank()) "application/octet-stream"

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION, """attachment; filename="${resource.filename}"""")
            .body(resource)
    }
}