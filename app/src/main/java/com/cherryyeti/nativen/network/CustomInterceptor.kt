package com.cherryyeti.nativen.network

import android.content.Context
import android.util.Log
import com.cherryyeti.nativen.data.Doujin
import com.cherryyeti.nativen.data.HomeDoujin
import com.google.gson.Gson
import com.ramcosta.composedestinations.generated.destinations.WebviewScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.jsoup.Jsoup
import java.io.IOException

class CustomInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = addHeadersToRequest(context, chain.request())
        Log.d("CustomInterceptor", newRequest.headers.toString())
        return chain.proceed(newRequest)
    }
}

fun addHeadersToRequest(context: Context, originalRequest: Request): Request {
    val sharedPreferences = context.getSharedPreferences("nativen", Context.MODE_PRIVATE)
    val userAgent = sharedPreferences.getString("userAgent", null)
    val cookies = sharedPreferences.getString("cookies", null)?.substring(1)

    return originalRequest.newBuilder().addHeader("User-Agent", userAgent ?: "")
        .addHeader("Cookie", cookies ?: "").build()
}

suspend fun customFetch(
    context: Context, url: String, destinationsNavigator: DestinationsNavigator
): String? {
    val client = OkHttpClient.Builder().addInterceptor(CustomInterceptor(context)).build()

    val request = Request.Builder().url(url).build()

    val requestWithHeaders = addHeadersToRequest(context, request)

    return try {
        val response = client.newCall(requestWithHeaders).execute()
        if (response.code == 403) {
            destinationsNavigator.navigate(WebviewScreenDestination())
            return null
        }
        if (response.isSuccessful) {
            response.body?.string()
        } else {
            null
        }
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}

suspend fun fetchDoujin(
    context: Context, url: String, destinationsNavigator: DestinationsNavigator
): Doujin? {
    val jsonResponse: String? = customFetch(context, url, destinationsNavigator)
    return jsonResponse?.let {
        Gson().fromJson(it, Doujin::class.java)
    }
}

suspend fun fetchDoujinList(
    context: Context, url: String, destinationsNavigator: DestinationsNavigator
): List<HomeDoujin>? {
    val response = customFetch(context, url, destinationsNavigator)
    return if (response != null) {
        val document = Jsoup.parse(response)
        val doujinElements = document.getElementsByClass("gallery")
        doujinElements.map { element ->
            HomeDoujin(
                id = element.getElementsByTag("a")[0].attr("href").split("/")[2].toInt(),
                thumbnail = element.getElementsByTag("img")[0].attr("data-src"),
                title = element.getElementsByClass("caption")[0].text(),
                width = element.getElementsByTag("img")[0].attr("width").toInt(),
                height = element.getElementsByTag("img")[0].attr("height").toInt()
            )

        }
    } else {
        return null
    }
}