package com.example.benchmark.Soal2

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import com.example.benchmark.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment


val pengguna: ClickerSys = ClickerSys()
@Composable
fun CatClickerMenu() {
    Box(
        modifier = Modifier.fillMaxSize()
        , contentAlignment = Alignment.Center
    ){
        Image(
            painter = painterResource(R.drawable.catbg),
            contentDescription = "catBackground",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                .width(150.dp)
                .height( 150.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
        ) { Text("A") }

        Image(
            painter = painterResource(R.drawable.catm),
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .padding(bottom = 50.dp),
            contentDescription = "catMenu",
        )


    }
}




@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CatClickerPreview(){
    CatClickerMenu()
}