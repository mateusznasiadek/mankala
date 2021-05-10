package com.example.mankala

class Player(val type:String = "Człowiek") {

    val bins = arrayListOf("4", "4", "4", "4", "4", "4")
    var well = "0"

    var totalTime: Long = 0
    var lastTurnTime: Long = 0

    var turns = 0

}