package com.example.mankala

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mankala.player.AlphaBetaPlayer
import com.example.mankala.player.MinMaxPlayer
import com.example.mankala.player.Player
import com.example.mankala.player.notifyObserver

class GameViewModel(
    val playerOneType: String,
    val playerTwoType: String,
    playerOneMinMaxValue: Int,
    playerTwoMinMaxValue: Int,
    isPlayerOneFirstMoveRandom: Boolean = false,
    isPlayerTwoFirstMoveRandom: Boolean = false
) : ViewModel() {

    private val _bins = MutableLiveData<ArrayList<Int>>()
    val bins: MutableLiveData<ArrayList<Int>>
        get() = _bins

    private var playing = true
    var chosenBin = -8
    private var lastRecipient = -1
    var gameOver = MutableLiveData<Boolean>()

    var playerOne = Player(true)
    var playerTwo = Player(false)

    val playerOneTurn = MutableLiveData<Boolean>()

    init {
        _bins.value = arrayListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)

        if (playerOneType == "Algorytm min-max")
            playerOne = MinMaxPlayer(true, playerOneMinMaxValue, isPlayerOneFirstMoveRandom)
        else if (playerOneType == "Algorytm alpha-beta cięć")
            playerOne = AlphaBetaPlayer(true, playerOneMinMaxValue, isPlayerOneFirstMoveRandom)

        if (playerTwoType == "Algorytm min-max")
            playerTwo = MinMaxPlayer(false, playerTwoMinMaxValue,isPlayerTwoFirstMoveRandom)
        else if (playerTwoType == "Algorytm alpha-beta cięć")
            playerTwo = AlphaBetaPlayer(false, playerTwoMinMaxValue, isPlayerTwoFirstMoveRandom)

        gameOver.value = false
        playerOneTurn.value = true
    }

    fun game() {

//        if (playerOne.myTurn.value!! && chosenBin > 5)
//            return
//        else if (playerTwo.myTurn.value!! && chosenBin < 7)
//            return
        if (playerOneTurn.value!! && chosenBin > 5)
            return
        else if (!playerOneTurn.value!! && chosenBin < 7)
            return
        else if (chosenBin < 0)
            return

        distributeStones()
        countTurn()
        setTurn(lastRecipient)
    }

    private fun countTurn() {
        if (playerOneTurn.value!!)
            playerOne.moves.value = playerOne.moves.value!!.plus(1)
        else
            playerTwo.moves.value = playerTwo.moves.value!!.plus(1)
    }

    private fun distributeStones() {
        var giveawayPile = _bins.value!![chosenBin]
        _bins.value!![chosenBin] = 0
        var recipient = chosenBin + 1

        while (giveawayPile > 0) {
            if (playerOneTurn.value!! && recipient == 13)
                recipient = 0
            else if (!playerOneTurn.value!! && recipient == 6)
                recipient = 7

            _bins.value!![recipient] = _bins.value!![recipient].plus(1)
            _bins.notifyObserver()

            giveawayPile -= 1

            if (giveawayPile == 0)
                lastRecipient = recipient
            else {
                recipient += 1
                if (recipient > 13)
                    recipient = 0
            }
        }
    }

    private fun setTurn(lastRecipient: Int) {
        if (playerOneTurn.value!! && _bins.value!![lastRecipient] == 1 && lastRecipient < 6 && _bins.value!![12 - lastRecipient] > 0) {
            _bins.value!![6] += _bins.value!![lastRecipient] + _bins.value!![12 - lastRecipient]
            _bins.value!![lastRecipient] = 0
            _bins.value!![12 - lastRecipient] = 0


            _bins.value = _bins.value

            this.lastRecipient = -1
            checkIfEnd()
            if (playing) {
                playerOneTurn.value = false
//                playerTwo.myTurn.value = true
            }

        } else if (!playerOneTurn.value!! && _bins.value!![lastRecipient] == 1 && 6 < lastRecipient && lastRecipient < 13 && _bins.value!![12 - lastRecipient] > 0) {
            _bins.value!![13] += _bins.value!![lastRecipient] + _bins.value!![12 - lastRecipient]
            _bins.value!![lastRecipient] = 0
            _bins.value!![12 - lastRecipient] = 0

            _bins.value = _bins.value

            this.lastRecipient = -1
            checkIfEnd()

            if (playing) {
//                playerTwo.myTurn.value = false
                playerOneTurn.value = true
            }
        } else if (lastRecipient != 6 && lastRecipient != 13) {
            this.lastRecipient = -1
            checkIfEnd()

            if (playing) {
                playerOneTurn.value = !playerOneTurn.value!!
//                if (playerOneTurn.value!!) {
//                    playerOne.myTurn.value = !playerOne.myTurn.value!!
//                    playerTwo.myTurn.value = !playerTwo.myTurn.value!!
//                } else {
//                    playerTwo.myTurn.value = !playerTwo.myTurn.value!!
//                    playerOne.myTurn.value = !playerOne.myTurn.value!!
//                }

            }
        } else {
            this.lastRecipient = -1
            checkIfEnd()

            if (playing) {
//                if (!playerOneTurn.value!!)
                    playerOneTurn.notifyObserver()
//                } else {
//                    playerOne.myTurn.notifyObserver()
//                }
            }
        }

    }

    private fun checkIfEnd() {
        var sideOne = 0
        var sideTwo = 0
        var i = 0
        while (i < 6) {
            sideOne += _bins.value!![i]
            sideTwo += _bins.value!![i + 7]
            i += 1
        }

        if (sideOne == 0 || sideTwo == 0) {
            playing = false
            _bins.value!![6] += sideOne
            _bins.value!![13] += sideTwo

            i = 0
            while (i < 6) {
                _bins.value!![i] = 0
                _bins.value!![i + 7] = 0
                i += 1
            }
            _bins.value = _bins.value
            gameOver.value = true
        }
    }
}