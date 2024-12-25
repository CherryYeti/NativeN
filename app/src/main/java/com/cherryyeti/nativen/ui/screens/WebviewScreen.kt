package com.cherryyeti.nativen.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.webkit.CookieManager
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cookie
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.edit
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@SuppressLint("SetJavaScriptEnabled")
@Destination<RootGraph>()
@Composable
fun WebviewScreen(navigator: DestinationsNavigator, applyPadding: Boolean = true) {
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val webView = WebView(context)
                    val cookieManager = CookieManager.getInstance()
                    val cookies = cookieManager.getCookie("https://nhentai.net/")
                    val userAgent = webView.settings.userAgentString
                    Log.d("WebviewScreen", "Cookies: $cookies")
                    Log.d("WebviewScreen", "User Agent: $userAgent")

                    // Save cookies and user agent to SharedPreferences
                    val sharedPreferences =
                        context.getSharedPreferences("nativen", Context.MODE_PRIVATE)
                    sharedPreferences.edit {
                        putString("cookies", cookies)
                        putString("userAgent", userAgent)
                    }
                    navigator.navigateUp()
                },
                modifier = Modifier.padding(bottom = 80.dp)
            ) {
                Icon(Icons.Filled.Cookie, contentDescription = "Cookies")
            }
        }
    ) { paddingValues ->
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    webViewClient = object : WebViewClient() {
                        override fun onReceivedError(
                            view: WebView?,
                            request: WebResourceRequest?,
                            error: WebResourceError?
                        ) {
                            Log.e("WebviewScreen", "Error: ${error?.description}")
                        }
                    }
                    settings.javaScriptEnabled = true
                    loadUrl("https://nhentai.net/")
                }
            },
            modifier = if (applyPadding) Modifier.padding(paddingValues) else Modifier
        )
    }
}