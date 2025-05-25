package com.example.myapplication.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.screens.GameScreen
import com.example.myapplication.ui.screens.HistoryScreen
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.viewmodel.GameViewModel
import androidx.compose.material3.MaterialTheme

/**
 * 应用主界面入口
 */
@Composable
fun MainApp() {
    val context = LocalContext.current
    val viewModel: GameViewModel = viewModel(factory = GameViewModel.Factory(context))
    val showHistory by viewModel.showHistory
    
    MyApplicationTheme {
        // 使用 MaterialTheme 的默认背景色
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (showHistory) {
                HistoryScreen(
                    viewModel = viewModel,
                    onBack = { viewModel.toggleHistory(false) }
                )
            } else {
                GameScreen(viewModel = viewModel)
            }
        }
    }
} 