package com.cherryyeti.nativen.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootGraph


@Destination<RootGraph>()
@Composable
fun TagScreen() {
    return Text("Tag Screen")
}