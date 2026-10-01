package com.wallace.Library.services

import com.wallace.Library.config.FileStorageConfig
import com.wallace.Library.exceptions.FileStorageException
import com.wallace.Library.exceptions.MyFileNotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Service
class FileStorageService @Autowired constructor(fileStorageConfig: FileStorageConfig) {

    // 1. Inicializa corretamente a variável membro da classe
    private val fileStorageLocation: Path = Paths.get(fileStorageConfig.uploadDir)
        .toAbsolutePath()
        .normalize()

    init {
        try {
            // 2. Cria fisicamente a pasta no disco caso ela não exista
            Files.createDirectories(fileStorageLocation)
        } catch (e: Exception) {
            throw FileStorageException("Could not create directory where the uploaded files will be stored", e)
        }
    }

    fun storageFile(file: MultipartFile): String {
        // Usa o operador elvis (?:) para garantir uma String caso originalFilename seja nulo
        val fileName = StringUtils.cleanPath(file.originalFilename ?: "")

        if (fileName.isBlank()) {
            throw FileStorageException("File name is invalid or empty.")
        }

        return try {
            if (fileName.contains(".."))
                throw FileStorageException("Sorry, $fileName contains invalid path sequence!")

            val targetLocation = fileStorageLocation.resolve(fileName)
            Files.copy(file.inputStream, targetLocation, StandardCopyOption.REPLACE_EXISTING)
            fileName
        } catch (e: Exception) {
            throw FileStorageException("Could not store file $fileName. Please, try again!", e)
        }
    }

    fun loadFileAsResource(fileName: String): Resource {
        return try {
            val filePath = fileStorageLocation.resolve(fileName).normalize()
            val resource: Resource = UrlResource(filePath.toUri())

            if (resource.exists()) resource
            else throw MyFileNotFoundException("File not found!")
        } catch (e: Exception) {
            throw MyFileNotFoundException("File not found $fileName!", e)
        }
    }
}
