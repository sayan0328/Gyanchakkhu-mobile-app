package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple80
import com.example.gyanchakkhu.ui.theme.MyRed80
import com.example.gyanchakkhu.utils.CustomTextField
import com.example.gyanchakkhu.utils.DigitalLibraryCard
import com.example.gyanchakkhu.utils.MyHorizontalDivider
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun ProfilePage(navController: NavController, authViewModel: AuthViewModel) {
    val userData by authViewModel.userData.observeAsState()
    val libName by authViewModel.libraryName.observeAsState()
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val userEmail = userData?.email ?: "Not Found!"
    val userName = userData?.name ?: "Not Found!"
    val userLibName = libName ?: "Not Found!"
    val userLibUid = userData?.libraryUid ?: "Not Found!"
    val userCardIssueNumber = userData?.cardUid ?: "Not Found!"

    var libNameField by remember { mutableStateOf("") } // Library Name Field Value
    var libUIDField by remember { mutableStateOf("") } // Library UID Field Value

    val gradient = gradientBrush(
        colorStops = arrayOf(
            0.0f to MyPurple120,
            2.0f to Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, POSITIVE_INFINITY)
    )

    val authState = authViewModel.authState.observeAsState()
    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate(Routes.login_page)
            else -> Unit
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
        ) {
            if (!isUserEnrolledInLibrary) {
                Image(
                    painter = painterResource(id = R.drawable.bg_idle),
                    contentDescription = "Home Bg",
                    Modifier.align(Alignment.Center)
                )
            }
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(30.dp))
                        Image(
                            painter = painterResource(id = R.drawable.bg_home),
                            contentDescription = "Login/SignUp Bg",
                            modifier = Modifier.fillMaxWidth(),
                            contentScale = ContentScale.FillWidth
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .padding(horizontal = 30.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Personal Information",
                                color = MyPurple80,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Name",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = userName,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .weight(2f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row {
                                Text(
                                    text = "Email",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = userEmail,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .weight(2f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row {
                                Text(
                                    text = "Password",
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "******",
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .weight(2f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .padding(top = 12.dp, bottom = 20.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Blue40),
                                onClick = {/*TODO*/ }
                            ) {
                                Text(
                                    text = "Change Password",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                        MyHorizontalDivider()
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .padding(horizontal = 30.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Library Information",
                                color = MyPurple80,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                            Row {
                                Column {
                                    Text(text = "Library Name", fontWeight = FontWeight.Bold)
                                    Text(text = "Library Unique ID", fontWeight = FontWeight.Bold)
                                }
                                Spacer(modifier = Modifier.width(20.dp))
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    if(isUserEnrolledInLibrary){
                                            Text(
                                                text = userLibName,
                                                modifier = Modifier.align(Alignment.End)
                                            )
                                        Text(
                                            text = userLibUid,
                                            modifier = Modifier.align(Alignment.End)
                                        )
                                    }else{
                                        CustomTextField(
                                            text = libNameField,
                                            onValueChange = {libNameField = it},
                                            label = "Enter Library Name"
                                        )
                                        CustomTextField(
                                            text = libUIDField,
                                            onValueChange = {libUIDField = it},
                                            label = "Enter Library UID"
                                        )
                                    }
                                }
                            }
                            Button(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .height(36.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = if(isUserEnrolledInLibrary) MyRed80 else Blue40),
                                onClick = {
                                    if(isUserEnrolledInLibrary) {
                                        authViewModel.removeLibrary()
                                        libNameField = ""
                                        libUIDField = ""
                                    } else authViewModel.registerAsLibraryUser(libNameField, libUIDField)
                                }
                            ) {
                                Text(
                                    text = if(isUserEnrolledInLibrary) "Change Library" else "Submit",
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                        MyHorizontalDivider()
                        Text(
                            text = "Virtual Library Card",
                            color = MyPurple80,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        DigitalLibraryCard(
                            name = userName,
                            cardIssueNumber = userCardIssueNumber,
                            libraryName = userLibName,
                            libraryUid = userLibUid,
                            isUserEnrolledInLibrary = isUserEnrolledInLibrary,)

                        Button(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp)),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                            onClick = { authViewModel.signout() }
                        ) {
                            Text(text = "Logout", color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(200.dp))
                    }
                }
            }
        }
    }
}