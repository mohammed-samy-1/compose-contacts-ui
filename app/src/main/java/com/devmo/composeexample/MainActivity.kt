package com.devmo.composeexample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Create
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devmo.composeexample.ui.theme.ComposeExampleTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeExampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = Color.Black
                ) {
                    Ui()
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class, ExperimentalMaterialApi::class)
@Composable
fun Ui() {
    val rainbowColors = listOf(
        Color(0xFFB78628),
        Color(0XFFC69320),
        Color(0XFFDBA514),
        Color(0XFFEEB609),
        Color(0XFFFCC201),
        Color.Yellow
    )
    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }
    val text1 = remember {
        mutableStateOf("0")
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "${text1.value} Contacts",
            style = TextStyle(brush),
            modifier = Modifier
                .background(brush, RectangleShape)
                .padding(1.dp)
                .background(Color.Black)
                .padding(17.dp),

            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(10.dp))

        val li = rememberSaveable(saver = SnapshotStateListSaver()) {
            mutableStateListOf<Contact>()
        }
        text1.value = li.size.toString()
        var name by remember {
            mutableStateOf(TextFieldValue(""))
        }
        var phone by remember {
            mutableStateOf(TextFieldValue(""))
        }
        var enable by remember {
            mutableStateOf(name.text.isNotBlank() && phone.text.isNotBlank())
        }


        Column(
            modifier = Modifier
                .weight(3f)
                .fillMaxWidth(),
        ) {
            TextField(
                value = name,
                onValueChange = {
                    name = it
                    enable = it.text.isNotBlank() && phone.text.isNotBlank()
                },
                textStyle = TextStyle(
                    brush = brush,
                    fontSize = 16.sp,
                    shadow = Shadow(color = Color(0XFFC69320), blurRadius = 7f)
                ),
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.AccountCircle,
                            contentDescription = null,
                            tint = Color(0XFFC69320),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(text = "Name", style = TextStyle(brush))
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color(0XFFC69320), focusedIndicatorColor = Color(0XFFC69320)
                ),
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Text)
            )
            TextField(
                value = phone,
                onValueChange = {
                    phone = it
                    enable = it.text.isNotBlank() && name.text.isNotBlank()
                },
                textStyle = TextStyle(
                    brush = brush,
                    fontSize = 16.sp,
                    shadow = Shadow(color = Color(0XFFC69320), blurRadius = 7f)
                ),
                label = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.Create,
                            contentDescription = null,
                            tint = Color(0XFFC69320),
                            modifier = Modifier.size(15.dp)
                        )
                        Text(text = "phone", style = TextStyle(brush))
                    }
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.textFieldColors(
                    cursorColor = Color(0XFFC69320), focusedIndicatorColor = Color(0XFFC69320)
                ),
                keyboardOptions = KeyboardOptions().copy(keyboardType = KeyboardType.Phone)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Button(
                onClick = {
                    li.add(Contact(Random.nextInt(), name.text, phone.text))
                    name = TextFieldValue("")
                    phone = TextFieldValue("")
                    enable = false
                },
                modifier = Modifier
                    .weight(2f)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0XFFC69320), disabledBackgroundColor = Color.Gray
                ),
                enabled = enable
            ) {
                Text(
                    text = "new Contact"
                )
            }
        }




        Spacer(modifier = Modifier.height(10.dp))
        val context = LocalContext.current
        LazyColumn(Modifier.weight(10f)) {

            items(li) {
                val dismissState = rememberDismissState(confirmStateChange = { dv ->
                    if (dv == DismissValue.DismissedToStart) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse("sms:${it.phone}")
                        context.startActivity(intent)
                    } else if (dv == DismissValue.DismissedToEnd) {
                        val uri = "tel:${it.phone}"
                        val callIntent = Intent(Intent.ACTION_CALL)
                        callIntent.data = Uri.parse(uri)
                        context.startActivity(callIntent)
                    }
                    false
                })
                SwipeToDismiss(state = dismissState,
                    background = {
                        when (dismissState.dismissDirection) {
                            DismissDirection.StartToEnd -> {

                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Green)
                                        .padding(15.dp, 5.dp)
                                ) {
                                    Spacer(
                                        modifier = Modifier
                                            .width(10.dp)
                                            .align(Alignment.CenterEnd)
                                    )
                                    Icon(
                                        Icons.Default.Call,
                                        contentDescription = null,
                                        modifier = Modifier.align(Alignment.CenterEnd)
                                    )
                                    Text(
                                        text = "Call", modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            DismissDirection.EndToStart -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(Color.Blue)
                                        .padding(15.dp, 5.dp)
                                ) {
                                    Icon(
                                        Icons.Filled.Email,
                                        contentDescription = null,
                                        modifier = Modifier.align(Alignment.CenterStart)
                                    )
                                    Text(
                                        text = "Message",
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                            else -> Box(modifier = Modifier.background(color = Color.Transparent))
                        }

                    },
                    directions = setOf(DismissDirection.EndToStart, DismissDirection.StartToEnd),
                    dismissContent = {
                        ListItem(item = it)
                    })

            }
        }
    }

}

@OptIn(ExperimentalTextApi::class)
@Composable
fun ListItem(item: Contact) {
    val rainbowColors = listOf(
        Color(0xFFB78628),
        Color(0XFFC69320),
        Color(0XFFDBA514),
        Color(0XFFEEB609),
        Color(0XFFFCC201),
        Color.Yellow
    )
    val brush = remember {
        Brush.linearGradient(
            colors = rainbowColors
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Text(
            text = item.name.toString(),
            modifier = Modifier.align(Alignment.CenterStart),
            fontSize = 30.sp,
            style = TextStyle(
                brush = brush,
                fontFamily = FontFamily.SansSerif,
                shadow = Shadow(color = Color(0XFFC69320), blurRadius = 7f)
            )
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ComposeExampleTheme {
        Ui()
    }
}