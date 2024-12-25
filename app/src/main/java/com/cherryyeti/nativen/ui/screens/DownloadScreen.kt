package com.cherryyeti.nativen.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator


@Destination<RootGraph>()
@Composable
fun DownloadScreen(destinationsNavigator: DestinationsNavigator) {


    return Text("Download Screen")
}