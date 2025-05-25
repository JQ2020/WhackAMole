package com.example.myapplication.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    
    // 记分板动画
    val scoreAnimation = remember(gameState.score) {
        Animatable(0f)
    }
    
    LaunchedEffect(gameState.score) {
        if (gameState.score > 0) {  // 只在分数大于0时执行动画
            scoreAnimation.animateTo(
                targetValue = 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            scoreAnimation.animateTo(
                targetValue = 0f,
                animationSpec = tween(150)
            )
        }
    }
    
    // 计时器动画
    val timerProgress = remember(gameState.timeLeft) {
        gameState.timeLeft / 20f
    }
    
    // 背景颜色 - 渐变效果
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.surface,
            MaterialTheme.colorScheme.surfaceVariant
        )
    )
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .padding(16.dp)
    ) {
        // 历史记录按钮
        ElevatedButton(
            onClick = { viewModel.toggleHistory(true) },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 8.dp, end = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 4.dp
            )
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "历史记录",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text("历史记录")
        }
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // 游戏信息卡片
            GameInfoCard(
                score = gameState.score,
                timeLeft = gameState.timeLeft,
                timerProgress = timerProgress,
                isPlaying = gameState.isPlaying,
                scoreScale = 1f + scoreAnimation.value * 0.2f
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // 游戏网格 - 中心位置
            GameGrid(
                molePositions = gameState.molePositions,
                onMoleClick = { viewModel.hitMole(it) },
                isPlaying = gameState.isPlaying
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // 控制按钮区域
            GameControls(
                isPlaying = gameState.isPlaying,
                score = gameState.score,
                onStartClick = { viewModel.startGame() }
            )
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

@Composable
private fun GameInfoCard(
    score: Int,
    timeLeft: Int,
    timerProgress: Float,
    isPlaying: Boolean,
    scoreScale: Float
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 得分标签与分数分开显示
            Text(
                text = "得分",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // 分数显示
            Text(
                text = "$score",
                fontSize = 36.sp * (if (score > 0) scoreScale else 1f),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // 时间进度条
            Text(
                text = "剩余时间: $timeLeft 秒",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            LinearProgressIndicator(
                progress = { timerProgress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = when {
                    timeLeft > 10 -> MaterialTheme.colorScheme.primary
                    timeLeft > 5 -> Color(0xFFFFA500) // 橙色
                    else -> Color(0xFFFF4444) // 红色
                },
                trackColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
            
            // 游戏状态显示
            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val infiniteTransition = rememberInfiniteTransition(label = "timer")
                val alpha by infiniteTransition.animateFloat(
                    initialValue = 0.4f,
                    targetValue = 1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(500),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "timerAlpha"
                )
                
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "状态",
                    tint = if (isPlaying) 
                        MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                    else 
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isPlaying) "游戏进行中" else "游戏未开始",
                    fontSize = 12.sp,
                    color = if (isPlaying)
                        MaterialTheme.colorScheme.primary.copy(alpha = alpha)
                    else
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        }
    }
}

@Composable
private fun GameGrid(
    molePositions: List<Boolean>,
    onMoleClick: (Int) -> Unit,
    isPlaying: Boolean
) {
    val gridBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    
    // 网格容器
    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
            .border(2.dp, gridBorderColor, RoundedCornerShape(16.dp))
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            for (row in 0..2) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    for (col in 0..2) {
                        val index = row * 3 + col
                        
                        // 为每个洞添加动画效果
                        var currentVisibility by remember(index, molePositions[index]) {
                            mutableStateOf(false)
                        }
                        
                        LaunchedEffect(index, molePositions[index]) {
                            if (isPlaying) {
                                currentVisibility = molePositions[index]
                            }
                        }
                        
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(4.dp)
                                .shadow(2.dp, CircleShape)
                        ) {
                            MoleHole(
                                isVisible = currentVisibility,
                                onClick = { 
                                    if (isPlaying) {
                                        onMoleClick(index)
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GameControls(
    isPlaying: Boolean,
    score: Int,
    onStartClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        ElevatedButton(
            onClick = onStartClick,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.elevatedButtonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ),
            elevation = ButtonDefaults.elevatedButtonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 8.dp
            ),
            modifier = Modifier
                .height(56.dp)
                .widthIn(min = 200.dp)
        ) {
            Icon(
                imageVector = if (score == 0) Icons.Default.PlayArrow else Icons.Default.Refresh,
                contentDescription = if (score == 0) "开始游戏" else "重新开始",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (score == 0) "开始游戏" else "重新开始",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
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
        shape = RoundedCornerShape(24.dp),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "🎓 学霸老婆毕业啦！", 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF69B4), // 粉红色
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    "亲爱的老婆大人：", 
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "你的智慧得分：", 
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
                
                // 分数显示，添加脉冲动画
                val infiniteTransition = rememberInfiniteTransition(label = "score")
                val scaleAnim by infiniteTransition.animateFloat(
                    initialValue = 1f,
                    targetValue = 1.1f,
                    animationSpec = infiniteRepeatable(
                        animation = tween(800),
                        repeatMode = RepeatMode.Reverse
                    ),
                    label = "scoreScale"
                )
                
                Text(
                    "$score 分", 
                    fontSize = 32.sp, 
                    fontWeight = FontWeight.Bold, 
                    color = Color(0xFFFFD700), // 金色
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .graphicsLayer { 
                            scaleX = scaleAnim
                            scaleY = scaleAnim
                        }
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // 装饰线
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .height(1.dp)
                        .background(Color(0xFFFF69B4).copy(alpha = 0.3f))
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "学士学位只是开始，", 
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    "你的人生永远满分！💯", 
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF69B4)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .height(48.dp)
            ) {
                Text(
                    "确定", 
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = Color.White,
        modifier = Modifier
            .padding(16.dp)
    )
} 