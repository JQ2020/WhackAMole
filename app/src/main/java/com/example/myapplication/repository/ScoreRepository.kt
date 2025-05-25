package com.example.myapplication.repository

import android.content.Context

/**
 * 分数仓库类，用于处理历史分数的保存和获取
 */
class ScoreRepository(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "score_history"
        private const val KEY_HISTORY = "history"
    }
    
    /**
     * 保存分数到历史记录
     */
    fun saveScore(score: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val old = prefs.getString(KEY_HISTORY, "") ?: ""
        val newHistory = if (old.isEmpty()) "$score" else "$old,$score"
        prefs.edit().putString(KEY_HISTORY, newHistory).apply()
    }
    
    /**
     * 获取历史分数列表
     */
    fun getScoreHistory(): List<Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val str = prefs.getString(KEY_HISTORY, "") ?: ""
        return if (str.isEmpty()) emptyList() else str.split(",").mapNotNull { it.toIntOrNull() }
    }
} 