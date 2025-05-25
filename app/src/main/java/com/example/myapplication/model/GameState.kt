package com.example.myapplication.model

// 游戏状态数据类
data class GameState(
    val score: Int = 0,
    val timeLeft: Int = 20,
    val isPlaying: Boolean = false,
    val molePositions: List<Boolean> = List(9) { false }
) 