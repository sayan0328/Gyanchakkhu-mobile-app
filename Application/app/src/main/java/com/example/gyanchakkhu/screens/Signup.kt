package com.example.gyanchakkhu.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple20
import com.example.gyanchakkhu.ui.theme.MyPurple80
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun SignupPage(navController: NavController, authViewModel: AuthViewModel) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val gradient = gradientBrush(
        colorStops = arrayOf(
            0.0f to MyPurple120,
            2.0f to Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, POSITIVE_INFINITY)
    )

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> {
                navController.navigate(Routes.home_page)
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message,
                Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }
    Surface(
        modifier = Modifier.fillMaxWidth(),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(R.drawable.gyanchakkhu_logo),
                        contentDescription = "Gyanchakkhu Logo",
                        Modifier.size(48.dp)
                    )
                    Text(
                        text = stringResource(id = R.string.signup),
                        style = TextStyle(
                            fontSize = 20.sp,
                            fontFamily = poppinsFontFamily
                        ),
                        fontWeight = FontWeight.Bold,
                        color = MyPurple20,
                    )
                }
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = stringResource(id = R.string.welcome_sg),
                    style = TextStyle(
                        fontFamily = poppinsFontFamily
                    ),
                    color = MyPurple80,
                    modifier = Modifier.padding(
                        top = 24.dp,
                        bottom = 20.dp,
                        start = 36.dp,
                        end = 36.dp
                    ),
                    textAlign = TextAlign.Center
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp)
                        .clip(shape = RoundedCornerShape(10.dp))
                        .background(Color.White)
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Name",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        ),
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        placeholder = {
                            Text(
                                text = "Enter your name",
                                style = TextStyle(fontFamily = poppinsFontFamily)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    Text(
                        text = "Email",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        ),
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                text = "Enter your email",
                                style = TextStyle(fontFamily = poppinsFontFamily)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    Text(
                        text = "Password",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        ),
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text(
                                text = "Enter your password",
                                style = TextStyle(fontFamily = poppinsFontFamily)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
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

                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    Text(
                        text = "Confirm Password",
                        textAlign = TextAlign.Start,
                        style = TextStyle(
                            fontFamily = poppinsFontFamily
                        ),
                        fontWeight = FontWeight.Light,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                    )
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        placeholder = {
                            Text(
                                text = "Confirm your password",
                                style = TextStyle(fontFamily = poppinsFontFamily)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp, bottom = 16.dp, start = 20.dp, end = 20.dp),
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

                        },
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )

                    Button(
                        onClick = {
                            name = name.trim()
                            email = email.trim()
                            authViewModel.signup(name, email, password, confirmPassword)
                        },
                        enabled = authState.value != AuthState.Loading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp, horizontal = 20.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Blue40.copy(alpha = 0.9f))
                            .height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text(
                            text = "Sign up",
                            color = Color.White,
                            style = TextStyle(fontFamily = poppinsFontFamily)
                        )
                    }
                    Text(
                        text = stringResource(id = R.string.have_account),
                        modifier = Modifier
                            .padding(bottom = 5.dp),
                        style = TextStyle(
                            color = Color.Gray,
                            fontFamily = poppinsFontFamily
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.goto_login),
                        modifier = Modifier
                            .clickable {
                                authViewModel.unAuthenticateUser()
                                navController.navigate(Routes.login_page)
                            },
                        style = TextStyle(
                            color = Blue80,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.ExtraBold
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}