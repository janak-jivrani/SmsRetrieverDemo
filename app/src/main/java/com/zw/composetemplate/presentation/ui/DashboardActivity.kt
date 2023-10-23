package com.zw.composetemplate.presentation.ui

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.bharadwaj.navigationbarmedium.MainActivity
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.mukeshsolanki.OTP_VIEW_TYPE_BORDER
import com.mukeshsolanki.OtpView
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.zw.composetemplate.R
import com.zw.composetemplate.core.MessageBroadcastReceiver
import com.zw.composetemplate.core.MessageListenerInterface
import com.zw.composetemplate.core.SmsBroadcastReceiver
import com.zw.composetemplate.core.SmsBroadcastReceiver.SmsBroadcastReceiverListener
import com.zw.composetemplate.presentation.viewmodels.DashboardViewModel
import com.zw.composetemplate.theme.ActiveColor
import dagger.hilt.android.AndroidEntryPoint
import ir.kaaveh.sdpcompose.sdp
import ir.kaaveh.sdpcompose.ssp
import kotlinx.coroutines.launch
import java.util.regex.Matcher
import java.util.regex.Pattern


@AndroidEntryPoint
class DashboardActivity : ComponentActivity() {
    private val viewModel: DashboardViewModel by viewModels()
    lateinit var smsBroadcastReceiver : SmsBroadcastReceiver
    val activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            //That gives all message to us.
            // We need to get the code from inside with regex
            val message: String? = result.data!!.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
            message?.let {
                getOtpFromMessage(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Handle the splash screen transition.
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContent {
            DashboardScreen(viewModel)
        }

        // Keep the splash screen visible for this Activity.
        splashScreen.setKeepOnScreenCondition { viewModel.initialSataLoad.value == false }

        // Set up an OnPreDrawListener to the root view.
//        val content: View = findViewById(android.R.id.content)
//        content.viewTreeObserver.addOnPreDrawListener(
//            object : ViewTreeObserver.OnPreDrawListener {
//                override fun onPreDraw(): Boolean {
//                    // Check whether the initial data is ready.
//                    return if (viewModel.initialSataLoad.value == true) {
//                        // The content is ready. Start drawing.
//                        content.viewTreeObserver.removeOnPreDrawListener(this)
//                        true
//                    } else {
//                        // The content isn't ready. Suspend.
//                        false
//                    }
//                }
//            }
//        )
        viewModel.setLoaded(true)

//
//        // Add a callback that's called when the splash screen is animating to the
//        // app content.
//        splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
//            val splashScreenView = splashScreenViewProvider.view
//            // Create your custom animation.
//            val slideUp = android.animation.ObjectAnimator.ofFloat(
//                splashScreenView,
//                View.TRANSLATION_Y,
//                0f,
//                -splashScreenView.height.toFloat()
//            )
//            slideUp.interpolator = AnticipateInterpolator()
//            slideUp.duration = 200L
//
//            // Call SplashScreenView.remove at the end of your custom animation.
//            slideUp.doOnEnd { splashScreenViewProvider.remove() }
//
//            // Run your animation.
//            slideUp.start()
//
//
//            // Get the duration of the animated vector drawable.
//            val animationDuration = splashScreenView.iconAnimationDuration
//            // Get the start time of the animation.
//            val animationStart = splashScreenView.iconAnimationStart
//            // Calculate the remaining duration of the animation.
//            val remainingDuration = if (animationDuration != null && animationStart != null) {
//                (animationDuration - Duration.between(animationStart, Instant.now()))
//                    .toMillis()
//                    .coerceAtLeast(0L)
//            } else {
//                0L
//            }
//
//        }

        //TODO: ask to user for read OTP consent dialog
        //startSmsUserConsent()
        // adding bind listener for message receiver on below line.
        MessageBroadcastReceiver.bindListener(object : MessageListenerInterface {
            override fun messageReceived(message: String?) {
                message?.let {
                    getOtpFromMessage(it)
                }
            }
        });

        viewModel.otpResponse.observe(this) {
            //TODO: handle response
            it.onSuccess {
                Toast.makeText(this@DashboardActivity, "Response Success= ${response.body()?.message}",Toast.LENGTH_LONG).show()
            }.onFailure {
                Toast.makeText(this@DashboardActivity, "Response fail= $it",Toast.LENGTH_LONG).show()
            }.onError {
                Toast.makeText(this@DashboardActivity, "Response error= $it",Toast.LENGTH_LONG).show()
            }.onException {
                Toast.makeText(this@DashboardActivity, "Response exception= $it",Toast.LENGTH_LONG).show()
            }
            viewModel.updateOTP("")
        }
    }

    private fun startSmsUserConsent() {
        val client = SmsRetriever.getClient(this)
        //We can add sender phone number or leave it blank
        // I'm adding null here
        client.startSmsUserConsent(null).addOnSuccessListener {
            Toast.makeText(
                /* context = */ applicationContext,
                /* text = */ "On Success",
                /* duration = */ Toast.LENGTH_LONG
            ).show()
        }.addOnFailureListener {
            Toast.makeText(
                applicationContext,
                "On OnFailure",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    private fun getOtpFromMessage(message: String) {
        // This will match any 6 digit number in the message
        val pattern = Pattern.compile("(|^)\\d{6}")
        val matcher: Matcher = pattern.matcher(message)
        if (matcher.find()) {
            matcher.group(0)?.let {
                viewModel.updateOTP(it)
            }
        }
    }

    private fun registerBroadcastReceiver() {
        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver.smsBroadcastReceiverListener = object : SmsBroadcastReceiverListener {
            override fun onSuccess(intent: Intent?) {
                activityResultLauncher.launch(intent!!)
            }

            override fun onFailure() {}
        }
        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver, intentFilter)
    }

    override fun onStart() {
        super.onStart()
        //registerBroadcastReceiver()
    }

    override fun onStop() {
        super.onStop()
        //unregisterReceiver(smsBroadcastReceiver)
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val otpRemember by remember { viewModel.otpValue }
    val permissions = arrayOf(Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS)
    val rememberStoragePermissionState = rememberMultiplePermissionsState(permissions = permissions.toList())
    val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        var allGranted = false
        it.entries.forEach {entry->
            if (!entry.value) {
                allGranted = false
            }
        }

        if (allGranted) {
            //performAfterPermission(context = context)
        } else {
            var shouldShowRationale = true
            rememberStoragePermissionState.permissions.forEach { permissionState ->
                if (!permissionState.status.shouldShowRationale) {
                    shouldShowRationale = false
                }
            }

            if (shouldShowRationale) {
                //We can ask permission again
            } else {
                //Send user to setting screen
            }
        }
    }

    LaunchedEffect(rememberStoragePermissionState) {
        val permissionResult = rememberStoragePermissionState.allPermissionsGranted
        if (!permissionResult) {
            /*if (shouldShowRationale) {
                // Show a rationale if needed (optional)
            } else {

            }*/
            // Request the permission
            lifecycleOwner.lifecycleScope.launch {
                requestPermissionLauncher.launch(permissions)
            }
        }/* else {
            performAfterPermission(context = context)
        }*/
    }

    Scaffold(
        topBar = { CenterAlignedTopAppBar(
            title = { Text(text = stringResource(R.string.app_name), fontSize = 18.ssp) }
        ) },
        content = { padding ->
            Column(modifier = Modifier.padding(padding), horizontalAlignment = Alignment.CenterHorizontally) {
                if (rememberStoragePermissionState.allPermissionsGranted) {
                    OtpView(
                        otpText = otpRemember,
                        onOtpTextChange = {
                            viewModel.updateOTP(it)
                        },
                        type = OTP_VIEW_TYPE_BORDER,
                        password = false,
                        containerSize = 48.sdp,
                        passwordChar = "•",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        charColor = Color.White,
                        otpCount = 6
                    )
                    /*Spacer(modifier = Modifier.height(16.sdp))
                    Button(
                        enabled = otpRemember.length > 5,
                        onClick = {
                            lifecycleOwner.lifecycleScope.launch {
                                viewModel.callOTPApi()
                            } },
                        colors = ButtonDefaults.buttonColors(containerColor = ActiveColor)
                    ) {
                        Text(text = "Call Api")
                    }*/

                } else {
                    Button(
                        onClick = {
                            lifecycleOwner.lifecycleScope.launch {
                                requestPermissionLauncher.launch(permissions)
                            } },
                        colors = ButtonDefaults.buttonColors(containerColor = ActiveColor)
                    ) {
                        Text(text = stringResource(id = R.string.grant_permission))
                    }
                }
            }
        },
        containerColor = colorResource(R.color.colorPrimaryDark) // Set background color to avoid the white flashing when you switch between screens
    )
}
