package com.example.myapplication.viewmodel

import android.content.Context
import android.os.Vibrator
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.model.GameState
import com.example.myapplication.repository.ScoreRepository
import com.example.myapplication.util.SoundManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class GameViewModel(
    context: Context,
    private val scoreRepository: ScoreRepository,
    private val soundManager: SoundManager
) : ViewModel() {
    
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    
    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    
    // 是否显示结束游戏的对话框
    private val _showDialog = mutableStateOf(false)
    val showDialog: State<Boolean> = _showDialog
    
    // 是否显示历史记录
    private val _showHistory = mutableStateOf(false)
    val showHistory: State<Boolean> = _showHistory
    
    // 计时器任务
    private var timerJob: Job? = null
    
    /**
     * 开始游戏
     */
    fun startGame() {
        // 取消之前的计时器任务
        timerJob?.cancel()
        
        // 重置游戏状态
        _gameState.value = GameState(isPlaying = true)
        
        // 启动新的计时器
        runGameTimer()
    }
    
    /**
     * 游戏计时器
     */
    private fun runGameTimer() {
        // 存储计时器任务引用
        timerJob = viewModelScope.launch {
            while (_gameState.value.timeLeft > 0 && _gameState.value.isPlaying) {
                delay(1000)
                _gameState.value = _gameState.value.copy(
                    timeLeft = _gameState.value.timeLeft - 1,
                    molePositions = _gameState.value.molePositions.map { Random.nextBoolean() }
                )
                
                // 如果时间到了，结束游戏
                if (_gameState.value.timeLeft <= 0) {
                    endGame()
                }
            }
        }
    }
    
    /**
     * 点击地鼠
     */
    fun hitMole(position: Int) {
        if (!_gameState.value.isPlaying) return
        
        val currentPositions = _gameState.value.molePositions
        if (position < 0 || position >= currentPositions.size) return
        
        if (currentPositions[position]) {
            // 击中地鼠
            soundManager.playHitSound()
            vibrator.vibrate(50)
            _gameState.value = _gameState.value.copy(
                score = _gameState.value.score + 1,
                molePositions = _gameState.value.molePositions.toMutableList().apply {
                    this[position] = false
                }
            )
        } else {
            // 击空
            soundManager.playMissSound()
            vibrator.vibrate(150)
            _gameState.value = _gameState.value.copy(
                score = maxOf(0, _gameState.value.score - 2)
            )
        }
    }
    
    /**
     * 结束游戏
     */
    private fun endGame() {
        // 取消计时器
        timerJob?.cancel()
        timerJob = null
        
        _gameState.value = _gameState.value.copy(isPlaying = false)
        scoreRepository.saveScore(_gameState.value.score)
        _showDialog.value = true
    }
    
    /**
     * 关闭对话框
     */
    fun dismissDialog() {
        _showDialog.value = false
    }
    
    /**
     * 重置游戏
     */
    fun resetGame() {
        // 取消计时器
        timerJob?.cancel()
        timerJob = null
        
        _gameState.value = GameState(isPlaying = false)
        _showDialog.value = false
    }
    
    /**
     * 显示历史记录
     */
    fun toggleHistory(show: Boolean) {
        _showHistory.value = show
    }
    
    /**
     * 获取历史分数
     */
    fun getScoreHistory(): List<Int> {
        return scoreRepository.getScoreHistory().sortedDescending()
    }
    
    /**
     * ViewModel 销毁时清理资源
     */
    override fun onCleared() {
        super.onCleared()
        // 取消计时器
        timerJob?.cancel()
        timerJob = null
    }
    
    /**
     * ViewModel 工厂
     */
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
                val scoreRepository = ScoreRepository(context)
                val soundManager = SoundManager(context)
                return GameViewModel(context, scoreRepository, soundManager) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 