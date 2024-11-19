package com.rdc.e09_logindefinitivo.screens.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.GoogleAuthProvider

import com.rdc.e09_logindefinitivo.R
import com.rdc.e09_logindefinitivo.navigation.Screens
import com.rdc.e09_logindefinitivo.theme.FONDO
import com.rdc.e09_logindefinitivo.theme.VERDESPOTIFY
import kotlinx.coroutines.launch

/*
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    //Text(text = "Login")
    val showLoginForm = rememberSaveable {
        mutableStateOf(true)
    }

    // Facebook
    /*val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcherFb = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)) {
        // nothing to do. handled in FacebookCallback

        scope.launch {
            val tokenFB = AccessToken.getCurrentAccessToken()
            val credentialFB = tokenFB?.let { FacebookAuthProvider.getCredential(it.token)}
            if(credentialFB != null){
                viewModel.signInWithFacebook(credentialFB){
                    navController.navigate(Screens.HomeScreen.name)
                }
            }
        }
    }*/

    // Google
    // este token se consigue en Firebase->Proveedores de Acceso->Google->Conf del SKD->Id de cliente web
    val token = "230607497518-616jqsn48jvbu7gtd9gs8747mh8582sg.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult() // esto abrirá un activity para hacer el login de Google
    ) {
        val task =
            GoogleSignIn.getSignedInAccountFromIntent(it.data) // esto lo facilita la librería añadida
        // el intent será enviado cuando se lance el launcher
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                navController.navigate(Screens.HomeScreen.name)
            }
        } catch (ex: Exception) {
            Log.d("My Login", "GoogleSignIn falló")
        }
    }




    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()

        ) {
            if (showLoginForm.value) { //si es true crea cuenta sino inicia sesion
                Text(text = "Inicia sesion")
                UserForm(isCreateAccount = false) { email, password ->
                    Log.d("My Login", "Logueando con $email y $password")
                    viewModel.signInWithEmailAndPassword(
                        email,
                        password
                    ) {//pasamos email, password, y la funcion que navega hacia home
                        navController.navigate(Screens.HomeScreen.name)
                    }

                }
            } else {
                Text(text = "Crear una cuenta")
                UserForm(isCreateAccount = true) { email, password ->
                    Log.d("My Login", "Creando cuenta con $email y $password")
                    viewModel.createUserWithEmailAndPassword(
                        email,
                        password
                    ) {//pasamos email, password, y la funcion que navega hacia home
                        navController.navigate(Screens.HomeScreen.name)
                    }

                }
            }
            // alternar entre Crear cuenta e iniciar sesion
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                val text1 = if (showLoginForm.value) "¿No tienes cuenta?"
                else "¿Ya tienes cuenta?"
                val text2 = if (showLoginForm.value) "Registrate"
                else "Inicia sesión"
                Text(text = text1)
                Text(text = text2,
                    modifier = Modifier
                        .clickable {
                            showLoginForm.value = !showLoginForm.value
                        }
                        .padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.secondary)
            }


            // GOOGLE
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { // Se crea un buider de opciones, una de ellas incluye un token

                        val opciones = GoogleSignInOptions
                            .Builder(
                                GoogleSignInOptions.DEFAULT_SIGN_IN
                            )
                            .requestIdToken(token) //requiere el token
                            .requestEmail() //y tb requiere el email
                            .build()
                        //creamos un cliente de logueo con estas opciones
                        val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
                        launcher.launch(googleSingInCliente.signInIntent)
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = "Login con GOOGLE",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                )

                Text(
                    text = "Login con Google",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // FACEBOOK
          /*  Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {

                        launcherFb.launch(listOf("email","public_profile"))
                    },
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_fb),
                    contentDescription = "Login con facebook",
                    modifier = Modifier
                        .padding(10.dp)
                        .size(40.dp)
                )

                Text(
                    text = "Login con Facebook",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }*/


        }
    }
}

