package com.example.benchmark.Soal2

import kotlin.math.roundToInt


class ClickerSys(){
    var click: Int = 0
    var clickPower: Int = 1
    var upgradeCost: Int = 10

    fun clicker(){
        click += clickPower
    }

    fun upgradeClicker(){
        if(click >= upgradeCost){
            click -= upgradeCost
            clickPower= (clickPower * 1.5).roundToInt()
            upgradeCost *= 2
        }
    }
}