package com.example.checklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.checklist.ui.theme.CheckListTheme


private const val CORRECT_EMAIL = "test@test.com"
private const val CORRECT_PASSWORD = "azerty"

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var showError by rememberSaveable { mutableStateOf(false) }
    val isLoginEnabled = email.isNotBlank() && password.length >= 6

    LoginContent(
        email = email,
        password = password,
        onEmailChange = {
            email = it
            showError = false
        },
        onPasswordChange = {
            password = it
            showError = false
        },
        isLoginEnabled = isLoginEnabled,
        showError = showError,
        onLoginClick = { showError = !(email == CORRECT_EMAIL && password == CORRECT_PASSWORD) },
        modifier = modifier,
    )
}

@Composable
fun LoginContent(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    isLoginEnabled: Boolean,
    showError: Boolean,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            space = 16.dp, alignment = Alignment.CenterVertically
        )
    ) {
        Text(
            text = "Connexion",
            style = MaterialTheme.typography.headlineMedium
        )

        EmailTextField(
            email = email,
            onEmailChange = onEmailChange
        )
        PasswordTextField(
            password = password,
            onPasswordChange = onPasswordChange
        )

        Button(
            enabled = isLoginEnabled,
            onClick = onLoginClick,
            modifier = modifier.fillMaxWidth(0.8f)
        ) {
            Text("Se connecter")
        }

        ErrorMessage(showError)
    }

}

@Composable
fun EmailTextField(
    email: String, onEmailChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = email,
        onValueChange = onEmailChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next, keyboardType = KeyboardType.Email
        ),
        label = { Text("E-Mail") },
        modifier = modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun PasswordTextField(
    password: String, onPasswordChange: (String) -> Unit, modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done, keyboardType = KeyboardType.Password
        ),
        label = { Text("Mot de passe") },
        visualTransformation = PasswordVisualTransformation(),
        modifier = modifier.fillMaxWidth(0.8f)
    )
}

@Composable
fun ErrorMessage(
    isShown: Boolean = false
) {
    if (isShown) Text(
        "Identifiants incorrects", color = MaterialTheme.colorScheme.error
    )
}

@Preview
@Composable
private fun PreviewLoginScreen() {
    CheckListTheme {
        LoginContent(
            "salut@gmail.com",
            password = "azer",
            onEmailChange = {},
            onPasswordChange = {},
            isLoginEnabled = false,
            showError = false,
            onLoginClick = {}
        )
    }
}