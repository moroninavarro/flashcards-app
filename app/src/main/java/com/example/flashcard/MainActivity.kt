package com.example.flashcard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.example.flashcard.ui.theme.FlashCardTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            Greeting("")

            MyHomeScreen(onNavigate = {
                println("")
            })
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier.fillMaxSize() .padding(top=80.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "¡This FlashCard is to help you study! ",
            fontSize = 24.sp,
            modifier = modifier)
    }

}

//This is my composable function to be able to
//generate a button to create a Flashcard and
//to display a message and the button with a
//good layout.
@Composable
fun MyHomeScreen(onNavigate:() -> Unit){
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("¡Welcome to FlashCards App!")
        Button(onClick = onNavigate) {
            Text("Create Flashcard")
        }
    }
}

@Preview(
    showBackground = true)
@Composable
fun GreetingPreview() {
    FlashCardTheme {
        Greeting("Moroni")
    }
}