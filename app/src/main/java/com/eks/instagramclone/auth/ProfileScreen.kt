package com.eks.instagramclone.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.eks.instagramclone.DestinationScreen
import com.eks.instagramclone.IgViewModel
import com.eks.instagramclone.main.CommonDivider
import com.eks.instagramclone.main.CommonProgressSpinner
import com.eks.instagramclone.main.navigateTo


@Composable
fun ProfileScreen(navController: NavController, vm: IgViewModel) {
    val isLoading = vm.inProgress.value
    if (isLoading) {
        CommonProgressSpinner()
    } else {
        val userData = vm.userData.value
        var name by rememberSaveable { mutableStateOf(userData?.name ?: "") }
        var username by rememberSaveable { mutableStateOf(userData?.username ?: "") }
        var bio by rememberSaveable { mutableStateOf(userData?.bio ?: "") }
        ProfileContent(
            vm = vm,
            name = name,
            username = username,
            bio = bio,
            onNameChange = { name = it },
            onUsernameChange = { username = it },
            onBioChange = { bio = it },
            onSave = {},
            onBack = { navigateTo(navController, DestinationScreen.MyPosts) },
            onLogout = {}
        )
    }
}

@Composable
fun ProfileContent(
    vm: IgViewModel,
    name: String,
    username: String,
    bio: String,
    onNameChange: (String) -> Unit,
    onUsernameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Back", modifier = Modifier.clickable { onBack.invoke() })
            Text(text = "Save", modifier = Modifier.clickable { onSave.invoke() })
        }
        CommonDivider()

        //user image
    }
}