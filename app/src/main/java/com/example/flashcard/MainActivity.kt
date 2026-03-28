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
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

//My functions saveSubjects and loadSubjects are to be able to save the items even if
//the user close the app.
fun saveSubjects(context: Context, subjects: List<Subject>){
    val sharedPreferences = context.getSharedPreferences("flashcards", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    val json = Gson().toJson(subjects)
    editor.putString("subjects", json)
    editor.apply()
}


//My functions saveSubjects and loadSubjects are to be able to save the items even if
//the user close the app.
fun loadSubjects(context: Context): MutableList<Subject>{
    val sharedPreferences = context.getSharedPreferences("flashcards", Context.MODE_PRIVATE)
    val json = sharedPreferences.getString("subjects", null)

    return if (json !=null){
        val type = object : TypeToken<MutableList<Subject>>() {}.type
        Gson().fromJson(json, type)
    }else {
        mutableListOf()
    }
}


//My main activity to handle all the navigation in my app: Home, subject, Create, Study screens
//And the buttons to navigate.
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FlashCardTheme {
                val context = LocalContext.current
                val subjects = remember {
                    mutableStateListOf<Subject>().apply {
                        addAll(loadSubjects(context))
                    }}


                var selectedSubject by remember { mutableStateOf<Subject?>(null)}

                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home"
                ) {
                    composable("home") {
                        MyHomeScreen(
                            subjects = subjects,
                            onCreateSubject = { name ->
                                subjects.add(Subject(name))
                                saveSubjects(context, subjects)
                            },
                            onOpenSubject = {
                                selectedSubject = it
                                navController.navigate("subject")
                            },
                            context = context
                        )
                    }


                    composable("subject"){
                        selectedSubject?.let { subject ->
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(subject.name,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Bold)

                                ReusableButton(
                                    "CREATE",
                                    {navController.navigate("create")},
                                    Color(0xFF1A1616),
                                    Modifier
                                )

                                ReusableButton(
                                    "STUDY",
                                    {navController.navigate("study")},
                                    Color(0xFF1A1616),
                                    Modifier
                                )

                                ReusableButton(
                                    "BACK",
                                    {navController.popBackStack() },
                                    Color(0xFF1A1616),
                                    Modifier
                                )
                            }
                        }
                    }
 //Route for my createScreen

                    composable("create") {
                        selectedSubject?.let {subject ->
                            MyCreateScreen(
                                onNavigate = {navController.popBackStack()},
                                subject = subject,
                                subjects = subjects,
                                context = context
                            )
                        }
                    }
//Route for my StudyScreen
                    composable ("study"){
                        selectedSubject?.let {subject ->
                        MyStudyScreen (
                            flashcards = subject.flashcards,
                            onNavigate = { navController.popBackStack() }
                        )
                    }
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
fun MyHomeScreen(
    subjects: MutableList<Subject>,
    onCreateSubject:(String) -> Unit,
    onOpenSubject: (Subject) -> Unit,
    context: Context){
    Scaffold(
        containerColor = Color(0xFFAABEE3)
    ){
        paddingValues ->
    var subjectName by remember {mutableStateOf("")}

        Column(
            modifier = Modifier.fillMaxSize().padding(paddingValues)
                .padding(top = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text("¡Welcome to FlashCards App!", fontSize = 20.sp)
            Text("Create a Subject")
            TextField(
                value = subjectName,
                onValueChange = {subjectName = it },
                label = { Text("subject name") }
            )
            Button(onClick = {
            if (subjectName.isNotBlank()){
                onCreateSubject(subjectName)
                subjectName = ""
            }
            }) {
                Text("Add Subject")
            }

            subjects.forEach { subject ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(subject.name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold)

                    Row{
                        Button(onClick = {onOpenSubject(subject) }){
                            Text("Open")
                        }

                        Button(onClick = {
                            subjects.remove(subject)
                            saveSubjects(context, subjects)
                        }) {
                            Text("❌")
                        }
                    }
                }
            }
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


//My data class for Flashcard, each flashcard has a single study item with a question and answer.
data class Flashcard(
    val question: String,
    val answer: String
)

//My data class Subject acts as a container for a collection of related flashcards.
data class Subject(
    val name: String,
    val flashcards: MutableList<Flashcard> = mutableListOf()
)



// My Create Screen displaying and handling the inputs for the questions and answers
@Composable
fun MyCreateScreen(onNavigate: () -> Unit, subject: Subject, subjects: List<Subject>, context: Context){
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
                subject.flashcards.add(newCard)
                saveSubjects(context, subjects)
                question = ""
                answer = ""
            }, Color(0xFF1A1616), modifier = Modifier)
            ReusableButton("BACK", onNavigate, Color(0xFF1A1616), modifier = Modifier)
        }

    }
}




