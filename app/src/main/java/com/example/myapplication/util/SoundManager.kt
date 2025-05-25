package com.example.myapplication.util

import android.content.Context
import android.media.AudioAttributes
import android.media.SoundPool
import com.example.myapplication.R

/**
 * 音效管理类
 */
class SoundManager(private val context: Context) {
    
    private val soundPool: SoundPool = SoundPool.Builder()
        .setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build()
        )
        .build()
    
    private var hitSoundId: Int = 0
    private var missSoundId: Int = 0
    
    init {
        // 加载音效
        hitSoundId = soundPool.load(context, R.raw.hit, 1)
        missSoundId = soundPool.load(context, R.raw.miss, 1)
    }
    
    fun playHitSound() {
        soundPool.play(hitSoundId, 1f, 1f, 1, 0, 1f)
    }
    
    fun playMissSound() {
        soundPool.play(missSoundId, 1f, 1f, 1, 0, 1f)
    }
    
    fun release() {
        soundPool.release()
    }
} 