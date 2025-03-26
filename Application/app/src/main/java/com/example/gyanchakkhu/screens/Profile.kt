package com.example.gyanchakkhu.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.ui.theme.MyPurple100
import com.example.gyanchakkhu.ui.theme.MyPurple60
import com.example.gyanchakkhu.ui.theme.MyRed80
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily
import com.example.gyanchakkhu.utils.CustomTextField
import com.example.gyanchakkhu.utils.DigitalLibraryCard
import com.example.gyanchakkhu.utils.MyHorizontalDivider
import com.example.gyanchakkhu.utils.QRCodeScannerScreen
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.utils.parseQRCodeData
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun ProfilePage(navController: NavController, authViewModel: AuthViewModel) {
    val context = LocalContext.current
    val authState = authViewModel.authState.observeAsState()
    val userData by authViewModel.userData.observeAsState()
    val libName by authViewModel.libraryName.observeAsState()
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    val userEmail = userData?.email ?: "Not Found!"
    val userName = userData?.name ?: "Not Found!"
    val userLibName = libName ?: "Not Found!"
    val userLibUid = userData?.libraryUid ?: "Not Found!"
    val userCardIssueNumber = userData?.cardUid ?: "Not Found!"
    var isCameraOn by remember { mutableStateOf(false) }

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

    LaunchedEffect(Unit) {
        when (authState.value) {
            is AuthState.Unauthenticated -> {
                navController.navigate(Routes.login_page)
            }
            else -> Unit
        }
    }

    var isGranted by remember { mutableStateOf(true) }
    val permissions = arrayOf(
        Manifest.permission.CAMERA
    )

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsResult ->
        val allGranted = permissionsResult.all { it.value }
        if (allGranted) {
            isGranted = true
            Toast.makeText(context, "All permissions granted!", Toast.LENGTH_SHORT).show()
        } else {
            isGranted = false
            Toast.makeText(context, "Some permissions were denied!", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        if (permissions.any {
                ContextCompat.checkSelfPermission(
                    context,
                    it
                ) != PackageManager.PERMISSION_GRANTED
            }) {
            permissionLauncher.launch(permissions)
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
                        Image(
                            painter = painterResource(id = R.drawable.bg_home),
                            contentDescription = "Login/SignUp Bg",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(30.dp),
                            contentScale = ContentScale.FillWidth
                        )
                        Text(
                            text = "Personal Information",
                            color = MyPurple100,
                            fontSize = 24.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .padding(top = 16.dp, start = 30.dp, end = 30.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "Name",
                                    color = MyPurple100,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = userName,
                                    fontFamily = poppinsFontFamily,
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
                                    color = MyPurple100,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = userEmail,
                                    fontFamily = poppinsFontFamily,
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
                                    color = MyPurple100,
                                    fontFamily = poppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = "******",
                                    fontFamily = poppinsFontFamily,
                                    modifier = Modifier
                                        .padding(horizontal = 12.dp)
                                        .weight(2f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    modifier = Modifier
                                        .padding(top = 12.dp, bottom = 20.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(36.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MyPurple120),
                                    onClick = { /*TODO*/ }
                                ) {
                                    Text(
                                        text = "Change Password",
                                        color = Color.Black,
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 12.sp
                                    )
                                }
                                Button(
                                    modifier = Modifier
                                        .padding(top = 12.dp, bottom = 20.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .height(36.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                                    onClick = { authViewModel.signOut() }
                                ) {
                                    Text(
                                        text = "Signout",
                                        color = Color.White,
                                        fontFamily = poppinsFontFamily,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                        MyHorizontalDivider()
                        Text(
                            text = "Library Information",
                            color = MyPurple100,
                            fontSize = 24.sp,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(Color.White)
                                .padding(top = 16.dp, start = 30.dp, end = 30.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                Row {
                                    Column {
                                        Text(
                                            text = "Library Name",
                                            color = MyPurple100,
                                            fontFamily = poppinsFontFamily,
                                            fontWeight = FontWeight.Bold)
                                        Text(
                                            text = "Library Unique ID",
                                            color = MyPurple100,
                                            fontFamily = poppinsFontFamily,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        if (isUserEnrolledInLibrary) {
                                            Text(
                                                text = userLibName,
                                                fontFamily = poppinsFontFamily,
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .horizontalScroll(rememberScrollState()),
                                                maxLines = 1,
                                                overflow = TextOverflow.Clip
                                            )
                                            Text(
                                                text = userLibUid,
                                                fontFamily = poppinsFontFamily,
                                                modifier = Modifier
                                                    .align(Alignment.End)
                                                    .horizontalScroll(rememberScrollState()),
                                                maxLines = 1,
                                                overflow = TextOverflow.Clip
                                            )
                                        } else {
                                            CustomTextField(
                                                text = libNameField,
                                                onValueChange = { libNameField = it },
                                                label = "Enter Library Name"
                                            )
                                            CustomTextField(
                                                text = libUIDField,
                                                onValueChange = { libUIDField = it },
                                                label = "Enter Library UID"
                                            )
                                        }
                                    }
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Button(
                                        modifier = Modifier
                                            .padding(top = 12.dp, bottom = 20.dp)
                                            .clip(RoundedCornerShape(20.dp))
                                            .height(36.dp)
                                            .then(
                                                if(isUserEnrolledInLibrary) Modifier.fillMaxWidth() else Modifier
                                            ),
                                        colors = ButtonDefaults.buttonColors(containerColor = if (isUserEnrolledInLibrary) MyRed80 else MyPurple120),
                                        onClick = {
                                            libNameField = libNameField.trim()
                                            libUIDField = libUIDField.trim()
                                            if (isUserEnrolledInLibrary) {
                                                authViewModel.removeLibrary()
                                                libNameField = ""
                                                libUIDField = ""
                                            } else authViewModel.authenticateAsLibraryUser(
                                                libNameField,
                                                libUIDField
                                            )
                                        }
                                    ) {
                                        Text(
                                            text = if (isUserEnrolledInLibrary) "Change Library" else "Submit",
                                            color = if (isUserEnrolledInLibrary) Color.White else Color.Black,
                                            fontFamily = poppinsFontFamily,
                                            fontSize = 12.sp
                                        )
                                    }
                                    if(!isUserEnrolledInLibrary){
                                        Button(
                                            modifier = Modifier
                                                .padding(top = 12.dp, bottom = 20.dp)
                                                .clip(RoundedCornerShape(20.dp))
                                                .height(36.dp),
                                            colors = ButtonDefaults.buttonColors(containerColor = Blue40),
                                            onClick = {
                                                if(isGranted) isCameraOn = true
                                                else Toast.makeText(
                                                    context,
                                                    "Please allow camera permission!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                        ) {
                                            Text(
                                                text = "Scan QR",
                                                color = Color.White,
                                                fontFamily = poppinsFontFamily,
                                                fontSize = 12.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        MyHorizontalDivider()
                        Text(
                            text = "Virtual Library Card",
                            color = MyPurple100,
                            fontFamily = poppinsFontFamily,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(12.dp)
                        )
                        DigitalLibraryCard(
                            name = userName,
                            cardIssueNumber = userCardIssueNumber,
                            libraryName = userLibName,
                            libraryUid = userLibUid,
                            isUserEnrolledInLibrary = isUserEnrolledInLibrary,
                        )
                        Spacer(modifier = Modifier.height(200.dp))
                    }
                }
            }
            if(isCameraOn){
                Dialog(
                    onDismissRequest = { isCameraOn = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                    ) {
                        QRCodeScannerScreen(
                            numOfCom = 1,
                            onResult = {
                                val libData = parseQRCodeData(it)
                                libNameField = libData[0]
                                libUIDField = libData[1]
                                isCameraOn = false
                            },
                            onClose = { isCameraOn = false }
                        )
                    }
                }
            }

        }
    }
}