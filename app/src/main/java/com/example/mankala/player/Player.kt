package com.example.mankala.player

import androidx.lifecycle.MutableLiveData

open class Player(val playerOne: Boolean) {

    var lastTurnStart: Long = System.currentTimeMillis()
    var lastTurnTime: Long = 0
    var totalTime: Long = 0

    var moves = MutableLiveData<Int>()
//    var myTurn = MutableLiveData<Boolean>()

    init {
        moves.value = 0
//        myTurn.value = playerOne
    }

    open fun makeMove(bins: ArrayList<Int>): Int {
        return -1
    }

    fun chooseBin(bin: Int, bins: ArrayList<Int>): Int {
        if (playerOne && bin > 5)
            return -1
        else if (!playerOne && bin < 7)
            return -1
        if (bins[bin] > 0) {
            val lastTurnEnd = System.currentTimeMillis()
            lastTurnTime = lastTurnEnd - lastTurnStart
            totalTime += lastTurnTime
//            moves.value = moves.value!!.plus(1)
            return bin
        }
        return -1
    }
}