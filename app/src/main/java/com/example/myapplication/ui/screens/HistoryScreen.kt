package com.example.myapplication.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.viewmodel.GameViewModel
import kotlinx.coroutines.delay

/**
 * 历史记录屏幕
 */
@Composable
fun HistoryScreen(
    viewModel: GameViewModel,
    onBack: () -> Unit
) {
    val history = viewModel.getScoreHistory()
    val surfaceColor = MaterialTheme.colorScheme.surface

    // 动画显示状态
    var showContent by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        showContent = false
        delay(100)
        showContent = true
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = surfaceColor
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 顶部栏
            TopAppBar(
                onBack = onBack,
                showContent = showContent
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 标题
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn() + slideInVertically(
                    initialOffsetY = { -50 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            ) {
                Text(
                    "历史得分榜",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 记录列表
            if (history.isEmpty()) {
                EmptyHistoryView(showContent)
            } else {
                ScoreHistoryList(history, showContent)
            }
        }
    }
}

@Composable
private fun TopAppBar(
    onBack: () -> Unit,
    showContent: Boolean
) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { -50 }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ElevatedButton(
                onClick = onBack,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.elevatedButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "返回",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("返回游戏")
            }
            
            // 历史记录图标
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "历史记录",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
private fun EmptyHistoryView(showContent: Boolean) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn() + slideInVertically(
            initialOffsetY = { 100 }
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "暂无游戏记录",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "快去玩一局吧！",
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreHistoryList(
    history: List<Int>,
    showContent: Boolean
) {
    AnimatedVisibility(
        visible = showContent,
        enter = fadeIn()
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 8.dp)
        ) {
            itemsIndexed(history) { index, score ->
                val isTopThree = index < 3
                val animDelay = index * 50L
                
                // 记录项目动画包装
                var showItem by remember { mutableStateOf(false) }
                
                LaunchedEffect(Unit) {
                    delay(animDelay)
                    showItem = true
                }
                
                AnimatedVisibility(
                    visible = showItem,
                    enter = fadeIn() + slideInVertically(
                        initialOffsetY = { 50 },
                        animationSpec = spring(
                            dampingRatio = Spring.DampingRatioMediumBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )
                ) {
                    ScoreItem(
                        score = score,
                        rank = index + 1,
                        isTopThree = isTopThree,
                        totalGames = history.size
                    )
                }
            }
        }
    }
}

@Composable
private fun ScoreItem(
    score: Int,
    rank: Int,
    isTopThree: Boolean,
    totalGames: Int
) {
    val rankColors = listOf(
        Color(0xFFFFD700), // 金
        Color(0xFFC0C0C0), // 银
        Color(0xFFCD7F32)  // 铜
    )
    
    val rankColor = when (rank) {
        1 -> rankColors[0]
        2 -> rankColors[1]
        3 -> rankColors[2]
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
    }
    
    val cardBackgroundColor = if (isTopThree) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
    } else {
        MaterialTheme.colorScheme.surface
    }
    
    Card(
        modifier = Modifier
            .padding(vertical = 6.dp, horizontal = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isTopThree) 4.dp else 2.dp
        ),
        border = if (isTopThree) BorderStroke(1.dp, rankColor.copy(alpha = 0.5f)) else null,
        colors = CardDefaults.cardColors(
            containerColor = cardBackgroundColor
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 排名显示
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(rankColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$rank",
                    color = rankColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            
            // 局数
            Text(
                text = "第${totalGames - rank + 1}局",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )
            
            // 分数
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isTopThree) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "奖杯",
                        tint = rankColor,
                        modifier = Modifier
                            .padding(end = 4.dp)
                            .size(18.dp)
                    )
                }
                
                Text(
                    text = "$score",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isTopThree) rankColor else MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = " 分",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
    }
} 