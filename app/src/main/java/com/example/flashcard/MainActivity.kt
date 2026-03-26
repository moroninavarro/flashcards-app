package com.example.flashcard



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import com.example.flashcard.ui.theme.FlashCardTheme
import androidx.compose.ui.Alignment
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardTheme {

                val flashcards = remember { mutableStateListOf<Flashcard>()}

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                )
                {
                    composable("home") {
                        MyHomeScreen(
                            onCreateClick = {
                            navController.navigate("create")
                        },
                            onStudyClick = {
                                navController.navigate("study")
                            }
                        )
 //Route for my createScreen
                    }
                    composable("create") {
                        MyCreateScreen(
                            onNavigate = { navController.popBackStack() },
                        flashcards = flashcards
                        )
                    }
//Route for my StudyScreen
                    composable ("study"){
                        MyStudyScreen (
                            flashcards = flashcards,
                            onNavigate = { navController.popBackStack()
                        })
                    }
                }
            }
        }
    }
}


// A Reusable Button to be able to call it in different
// screens in my code
@Composable
fun ReusableButton(
    text: String,
    onClick: () -> Unit,
    containerColor: Color,
    modifier: Modifier
){
 Button(
     onClick = onClick,
     modifier = modifier,
     colors = ButtonDefaults.buttonColors(
         containerColor = containerColor
     )
 ) {
     Text(text = text, color = Color.White)
 }
}



//This is my composable function to be able to
//generate a button to create a Flashcard and
//to display a message and the button with a
//good layout.
@Composable
fun MyHomeScreen(onCreateClick:() -> Unit,
                 onStudyClick: () -> Unit){
    Scaffold(
        containerColor = Color(0xFFAABEE3)
    ){
        paddingValues ->

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("¡Welcome to FlashCards App!", fontSize = 20.sp)
            Text("Select an option to continue")
            ReusableButton("CREATE", onCreateClick, Color(0xFF1A1616), modifier = Modifier)
            ReusableButton("STUDY", onStudyClick, Color(0xFF1A1616), modifier = Modifier)
        }
    }
}



//My composable function for my Study Screen, displaying buttons and the list of the flashcards
@Composable
fun MyStudyScreen(onNavigate: () -> Unit, flashcards: List<Flashcard>){
    var currentIndex by remember { mutableIntStateOf(0) }
    var showAnswer by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text("Study Time", fontSize = 30.sp)
        if (flashcards.isNotEmpty()){
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(200.dp)
                .background(Color(0xFFF2DFDF), RoundedCornerShape(16.dp))
                .border(2.dp, Color.Black, RoundedCornerShape(16.dp))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = if (showAnswer)
                    flashcards[currentIndex].answer
                 else
                    flashcards[currentIndex].question,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )

        }
            Row(
                modifier = Modifier.fillMaxWidth(0.6f),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ){
                ReusableButton("ANSWER", {showAnswer = !showAnswer},Color(0xFF1A1616), modifier = Modifier.weight(1f))
                ReusableButton("NEXT",{
                    showAnswer = false
                    if (currentIndex < flashcards.size -1){
                        currentIndex++
                    } else {
                        currentIndex = 0
                    }
                },Color(0xFF1A1616), modifier = Modifier.weight(1f))
            }


        } else{
            Text("No flashcards yet. Create one to get started! :)", fontWeight = FontWeight.Bold)
        }

        ReusableButton("BACK", onNavigate, Color(0xFF1A1616), modifier = Modifier)
    }
}

data class Flashcard(
    val question: String,
    val answer: String
)

// My Create Screen
@Composable
fun MyCreateScreen(onNavigate: () -> Unit, flashcards: MutableList<Flashcard>){
    var question by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a Flashcard to study", fontSize = 30.sp)

        TextField(
            value = question,
            onValueChange = { question = it },
            label = { Text("Question") }
        )
        TextField(
            value = answer,
            onValueChange = { answer = it },
            label = { Text("Answer") }
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
        ){
            ReusableButton("SAVE", {
                val newCard = Flashcard(question,answer)
                flashcards.add(newCard)
                question = ""
                answer = ""
            }, Color(0xFF1A1616), modifier = Modifier)
            ReusableButton("BACK", onNavigate, Color(0xFF1A1616), modifier = Modifier)
        }

    }
}



//@Preview(
//    showBackground = true)
//@Composable
//fun MyHomeScreen(){
//    Scaffold(
//        containerColor = Color(0xFFF5EFE6)
//    ){
//            paddingValues ->
//
//        Column(
//            modifier = Modifier.fillMaxSize().padding(paddingValues),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            Text("¡Welcome to FlashCards App!")
//            Text("Select an option to continue")
//
//        }
//    }
//}