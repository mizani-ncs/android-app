package ncshack.samba.mizan.data.remote

interface ApiService {
    companion object {
        const val BASE_URL = "https://4c31-154-121-25-145.ngrok-free.app"
        const val GRAPHQL_URL = "$BASE_URL/graphql"
        const val UPLOAD_URL = "$BASE_URL/upload"
        const val DOWNLOAD_URL = "$BASE_URL/download"
    }
}