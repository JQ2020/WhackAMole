package com.example.myapplication.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
    Box(
        modifier = Modifier
            .size(80.dp)
            .clip(CircleShape)
            .background(Color(0xFF8B4513)) // 洞的颜色
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible) {
            // 地鼠形象
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(y = 10.dp) // 让地鼠稍微下沉
            ) {
                // 头部
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape)
                        .background(Color(0xFFA0522D)) // 棕色
                )
                // 左耳朵
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = (-16).dp, y = 0.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Color(0xFFA0522D))
                )
                // 右耳朵
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .offset(x = 16.dp, y = 0.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Color(0xFFA0522D))
                )
                // 左眼睛
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .offset(x = (-8).dp, y = 10.dp)
                        .align(Alignment.CenterStart)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
                // 右眼睛
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .offset(x = 8.dp, y = 10.dp)
                        .align(Alignment.CenterEnd)
                        .clip(CircleShape)
                        .background(Color.Black)
                )
                // 鼻子
                Box(
                    modifier = Modifier
                        .size(width = 12.dp, height = 8.dp)
                        .align(Alignment.BottomCenter)
                        .clip(CircleShape)
                        .background(Color(0xFFB22222)) // 深红色
                )
            }
        }
    }
} 