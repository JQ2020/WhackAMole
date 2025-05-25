package com.example.myapplication.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 地鼠洞组件
 * @param isVisible 是否显示地鼠
 * @param onClick 点击事件回调
 */
@Composable
fun MoleHole(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    // 洞的颜色
    val holeBrush = Brush.radialGradient(
        colors = listOf(
            Color(0xFF613613), // 深棕色中心
            Color(0xFF8B4513)  // 棕色边缘
        )
    )
    
    // 地鼠动画
    val animSpec = spring<Float>(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )
    
    // 地鼠出现/消失动画
    val moleOffsetY = remember { Animatable(50f) }
    val moleScale = remember { Animatable(0.7f) }
    
    // 点击效果动画
    var isPressed by remember { mutableStateOf(false) }
    val pressScale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(100),
        label = "pressScale"
    )
    
    // 地鼠出现/消失动画
    LaunchedEffect(isVisible) {
        if (isVisible) {
            // 地鼠出现
            moleOffsetY.snapTo(40f)
            moleScale.snapTo(0.8f)
            
            moleOffsetY.animateTo(
                targetValue = 10f,
                animationSpec = animSpec
            )
            moleScale.animateTo(
                targetValue = 1f,
                animationSpec = animSpec
            )
        } else {
            // 地鼠消失
            moleOffsetY.animateTo(
                targetValue = 50f,
                animationSpec = tween(200)
            )
            moleScale.animateTo(
                targetValue = 0.7f,
                animationSpec = tween(200)
            )
        }
    }
    
    // 最外层兜底背景，防止任何情况下洞口都是棕色
    Box(
        modifier = Modifier
            .size(90.dp)
            .background(Color(0xFF8B4513), shape = CircleShape), // 兜底棕色
        contentAlignment = Alignment.Center
    ) {
        // 洞口容器
        Box(
            modifier = Modifier
                .size(90.dp)
                .shadow(
                    elevation = 4.dp,
                    shape = CircleShape,
                    spotColor = Color.Black.copy(alpha = 0.3f)
                )
                .clip(CircleShape)
                .background(holeBrush)
                .clickable {
                    isPressed = true
                    onClick()
                }
                .scale(pressScale),
            contentAlignment = Alignment.Center
        ) {
            // 洞的边缘效果
            Box(
                modifier = Modifier
                    .size(85.dp)
                    .clip(CircleShape)
                    .border(
                        width = 3.dp,
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF613613).copy(alpha = 0.7f),
                                Color(0xFF8B4513).copy(alpha = 0.3f)
                            )
                        ),
                        shape = CircleShape
                    )
            )
            
            // 地鼠
            if (isVisible) {
                // 地鼠容器
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .offset(y = moleOffsetY.value.dp)
                        .scale(moleScale.value)
                ) {
                    // 地鼠身体
                    val bodyColor = Color(0xFFBB8760)
                    val faceColor = Color(0xFFDDA680)
                    
                    // 身体
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .align(Alignment.BottomCenter)
                            .clip(CircleShape)
                            .background(bodyColor)
                            .shadow(2.dp, CircleShape)
                    )
                    
                    // 脸部
                    Box(
                        modifier = Modifier
                            .size(45.dp)
                            .align(Alignment.BottomCenter)
                            .offset(y = (-8).dp)
                            .clip(CircleShape)
                            .background(faceColor)
                    )
                    
                    // 左耳朵
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .offset(x = (-15).dp, y = (-5).dp)
                            .align(Alignment.TopStart)
                            .clip(CircleShape)
                            .background(bodyColor)
                            .border(1.dp, bodyColor.copy(alpha = 0.7f), CircleShape)
                    )
                    
                    // 右耳朵
                    Box(
                        modifier = Modifier
                            .size(18.dp)
                            .offset(x = 15.dp, y = (-5).dp)
                            .align(Alignment.TopEnd)
                            .clip(CircleShape)
                            .background(bodyColor)
                            .border(1.dp, bodyColor.copy(alpha = 0.7f), CircleShape)
                    )
                    
                    // 左眼睛
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .offset(x = (-10).dp, y = 10.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                    
                    // 右眼睛
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .offset(x = 10.dp, y = 10.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color.Black)
                    )
                    
                    // 鼻子
                    Box(
                        modifier = Modifier
                            .size(width = 14.dp, height = 10.dp)
                            .offset(y = 25.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color(0xFFFF6B6B))
                    )
                    
                    // 腮红
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .offset(x = (-18).dp, y = 15.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color(0xFFFFA6A6).copy(alpha = 0.6f))
                    )
                    
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .offset(x = 18.dp, y = 15.dp)
                            .align(Alignment.Center)
                            .clip(CircleShape)
                            .background(Color(0xFFFFA6A6).copy(alpha = 0.6f))
                    )
                }
            }
            
            // 释放点击状态
            LaunchedEffect(isPressed) {
                if (isPressed) {
                    kotlinx.coroutines.delay(100)
                    isPressed = false
                }
            }
        }
    }
} 