@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit = { email, pwd -> }
) {

    val email = rememberSaveable {
        mutableStateOf("")
    }

    val password = rememberSaveable {
        mutableStateOf("")
    }

    val passwordVisible = rememberSaveable {
        mutableStateOf(false)
    }

    val valido = remember(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    //controla que al hacer clic en el boton submite, el teclado se oculte
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(
            emailState = email
        )

        PasswordInput(
            passwordState = password,
            passwordVisible = passwordVisible
        )

        SubmitButton(
            textId = if (isCreateAccount) "Crear cuenta" else "Login",
            inputValido = valido
        ) {
            onDone(email.value.trim(), password.value.trim())
            //se oculta el teclado, el ? es que se llama la función en modo seguro
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClic: () -> Unit
) {
    Button(
        onClick = onClic,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
        shape = CircleShape,
        enabled = inputValido
    ) {
        Text(
            text = textId,
            modifier = Modifier.padding(5.dp)
        )
    }
}


@Composable
fun EmailInput(
    emailState: MutableState<String>,
    labelId: String = "Email"
) {
    InputField(
        valuestate = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    valuestate: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true,
) {
    OutlinedTextField(
        value = valuestate.value,
        onValueChange = { valuestate.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)

    )
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    labelId: String = "Password",
    passwordVisible: MutableState<Boolean>
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else PasswordVisualTransformation()

    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        singleLine = true,
        modifier = Modifier
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            } else null
        }
    )
}

@Composable
fun PasswordVisibleIcon(
    passwordVisible: MutableState<Boolean>
) {
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility

    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(
            imageVector = image,
            contentDescription = ""
        )
    }
}
*/

@Composable
fun UserForm(
    isCreateAccount: Boolean,
    onDone: (String, String) -> Unit = { email, password -> }
) {
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val passwordVisible = rememberSaveable { mutableStateOf(false) }
    val valido = rememberSaveable(email.value, password.value) {
        email.value.trim().isNotEmpty() && password.value.trim().isNotEmpty()
    }

    //Controla que al hacer click en el boton submi, el teclado se oculta
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        EmailInput(emailState = email)
        PasswordInput(passwordState = password, passwordVisible = passwordVisible)
        SubmitButton(
            textId = if (isCreateAccount) "Crear Cuenta" else "Iniciar sesion",
            inputValido = true,
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}

@Composable
fun SubmitButton(
    textId: String,
    inputValido: Boolean,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        enabled = inputValido,
        shape = CircleShape,
        ButtonDefaults.buttonColors(
            containerColor = VERDESPOTIFY
        )
    ) {
        Text(text = textId, modifier = Modifier.padding(5.dp))
    }
}

