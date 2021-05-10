package com.example.mankala

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

class MenuViewModel : ViewModel() {

    private val _playerOneType = MutableLiveData<String>()
    val playerOneType: MutableLiveData<String>
        get() = _playerOneType

    private val _playerTwoType = MutableLiveData<String>()
    val playerTwoType: MutableLiveData<String>
        get() = _playerTwoType

    private val _playerOneMinMaxValue = MutableLiveData<Int>()
    val playerOneMinMaxValue: MutableLiveData<Int>
        get() = _playerOneMinMaxValue

    private val _playerTwoMinMaxValue = MutableLiveData<Int>()
    val playerTwoMinMaxValue: MutableLiveData<Int>
        get() = _playerTwoMinMaxValue

    init {
        _playerOneType.value = "Człowiek"
        _playerTwoType.value = "Człowiek"
        _playerOneMinMaxValue.value = 1
        _playerTwoMinMaxValue.value = 1
    }
}