package com.example.mankala

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.supportActionBar?.hide()
        setContentView(R.layout.activity_main)
    }
}