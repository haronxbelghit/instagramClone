package com.eks.instagramclone.main

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eks.instagramclone.DestinationScreen
import com.eks.instagramclone.IgViewModel
import com.eks.instagramclone.R
import com.eks.instagramclone.auth.ProfileScreen

@Composable
fun MyPostsScreen(navController: NavController, vm: IgViewModel) {

//            Text(text = "My posts Screen")
//        }
//        BottomNavMenu(
//            selectedItem = BottomNavigationItem.POSTS,
//            navController = navController
//        )
//    }
    val userData = vm.userData.value
    val isLoading = vm.inProgress.value
    val newPostImageLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent(),){
        uri ->
        uri?.let{
            val encoded = Uri.encode(it.toString())
            val route = DestinationScreen.NewPost.createRoute(encoded)
            navController.navigate(route)
        }

    }
    Column {
        Column(modifier = Modifier.weight(1f)) {
            Row {
                ProfileImage(userData?.imageUrl) {
                    newPostImageLauncher.launch("image/*")
                }
                Text(
                    text = "15\nPosts",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "50k\nFollowers",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "500\nFollowing",
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                    textAlign = TextAlign.Center
                )
            }
            Column(modifier = Modifier.padding(8.dp)) {
                val usernameDisplay = userData?.username ?: ""
                Text(
                    text = userData?.name ?: "",
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = usernameDisplay
                )
                Text(
                    text = userData?.bio ?: ""
                )
            }
            OutlinedButton(
                onClick = { navigateTo(navController, DestinationScreen.Profile )},
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
                elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    pressedElevation = 0.dp,
                    disabledElevation = 0.dp
                ),
                shape = RoundedCornerShape(10)
            ) {
                Text(text = "Edit Profile", color = Color.DarkGray)
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "Posts list")
            }
        }
        BottomNavMenu(selectedItem = BottomNavigationItem.POSTS, navController = navController)

    }
    if (isLoading) CommonProgressSpinner()
}

@Composable
fun ProfileImage(imageUrl: String?, onclick: () -> Unit) {
    Box(modifier = Modifier
        .padding(top = 16.dp)
        .clickable { onclick.invoke() }
    ) {
        UserImageCard(
            userImage = imageUrl,
            modifier = Modifier
                .padding(8.dp)
                .size(80.dp)
        )
        Card(
            shape = CircleShape,
            border = BorderStroke(width = 2.dp, color = Color.White),
            modifier = Modifier
                .size(32.dp)
                .align(Alignment.BottomEnd)
                .padding(bottom = 8.dp, end = 8.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_baseline_add_24),
                contentDescription = null,
                modifier = Modifier.background(Color.Blue)
            )
        }
    }
}
