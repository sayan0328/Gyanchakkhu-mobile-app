package com.example.gyanchakkhu.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gyanchakkhu.R
import com.example.gyanchakkhu.ui.theme.Blue40
import com.example.gyanchakkhu.ui.theme.Blue80
import com.example.gyanchakkhu.ui.theme.MyPurple120
import com.example.gyanchakkhu.utils.Routes
import com.example.gyanchakkhu.utils.gradientBrush
import com.example.gyanchakkhu.viewmodels.AuthState
import com.example.gyanchakkhu.viewmodels.AuthViewModel
import kotlin.Float.Companion.POSITIVE_INFINITY

@Composable
fun BooksPage(navController: NavController, authViewModel: AuthViewModel) {

//    // LOCATION SERVICE START
//
//    val context = LocalContext.current
//    val locationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
//    var location by remember { mutableStateOf<Location?>(null) }
//    val LOCATION_PERMISSION_REQUEST_CODE = 123
//
//    val locationRequest =  LocationRequest.Builder(
//        Priority.PRIORITY_HIGH_ACCURACY,
//        1000 // Update interval in milliseconds (1 second)
//    ).setMinUpdateIntervalMillis(500) // Fastest update interval in milliseconds
//        .build()
//    val locationCallback = object : LocationCallback() {
//        override fun onLocationResult(locationResult: LocationResult) {
//            super.onLocationResult(locationResult)
//            location = locationResult.lastLocation
//        }
//    }
//
//    LaunchedEffect(key1 = Unit) {
//        if (ActivityCompat.checkSelfPermission(
//                context,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat
//                .checkSelfPermission(
//                    context,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                context as Activity,
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ),
//                LOCATION_PERMISSION_REQUEST_CODE
//            )
//            return@LaunchedEffect
//        }
//        locationClient.requestLocationUpdates(locationRequest, locationCallback, null)
//
//    }
//
//    // LOCATION SERVICE END

    val authState = authViewModel.authState.observeAsState()
    val isUserEnrolledInLibrary by authViewModel.isEnrolledInLibrary.observeAsState(false)
    var isIssueSelected by remember { mutableStateOf(true) }
    val gradient = gradientBrush(
        colorStops = arrayOf(
            0.0f to MyPurple120,
            2.0f to Color.White
        ),
        start = Offset(0f, 0f),
        end = Offset(0f, POSITIVE_INFINITY)
    )

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

//                    //LOCATION SERVICE START
//                    if (location != null) {
//                        Text("Latitude: ${location?.latitude}, Longitude: ${location?.longitude}")
//                    } else {
//                        Text("Location not available")
//                    }
//                    //LOCATION SERVICE END

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
                        Spacer(modifier = Modifier.height(30.dp))
                        if (!isUserEnrolledInLibrary) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 20.dp)
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(Color.White)
                                    .padding(horizontal = 20.dp)
                                    .height(36.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = stringResource(id = R.string.complete_profile),
                                    fontSize = 14.sp
                                )
                                Text(
                                    text = "Goto Profile",
                                    color = Blue80,
                                    fontSize = 14.sp,
                                    modifier = Modifier
                                        .clickable {
                                            navController.navigate(Routes.profile_page) {
                                                popUpTo(Routes.home_page) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                )
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                        }
                        Row {
                            Button(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .height(32.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = if (isIssueSelected) Blue40 else Color.White),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 44.dp, vertical = 1.dp),
                                onClick = {
                                    isIssueSelected = true
                                }
                            ) {
                                Text(
                                    text = "Issue",
                                    color = if (!isIssueSelected) Blue40 else Color.White,
                                    fontSize = 16.sp
                                )
                            }
                            Button(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .height(32.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = if (!isIssueSelected) Blue40 else Color.White),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 44.dp, vertical = 1.dp),
                                onClick = {
                                    isIssueSelected = false
                                }
                            ) {
                                Text(
                                    text = "Submit",
                                    color = if (isIssueSelected) Blue40 else Color.White,
                                    fontSize = 16.sp
                                )
                            }
                        }
                        when (isIssueSelected) {
                            true -> IssuePage()
                            false -> SubmitPage()
                        }
                        Spacer(modifier = Modifier.height(160.dp))
                    }
                }
            }
        }
    }
}