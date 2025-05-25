package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.GameViewModel

/**
 * 历史记录屏幕
 */
@Composable
fun HistoryScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit
) {
    val history = viewModel.getScoreHistory()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .wrapContentSize(Alignment.Center),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            Button(onClick = onBack) {
                Text("返回游戏")
            }
        }
        
        Text(
            "历史得分", 
            fontSize = 24.sp, 
            fontWeight = FontWeight.Bold, 
            modifier = Modifier.padding(8.dp)
        )
        
        if (history.isEmpty()) {
            Text("暂无记录")
        } else {
            history.reversed().forEachIndexed { idx, score ->
                Text(
                    "第${history.size-idx}局：$score 分", 
                    fontSize = 18.sp, 
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
} 