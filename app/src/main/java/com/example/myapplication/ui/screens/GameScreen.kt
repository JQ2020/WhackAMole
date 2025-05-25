package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.components.MoleHole
import com.example.myapplication.viewmodel.GameViewModel

/**
 * 游戏主屏幕
 */
@Composable
fun GameScreen(viewModel: GameViewModel) {
    val gameState by viewModel.gameState.collectAsState()
    val showDialog by viewModel.showDialog
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { viewModel.toggleHistory(true) }) {
                Text("历史记录")
            }
        }
        
        // 游戏信息显示
        Text(
            text = "得分: ${gameState.score}",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "剩余时间: ${gameState.timeLeft}秒",
            fontSize = 20.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        // 开始/重新开始按钮
        Button(
            onClick = { viewModel.startGame() },
            enabled = !gameState.isPlaying
        ) {
            Text(text = if (gameState.score == 0) "开始游戏" else "重新开始")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 游戏网格
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (row in 0..2) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        MoleHole(
                            isVisible = gameState.molePositions[index],
                            onClick = { viewModel.hitMole(index) }
                        )
                    }
                }
            }
        }
    }
    
    // 游戏结束对话框
    if (showDialog) {
        GameOverDialog(
            score = gameState.score,
            onDismiss = { viewModel.dismissDialog() },
            onReset = { viewModel.resetGame() }
        )
    }
}

/**
 * 游戏结束对话框
 */
@Composable
fun GameOverDialog(
    score: Int,
    onDismiss: () -> Unit,
    onReset: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("🎓 学霸老婆毕业啦！", 
                 fontSize = 24.sp, 
                 fontWeight = FontWeight.Bold,
                 color = Color(0xFFFF69B4)) // 粉红色
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("亲爱的老婆大人：", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("你的智慧得分：", fontSize = 18.sp)
                Text("$score 分", 
                     fontSize = 32.sp, 
                     fontWeight = FontWeight.Bold, 
                     color = Color(0xFFFFD700)) // 金色
                Spacer(modifier = Modifier.height(8.dp))
                Text("学士学位只是开始，", fontSize = 14.sp)
                Text("你的人生永远满分！💯", fontSize = 14.sp)
            }
        },
        confirmButton = {
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("确定", color = Color.White)
            }
        },
        containerColor = Color.White
    )
} 