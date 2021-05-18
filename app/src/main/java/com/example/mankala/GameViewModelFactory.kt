package com.example.mankala

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class GameViewModelFactory(
    private val playerOneType: String,
    private val playerTwoType: String,
    private val playerOneMinMaxVal: Int,
    private val playerTwoMinMaxVal: Int,
    private val isPlayerOneFirstMoveRandom: Boolean = false,
    private val isPlayerTwoFirstMoveRandom: Boolean = false
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameViewModel::class.java)) {
            return GameViewModel(
                playerOneType,
                playerTwoType,
                playerOneMinMaxVal,
                playerTwoMinMaxVal,
                isPlayerOneFirstMoveRandom,
                isPlayerTwoFirstMoveRandom
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}