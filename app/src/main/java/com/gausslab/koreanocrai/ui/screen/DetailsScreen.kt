package com.gausslab.koreanocrai.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@Composable
fun DetailsScreen(
    title: String? = null,
    content: @Composable () -> Unit,
) {
    val viewModel: DetailsViewModel = hiltViewModel()
    Scaffold(
        modifier = Modifier,
        topBar = {
            Column {
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier) {
                        IconButton(onClick = { viewModel.navController.navigateUp() }) {
                            Icon(
                                modifier = Modifier,
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.Black
                            )

                        }
                    }
                    title?.let {
                        Text(
                            modifier = Modifier
                                .padding(start = 16.dp, end = 16.dp, top = 20.dp, bottom = 20.dp),
                            text = it,
                            textAlign = TextAlign.Start,
                            style = TextStyle(
                                fontSize = 18.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight(700),
                                color = Color(0xFF000000),
                                textAlign = TextAlign.Center,
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
            }
        }
    ) {
        Column(modifier = Modifier.padding(it)) {
            content()
        }
    }
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    val navController: NavHostController
) : ViewModel()