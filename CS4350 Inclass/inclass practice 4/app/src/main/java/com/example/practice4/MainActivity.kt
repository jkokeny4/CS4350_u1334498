package com.example.practice4

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            var text1 by remember { mutableStateOf("") }
            var text2 by remember { mutableStateOf("") }
            var result by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                TextField(
                    value = text1,
                    onValueChange = { text1 = it },
                    label = { Text("First text") }
                )

                TextField(
                    value = text2,
                    onValueChange = { text2 = it },
                    label = { Text("Second text") }
                )

                Button(
                    onClick = {
                        result = text1 + " " + text2
                    },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Concatenate")
                }

                Text(
                    text = result,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}