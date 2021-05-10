package com.example.mankala

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel(
    playerOneType: String,
    playerTwoType: String,
    val playerOneMinMaxValue: Int,
    val playerTwoMinMaxValue: Int
) : ViewModel() {

    private val _playerOne = MutableLiveData<Player>()
    val playerOne: MutableLiveData<Player>
        get() = _playerOne

    private val _playerTwo = MutableLiveData<Player>()
    val playerTwo: MutableLiveData<Player>
        get() = _playerTwo

    private val _bins = MutableLiveData<ArrayList<Int>>()
    val bins: MutableLiveData<ArrayList<Int>>
        get() = _bins

    private val _playerOneTurn = MutableLiveData<Boolean>()
    val playerOneTurn: MutableLiveData<Boolean>
        get() = _playerOneTurn

    var playing = true

    var chosenBin = -8
    var lastRecipient = -1

    init {
        _bins.value = arrayListOf(4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0)
        _playerOneTurn.value = true
        _playerOne.value = Player(playerOneType)
        _playerTwo.value = Player(playerTwoType)
    }

    fun game() {

//        if (_playerOneTurn.value!! && chosenBin > 5)
//            return
//        else if (!_playerOneTurn.value!! && chosenBin < 7)
//            return

        distributeStones()
        countTurn()
        setTurn(lastRecipient)
        lastRecipient = -1
        checkIfEnd()
    }

    private fun countTurn() {
        if (_playerOneTurn.value!!)
            _playerOne.value!!.turns += 1
        else
            _playerTwo.value!!.turns += 1
    }

    private fun distributeStones() {
        var giveawayPile = _bins.value!![chosenBin]
        _bins.value!![chosenBin] = 0
        var recipient = chosenBin + 1

        while (giveawayPile > 0) {
            if (_playerOneTurn.value!! && recipient == 13)
                recipient = 0
            else if (!_playerOneTurn.value!! && recipient == 6)
                recipient = 7

            _bins.value!![recipient] = _bins.value!![recipient].plus(1)
            _bins.value = _bins.value

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
        if (_playerOneTurn.value!! && _bins.value!![lastRecipient] == 1 && lastRecipient < 6 && _bins.value!![12 - lastRecipient] > 0) {
            _bins.value!![6] += _bins.value!![lastRecipient] + _bins.value!![12 - lastRecipient]
            _bins.value!![lastRecipient] = 0
            _bins.value!![12 - lastRecipient] = 0

            _bins.value = _bins.value

            _playerOneTurn.value = false
        } else if (!_playerOneTurn.value!! && _bins.value!![lastRecipient] == 1 && 6 < lastRecipient && lastRecipient < 13 && _bins.value!![12 - lastRecipient] > 0) {
            _bins.value!![13] += _bins.value!![lastRecipient] + _bins.value!![12 - lastRecipient]
            _bins.value!![lastRecipient] = 0
            _bins.value!![12 - lastRecipient] = 0

            _bins.value = _bins.value

            _playerOneTurn.value = true
        } else if (lastRecipient != 6 && lastRecipient != 13) {
            _playerOneTurn.value = !_playerOneTurn.value!!
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
        }
//        if (playerOne.value?.bins?.sum() == 0 || playerTwo.value?.bins?.sum() == 0) {
//            playing = false
//            playerOne.value?.well = playerOne.value?.well?.plus(playerOne.value?.bins?.sum()!!)!!
//            playerTwo.value?.well = playerTwo.value?.well?.plus(playerTwo.value?.bins?.sum()!!)!!
//
//            playerOne.value?.bins?.replaceAll { 0 }
//            playerTwo.value?.bins?.replaceAll { 0 }
//        }
    }
}