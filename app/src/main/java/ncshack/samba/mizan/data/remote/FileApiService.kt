package ncshack.samba.mizan.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readRawBytes
import io.ktor.http.ContentType
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.append
import io.ktor.http.contentLength
import java.io.File

class FileApiService(
    private val httpClient: HttpClient,
) {
    suspend fun uploadFile(
        token: String,
        file: File,
        sessionId: String,
    ): Result<String> = runCatching {
        val response = httpClient.post(ApiService.UPLOAD_URL) {
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(
                MultiPartFormDataContent(
                    formData {
                        append("sessionId", sessionId)
                        append(
                            key = "file",
                            value = file.readBytes(),
                            headers = Headers.build {
                                append(HttpHeaders.ContentType, ContentType.Application.OctetStream.toString())
                                append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                            },
                        )
                    }
                )
            )
        }
        if (response.status == HttpStatusCode.OK) {
            response.bodyAsText()
        } else {
            throw Exception("Upload failed: ${response.status}")
        }
    }

    suspend fun downloadFile(
        token: String,
        fileUrl: String,
        destination: File,
    ): Result<Unit> = runCatching {
        val response = httpClient.get(fileUrl) {
            header(HttpHeaders.Authorization, "Bearer $token")
        }
        if (response.status == HttpStatusCode.OK) {
            destination.writeBytes(response.readRawBytes())
        } else {
            throw Exception("Download failed: ${response.status}")
        }
    }
}
