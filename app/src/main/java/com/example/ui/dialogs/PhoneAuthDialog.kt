package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GroupByOrangeButton
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EasyGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun PhoneAuthDialog(
    currentPhone: String,
    onDismiss: () -> Unit,
    onVerified: () -> Unit
) {
    var phoneInput by remember { mutableStateOf(currentPhone) }
    var otpInput by remember { mutableStateOf("") }
    var isOtpSent by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderHighlight, RoundedCornerShape(24.dp))
                .padding(24.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (!isOtpSent) "Rider Phone Verification" else "Enter 4-Digit OTP",
                        color = TextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = if (!isOtpSent) {
                        "Enter your mobile number to receive OTP via MiMSMS gateway."
                    } else {
                        "Enter the 4-digit code sent to $phoneInput (Default: 4421)"
                    },
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                if (!isOtpSent) {
                    OutlinedTextField(
                        value = phoneInput,
                        onValueChange = { phoneInput = it },
                        label = { Text("Phone Number") },
                        placeholder = { Text("+880 17XXXXXXXX / +1 (555)") },
                        singleLine = true,
                        leadingIcon = {
                            Icon(imageVector = Icons.Default.Phone, contentDescription = null, tint = PrimaryOrange)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryOrange,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GroupByOrangeButton(
                        text = if (isLoading) "Sending OTP..." else "Send Verification Code",
                        enabled = phoneInput.length > 6 && !isLoading,
                        onClick = {
                            scope.launch {
                                isLoading = true
                                delay(900)
                                isLoading = false
                                isOtpSent = true
                            }
                        }
                    )
                } else {
                    OutlinedTextField(
                        value = otpInput,
                        onValueChange = { if (it.length <= 4) otpInput = it },
                        label = { Text("4-Digit OTP") },
                        placeholder = { Text("4421") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EasyGreen,
                            unfocusedBorderColor = BorderSubtle
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    GroupByOrangeButton(
                        text = if (isLoading) "Verifying..." else "Verify & Sign In",
                        enabled = otpInput.length == 4 && !isLoading,
                        onClick = {
                            scope.launch {
                                isLoading = true
                                delay(800)
                                isLoading = false
                                onVerified()
                            }
                        }
                    )
                }
            }
        }
    }
}
