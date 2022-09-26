package com.eks.instagramclone.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.eks.instagramclone.DestinationScreen
import com.eks.instagramclone.IgViewModel
import com.eks.instagramclone.R
import com.eks.instagramclone.main.CheckSignIn
import com.eks.instagramclone.main.CommonProgressSpinner
import com.eks.instagramclone.main.navigateTo

@Composable
fun LoginScreen(navController: NavController, vm: IgViewModel) {

    CheckSignIn(vm = vm, navController = navController)

    val focus = LocalFocusManager.current

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .verticalScroll(
                    rememberScrollState()
                ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val emailState = remember { mutableStateOf(TextFieldValue()) }
            val passwordState = remember { mutableStateOf(TextFieldValue()) }
            Image(
                painter = painterResource(id = R.drawable.ig_logo),
                contentDescription = "logo",
                modifier = Modifier
                    .width(250.dp)
                    .padding(top = 16.dp)
                    .padding(8.dp)
            )
            Text(
                text = "Login",
                modifier = Modifier.padding(8.dp),
                fontSize = 30.sp,
                fontFamily = FontFamily.SansSerif
            )
            OutlinedTextField(
                value = emailState.value,
                onValueChange = { emailState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Email") }
            )
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = { passwordState.value = it },
                modifier = Modifier.padding(8.dp),
                label = { Text(text = "Password") },
                visualTransformation = PasswordVisualTransformation()
            )
            Button(
                modifier = Modifier.padding(8.dp),
                onClick = {
                    focus.clearFocus(force = true)
                    vm.onLogin(emailState.value.text,passwordState.value.text)
                }
            ) {
                Text(text = "Login")
            }
            Text(
                text = "New here? Go to sign up ->",
                color = Color.Blue,
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        navigateTo(navController, DestinationScreen.SignUp)
                    }
            )
        }
        val isLoading = vm.inProgress.value
        if (isLoading) {
            CommonProgressSpinner()
        }
    }
}