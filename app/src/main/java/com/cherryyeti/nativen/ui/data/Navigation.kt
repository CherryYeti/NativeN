package com.cherryyeti.nativen.ui.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Tag
import androidx.compose.ui.graphics.vector.ImageVector
import coil3.compose.AsyncImagePainter
import com.ramcosta.composedestinations.generated.destinations.DownloadScreenDestination
import com.ramcosta.composedestinations.generated.destinations.FavoriteScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HistoryScreenDestination
import com.ramcosta.composedestinations.generated.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.destinations.TagScreenDestination
import com.ramcosta.composedestinations.navargs.DestinationsNavTypeSerializer
import com.ramcosta.composedestinations.navargs.NavTypeSerializer
import com.ramcosta.composedestinations.spec.Direction



class Navigation {
    data class BottomNavItem(
        val title: String,
        val selectedIcon: ImageVector,
        val unselectedIcon: ImageVector,
        val route: Direction,
    )

    companion object {
        val bottomNavItems = listOf(
            BottomNavItem(
                "Home",
                Icons.Filled.Home,
                Icons.Outlined.Home,
                HomeScreenDestination()
            ),
            BottomNavItem(
                "Downloads",
                Icons.Filled.Download,
                Icons.Outlined.Download,
                DownloadScreenDestination()
            ),
            BottomNavItem(
                "Favorites",
                Icons.Filled.Favorite,
                Icons.Outlined.FavoriteBorder,
                FavoriteScreenDestination()
            ),
            BottomNavItem(
                "Tags",
                Icons.Filled.Tag,
                Icons.Outlined.Tag,
                TagScreenDestination()
            ),
            BottomNavItem(
                "History",
                Icons.Filled.History,
                Icons.Outlined.History,
                HistoryScreenDestination()
            )
        )
    }
}