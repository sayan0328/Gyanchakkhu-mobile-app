package com.example.gyanchakkhu.screens

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue120
import com.example.gyanchakkhu.ui.theme.Blue160
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel

@Composable
fun LoginPage(navController: NavController, authViewModel: AuthViewModel) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(Routes.home_page)
            }
            is AuthState.Error -> Toast.makeText(context, (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT).show()
            else -> Unit
        }
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Image(
                        painter = painterResource(id = R.drawable.bg_lgsg),
                        contentDescription = "Login/SignUp Bg",
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                }
                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp, horizontal = 40.dp)
                            .clip(shape = RoundedCornerShape(10.dp))
                            .border(
                                BorderStroke(1.dp, Color.Black),
                                shape = RoundedCornerShape(10.dp)
                            )

                    ) {
                        Text(
                            text = stringResource(id = R.string.login),
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = FontFamily.Serif
                            ),
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 10.dp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = stringResource(id = R.string.welcome_lg),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Default
                            ),
                            fontWeight = FontWeight.Light,
                            color = Color.Black,
                            modifier = Modifier.padding(top = 5.dp, bottom = 20.dp),
                            textAlign = TextAlign.Center
                        )
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            label = { Text("Email") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 20.dp),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 20.dp),
                            singleLine = true,
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            trailingIcon = {
                                val image =
                                    if (passwordVisible) R.drawable.baseline_visibility_24 else R.drawable.baseline_visibility_off_24
                                Image(
                                    painter = painterResource(id = image),
                                    contentDescription = null,
                                    modifier = Modifier.clickable {
                                        passwordVisible = !passwordVisible
                                    }
                                )

                            }
                        )
                        Text(
                            text = stringResource(id = R.string.forgot_password),
                            style = TextStyle(
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight(300),
                                fontStyle = FontStyle.Italic,
                                color = Color(0xFF000000)
                            ),
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 20.dp)
                                .clickable { /*TODO*/ }
                        )
                        Button(
                            onClick = { authViewModel.login(email, password) },
                            enabled = authState.value != AuthState.Loading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp, horizontal = 20.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Blue40.copy(alpha = 0.9f))
                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                        ) {
                            Text(text = "Login", color = Color.White)
                        }
                        Text(
                            text = stringResource(id = R.string.no_account),
                            modifier = Modifier
                                .padding(bottom = 5.dp),
                            style = TextStyle(
                                color = Color.Gray
                            )
                        )
                        Text(
                            text = stringResource(id = R.string.goto_signup),
                            modifier = Modifier
                                .clickable { navController.navigate(Routes.signup_page) },
                            style = TextStyle(
                                color = Blue80,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
                item {
                    Image(
                        painter = painterResource(id = R.drawable.pic1),
                        modifier = Modifier.size(150.dp),
                        contentDescription = null
                    )
                }
                item {
                    TextButton(onClick = { /*TODO*/ }) {
                        Text(
                            text = stringResource(id = R.string.help),
                            fontSize = 12.sp,
                            color = Color.Blue
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}