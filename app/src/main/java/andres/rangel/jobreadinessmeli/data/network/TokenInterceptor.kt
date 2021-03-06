package andres.rangel.jobreadinessmeli.data.network

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class TokenInterceptor : Interceptor {

    private val className = "TokenInterceptor"
    private val grantType = "authorization_code"
    private val clientId = "3424956346656258"
    private val clientSecret = "ymhZqTiBND2LlEsd4ES66F00ZTcDEh17"
    private val redirectUri = "https://www.google.com/"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        //rewrite the request to add bearer token
        val newRequest: Request = chain.request().newBuilder()
            .header("Authorization", "Bearer $code")
            .build()
        return chain.proceed(newRequest)
    }

    // POST request to get a token
    fun getToken(code: String, answer: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val call = MeliApi.token.getToken(grantType, clientId, clientSecret, code, redirectUri)
            launch {
                if (call.isSuccessful) {
                    Log.i(className, "successful request to get token")
                    TokenInterceptor.code = call.body()?.token.toString()
                    answer(true)
                } else {
                    answer(false)
                }
            }
        }
    }

    companion object {
        var code = "APP_USR-3424956346656258-040517-5009c0ec0d5b7272413cf87aabdeb1d2-316674397"
    }

}