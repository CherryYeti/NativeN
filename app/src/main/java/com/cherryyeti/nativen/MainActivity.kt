@file:OptIn(ExperimentalMaterial3Api::class)

package com.cherryyeti.nativen

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.cherryyeti.nativen.ui.data.Navigation.Companion.bottomNavItems
import com.cherryyeti.nativen.ui.theme.NativeNTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.NavHostAnimatedDestinationStyle
import com.ramcosta.composedestinations.generated.NavGraphs
import com.ramcosta.composedestinations.spec.DestinationStyle
import com.ramcosta.composedestinations.utils.rememberDestinationsNavigator


class MainViewModel : ViewModel() {
    var title by mutableStateOf("Home")
        private set
    var showTopAppBar by mutableStateOf(true)
        private set
    var showBottomAppBar by mutableStateOf(true)
        private set
    var showInnerPadding by mutableStateOf(true)
        private set

    fun updateTitle(newTitle: String) {
        title = newTitle
    }

    fun updateTopAppBarVisibility(visible: Boolean) {
        showTopAppBar = visible
    }
    fun updateBottomAppBarVisibility(visible: Boolean) {
        showBottomAppBar = visible
    }
    fun updateInnerPaddingVisibility(visible: Boolean) {
        showInnerPadding = visible
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NativeNTheme {
                val viewModel: MainViewModel = viewModel()
                var selectedItemIndex by rememberSaveable { mutableIntStateOf(0) }
                val navController = rememberNavController()
                val navigator = navController.rememberDestinationsNavigator()

                navController.addOnDestinationChangedListener { _, destination, _ ->
                    Log.d("MainActivity", "Destination: ${destination.route}")
                    viewModel.updateTopAppBarVisibility(destination.route != "doujin_screen/{id}")
                    viewModel.updateBottomAppBarVisibility(destination.route != "doujin_screen/{id}")
                    viewModel.updateInnerPaddingVisibility(destination.route != "doujin_screen/{id}")

                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        if (viewModel.showTopAppBar) {
                            TopAppBar(
                                title = { Text(viewModel.title) },
                                actions = {
                                    if (selectedItemIndex == 0) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = "Search",
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                        Icon(
                                            imageVector = Icons.Default.Settings,
                                            contentDescription = "Search",
                                            modifier = Modifier.padding(end = 16.dp)
                                        )
                                    }
                                }
                            )
                        }
                    },
                    bottomBar = {
                        if(viewModel.showBottomAppBar){
                            NavigationBar {
                                bottomNavItems.forEachIndexed { index, item ->
                                    NavigationBarItem(
                                        selected = selectedItemIndex == index,
                                        onClick = {
                                            selectedItemIndex = index
                                            viewModel.updateTitle(item.title)
                                            navigator.navigate(item.route)
                                        },
                                        label = { Text(item.title) },
                                        alwaysShowLabel = false,
                                        icon = {
                                            Icon(
                                                imageVector = if (selectedItemIndex == index) item.selectedIcon else item.unselectedIcon,
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        }

                    }
                ) { innerPadding ->
                    val modifier = if (viewModel.showInnerPadding) {
                        Modifier.padding(innerPadding)
                    } else {
                        Modifier
                    }
                    DestinationsNavHost(
                        navController = navController,
                        navGraph = NavGraphs.root,
                        modifier = modifier,
                    )
                }
            }
        }
    }
}