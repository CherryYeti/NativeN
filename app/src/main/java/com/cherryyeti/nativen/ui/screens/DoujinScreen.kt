package com.cherryyeti.nativen.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.cherryyeti.nativen.data.Doujin
import com.cherryyeti.nativen.data.dummyDoujin
import com.cherryyeti.nativen.network.fetchDoujin
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Destination<RootGraph>()
@Composable
fun DoujinScreen(id: Int, destinationsNavigator: DestinationsNavigator) {
    val context = LocalContext.current
    var doujinData by rememberSaveable { mutableStateOf<Doujin>(dummyDoujin) }

    LaunchedEffect(context) {
        launch(Dispatchers.IO) {
            val home = fetchDoujin(
                context, "https://nhentai.net/api/gallery/${id}", destinationsNavigator
            )
            if (home != null) {
                doujinData = home
            }
        }
    }
    if (doujinData != dummyDoujin) {
        val imageUrl =
            "https://t.nhentai.net/galleries/${doujinData.media_id}/thumb.${if (doujinData.images.thumbnail.t == "j") "jpg" else "png"}"
        val painter = rememberAsyncImagePainter(imageUrl)
        val painterState by painter.state.collectAsState()
        Box(modifier = Modifier.fillMaxSize()) {
            if (painterState is AsyncImagePainter.State.Loading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(doujinData.images.thumbnail.w.toFloat() / doujinData.images.thumbnail.h.toFloat())
                        .align(Alignment.Center)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .size(24.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(doujinData.images.thumbnail.w.toFloat() / doujinData.images.thumbnail.h.toFloat())
            ) {
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(doujinData.images.thumbnail.w.toFloat() / doujinData.images.thumbnail.h.toFloat())
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                    MaterialTheme.colorScheme.background
                                )
                            )
                        )
                )
            }

            Box(modifier = Modifier.fillMaxSize()) {
                Column {
                    TopAppBar(
                        title = {
                            Text("")
                        },
                        navigationIcon = {
                            IconButton(onClick = { destinationsNavigator.navigateUp() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Back",
                                )
                            }
                        },
                        actions = {
                            Icon(
                                imageVector = Icons.Outlined.Download,
                                contentDescription = "Download",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                            Icon(
                                imageVector = Icons.Outlined.Favorite,
                                contentDescription = "Favorite",
                                modifier = Modifier.padding(end = 16.dp)
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0f)
                        ),
                    )
                    LazyColumn {
                        if (doujinData != dummyDoujin) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                ) {
                                    Image(
                                        painter = painter,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(150.dp)
                                            .aspectRatio(doujinData.images.thumbnail.w.toFloat() / doujinData.images.thumbnail.h.toFloat())
                                            .clip(MaterialTheme.shapes.medium)
                                    )
                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            text = doujinData.title.pretty,
                                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                            modifier = Modifier.padding(start = 16.dp)
                                        )
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Icon(
                                                imageVector = Icons.Outlined.Brush,
                                                contentDescription = "Favorite",
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                            Text(
                                                modifier = Modifier.padding(start = 4.dp),
                                                text = doujinData.tags.find { it.type == "artist" }?.name
                                                    ?: "Unknown Artist"
                                            )
                                        }
                                        Row(modifier = Modifier.fillMaxWidth()) {
                                            Icon(
                                                imageVector = Icons.Outlined.CalendarToday,
                                                contentDescription = "Upload Date",
                                                modifier = Modifier.padding(start = 16.dp)
                                            )
                                            Text(
                                                modifier = Modifier.padding(start = 4.dp),
                                                text = SimpleDateFormat(
                                                    "yyyy-MM-dd", Locale.getDefault()
                                                ).format(
                                                    Date(doujinData.upload_date * 1000)
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                            item {
                                TagFlowRows(doujinData)
                            }
                            item {
                                PagePreviews(doujinData)
                            }
                        } else {
                            item {
                                CircularProgressIndicator(
                                    modifier = Modifier
                                        .align(Alignment.CenterHorizontally)
                                        .size(36.dp),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    } else {
        Row {
            Box(
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
@Composable
fun PagePreviews(doujinData: Doujin) {
    val pageUrls = remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(doujinData) {
        val urls = doujinData.images.pages.mapIndexed { index, element ->
            "https://t1.nhentai.net/galleries/${doujinData.media_id}/${index + 1}t.${if (element.t == "j") "jpg" else "png"}"
        }
        pageUrls.value = urls
    }

    Column {
        pageUrls.value.forEach { url ->
            val painter = rememberAsyncImagePainter(url)
            val painterState by painter.state.collectAsState()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                if (painterState is AsyncImagePainter.State.Loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .align(Alignment.Center)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(24.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Image(
                    painter = painter,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun TagFlowRows(doujinData: Doujin) {
    val tagTypes = doujinData.tags.map { it.type }.distinct()
    val tagsByType = doujinData.tags.groupBy { it.type }

    tagTypes.forEach { type ->
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = type, modifier = Modifier.padding(top = 16.dp, end = 8.dp)
            )
            FlowRow(
                modifier = Modifier.padding(4.dp),
            ) {
                tagsByType[type]?.forEach { tag ->
                    SuggestionChip(modifier = Modifier.padding(2.dp),
                        onClick = { /* Handle click event */ },
                        label = { Text(tag.name) })
                }
            }
        }
    }
}