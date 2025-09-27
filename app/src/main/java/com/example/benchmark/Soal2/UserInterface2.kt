package com.example.benchmark.Soal2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.example.benchmark.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


val pengguna: ClickerSys = ClickerSys()
@Composable
fun CatClickerMenu() {
    var coins by remember { mutableStateOf(pengguna.click) }
    var power by remember { mutableStateOf(pengguna.clickPower) }
    var cost by remember { mutableStateOf(pengguna.upgradeCost) }
    var catPressed by remember { mutableStateOf(false) }
    LaunchedEffect(catPressed) {
        if (catPressed) {
            delay(150)
            catPressed = false
        }
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(R.drawable.catbg),
            contentDescription = "catBackground",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.8f))
                    .padding(12.dp)
            ) {
                Text("Your Coins")
                Text("$coins", color = Color.Green)
                Text("$power coins per tap")
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Tap the Cat!", color = Color.White)

                Image(
                    painter = painterResource(
                        if (catPressed) R.drawable.catopenmouth else R.drawable.catm
                    ),
                    contentDescription = "catMenu",
                    modifier = Modifier
                        .width(200.dp)
                        .height(200.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            catPressed = true
                            pengguna.clicker()
                            coins = pengguna.click
                            power = pengguna.clickPower
                            cost = pengguna.upgradeCost

                            CoroutineScope(Dispatchers.Main).launch {
                                delay(150)
                                catPressed = false
                            }
                        }
                )

                Text(
                    if (catPressed) "Meow!" else "Purr~",
                    color = Color.White
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Text("Give Me Your Coin")
                Text("Next upgrade: +2 coins per tap")

                Spacer(Modifier.height(8.dp))

                if (coins >= cost) {
                    Button(
                        onClick = {
                            pengguna.upgradeClicker()
                            coins = pengguna.click
                            power = pengguna.clickPower
                            cost = pengguna.upgradeCost
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green)
                    ) {
                        Text("Pay for $cost coins")
                    }
                } else {
                    val missing = cost - coins
                    Button(
                        onClick = { },
                        enabled = false,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray)
                    ) {
                        Text("Find $missing more coins")
                    }
                }
            }
        }
    }
}





@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatClickerPreview(){
    CatClickerMenu()
}