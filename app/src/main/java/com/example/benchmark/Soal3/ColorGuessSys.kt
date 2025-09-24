package com.example.benchmark.Soal3

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

class ColorGuessSys() {
    val realColor = listOf<Color>()
    var colorList = listOf("Red", "Green", "Blue", "Yellow", "Black", "White", "Purple", "Orange")




    fun questionGenerator(): String {
        return colorList.random()
    }
}