@Composable
fun PasswordInput(
    passwordState: MutableState<String>,
    passwordVisible: MutableState<Boolean>,
    labelId: String = "Password"
) {
    val visualTransformation = if (passwordVisible.value)
        VisualTransformation.None
    else
        PasswordVisualTransformation()
    OutlinedTextField(
        value = passwordState.value,
        onValueChange = { passwordState.value = it },
        label = { Text(text = labelId) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            focusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.LightGray,
            unfocusedTextColor = Color.LightGray
        ),
        singleLine = true,
        modifier = Modifier
            .padding(
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = visualTransformation,
        trailingIcon = {
            if (passwordState.value.isNotBlank()) {
                PasswordVisibleIcon(passwordVisible)
            } else null
        }
    )
}

@Composable
fun PasswordVisibleIcon(passwordVisible: MutableState<Boolean>) {
    val image = if (passwordVisible.value)
        Icons.Default.VisibilityOff
    else
        Icons.Default.Visibility
    IconButton(onClick = { passwordVisible.value = !passwordVisible.value }) {
        Icon(imageVector = image, contentDescription = "Visibility Icon")
    }
}

@Composable
fun EmailInput(emailState: MutableState<String>, labelId: String = "Email") {
    InputField(
        valueState = emailState,
        labelId = labelId,
        keyboardType = KeyboardType.Email,
    )
}

@Composable
fun InputField(
    valueState: MutableState<String>,
    labelId: String,
    keyboardType: KeyboardType,
    isSingleLine: Boolean = true,
) {
    OutlinedTextField(
        value = valueState.value,
        onValueChange = { valueState.value = it },
        label = { Text(text = labelId) },
        singleLine = isSingleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.LightGray,
            focusedTextColor = Color.White,
            cursorColor = Color.White,
            focusedLabelColor = Color.LightGray,
            unfocusedTextColor = Color.LightGray
        ),
        modifier = Modifier
            .padding(
                bottom = 10.dp,
                start = 10.dp,
                end = 10.dp
            )
            .fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )
}

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var showLoginForm by rememberSaveable { mutableStateOf(true) }
    // Google
    // este token se consigue en Firebase->Proveedores de Acceso->Google->Conf del SKD->Id de cliente web
    val token = "230607497518-616jqsn48jvbu7gtd9gs8747mh8582sg.apps.googleusercontent.com"
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts
            .StartActivityForResult() // esto abrirá un activity para hacer el login de Google
    ) {
        val task =
            GoogleSignIn.getSignedInAccountFromIntent(it.data) // esto lo facilita la librería añadida
        // el intent será enviado cuando se lance el launcher
        try {
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)
            viewModel.signInWithGoogleCredential(credential) {
                navController.navigate(Screens.HomeScreen.name)
            }
        } catch (ex: Exception) {
            Log.d("My Login", "GoogleSignIn falló")
        }
    }

    // Facebook
    val scope = rememberCoroutineScope()
    val loginManager = LoginManager.getInstance()
    val callbackManager = remember { CallbackManager.Factory.create() }
    val launcherFb = rememberLauncherForActivityResult(
        loginManager.createLogInActivityResultContract(callbackManager, null)) {
        // nothing to do. handled in FacebookCallback

        scope.launch {
            val tokenFB = AccessToken.getCurrentAccessToken()
            val credentialFB = tokenFB?.let { FacebookAuthProvider.getCredential(it.token)}
            if(credentialFB != null){
                viewModel.signInWithFacebook(credentialFB){
                    navController.navigate(Screens.HomeScreen.name)
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(FONDO),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.iconospotify),
                    contentDescription = "Spotify Logo",
                    modifier = Modifier.size(30.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Spotify",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                TextButton(onClick = { showLoginForm = true }) {
                    Text(
                        text = "Iniciar Sesión",
                        color = if (showLoginForm) VERDESPOTIFY else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = if (showLoginForm) FontWeight.Bold else FontWeight.Normal
                    )
                }
                TextButton(onClick = { showLoginForm = false }) {
                    Text(
                        text = "Crear Cuenta",
                        color = if (!showLoginForm) VERDESPOTIFY else Color.Gray,
                        fontSize = 18.sp,
                        fontWeight = if (!showLoginForm) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (showLoginForm) {
                UserForm(isCreateAccount = false) { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate(Screens.HomeScreen.name)
                    }
                }
            } else {
                UserForm(isCreateAccount = true) { email, password ->
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(Screens.HomeScreen.name)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val opciones = GoogleSignInOptions
                        .Builder(
                            GoogleSignInOptions.DEFAULT_SIGN_IN
                        )
                        .requestIdToken(token) //requiere el token
                        .requestEmail() //y tb requiere el email
                        .build()
                    //creamos un cliente de logueo con estas opciones
                    val googleSingInCliente = GoogleSignIn.getClient(context, opciones)
                    launcher.launch(googleSingInCliente.signInIntent)
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Continuar con Google", color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(R.drawable.iconogoogle),
                        contentDescription = "Google Icon",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // FACEBOOK
            Button(
                onClick = {
                    launcherFb.launch(listOf("email","public_profile"))
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.LightGray),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Continuar con Facebook", color = Color.Black)
                    Spacer(modifier = Modifier.width(8.dp))
                    Image(
                        painter = painterResource(R.drawable.iconofacebook),
                        contentDescription = "Icono Facebook",
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { showLoginForm = !showLoginForm }) {
                Text(
                    text = if (showLoginForm) "No tengo cuenta, crear una cuenta" else "Ya tengo cuenta, iniciar sesión",
                    color = Color.Gray
                )
            }
        }
    }
}
