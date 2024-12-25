package com.cherryyeti.nativen.ui.screens

import DoujinComponent
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.cherryyeti.nativen.data.HomeDoujin
import com.cherryyeti.nativen.network.fetchDoujinList
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import androidx.compose.runtime.saveable.rememberSaveable

@Destination<RootGraph>(start = true)
@Composable
fun HomeScreen(destinationsNavigator: DestinationsNavigator, applyPadding: Boolean = true) {
    val context = LocalContext.current
    var doujinData by rememberSaveable { mutableStateOf<List<HomeDoujin>>(emptyList()) }
    LaunchedEffect(context) {
        launch(Dispatchers.IO) {
            val home = fetchDoujinList(
                context,
                "https://nhentai.net/search/?q=language%3A%22english%22&sort=popular",
                destinationsNavigator
            )
            if (home != null) {
                doujinData = home
            }
        }
    }
    Surface {
        if (doujinData.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(doujinData) { doujin ->
                    val cleanedTitle = doujin.title.replace(Regex("\\[.*?\\]\\s*"), "")
                    DoujinComponent(
                        context,
                        doujin.thumbnail,
                        cleanedTitle,
                        doujin.width,
                        doujin.height,
                        doujin.id,
                        destinationsNavigator
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(36.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}