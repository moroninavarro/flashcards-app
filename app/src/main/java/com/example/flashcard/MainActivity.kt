package com.example.flashcard



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

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
    color: Color
){
 Button(
     onClick = onClick,
     colors = ButtonDefaults.buttonColors(
         containerColor = Color.Black
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
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Select an option to continue")
        ReusableButton("CREATE", onCreateClick, Color.Cyan)
        ReusableButton("STUDY", onStudyClick, Color.Red)

    }
}

@Composable
fun MyStudyScreen(onNavigate: () -> Unit, flashcards: List<Flashcard>){
    var currentIndex by remember { mutableStateOf(0) }
    var showAnswer by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        Text("Study My FlashCards")
        if (flashcards.isNotEmpty()){
            if (showAnswer){
                Text(flashcards[currentIndex].answer)
            }else{
                Text(flashcards[currentIndex].question)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
            ){
                ReusableButton("ANSWER", {showAnswer = !showAnswer},Color.Red)
                ReusableButton("NEXT",onNavigate,Color.Red)
            }


        } else{
            Text("First create your own FlashCards to start study :)")
        }

        ReusableButton("BACK", onNavigate, Color.Green)
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
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Create a Flashcard to study")

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
        ReusableButton("SAVE", {
            val newCard = Flashcard(question,answer)
            flashcards.add(newCard)
            question = ""
            answer = ""
        }, Color.Black)
        ReusableButton("BACK", onNavigate, Color.Red)

    }
}



//@Preview(
//    showBackground = true)
//@Composable
//fun MyCreateScreen(){
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.SpaceAround,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text("Create a Flashcard to study")
//
//        ReusableButton("BACK") { }
//    }
//}