package com.example.ui.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AlertRed
import com.example.ui.theme.BgDark
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EasyGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SurfaceCard
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.BkashPaymentState

val BkashPink = Color(0xFFE2136E)
val BkashPinkDark = Color(0xFF9E0B4C)

@Composable
fun BkashPaymentDialog(
    state: BkashPaymentState,
    onPhoneChange: (String) -> Unit,
    onOtpChange: (String) -> Unit,
    onPinChange: (String) -> Unit,
    onSubmitStep: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(24.dp))
                .background(SurfaceDark)
                .border(1.dp, BorderHighlight, RoundedCornerShape(24.dp))
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // bKash Branded Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BkashPink)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "bKash",
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = Color.White.copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "PGW v1.2",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Body content based on step
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    when (state.step) {
                        1 -> {
                            // Step 1: Invoice Overview
                            Text(
                                text = "Upgrade to GroupBy Pro",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Unlock smart helmet visor casting, unlimited telemetry export, and priority CPaaS voice mesh.",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            // Invoice box
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(SurfaceCard)
                                    .border(1.dp, BorderSubtle, RoundedCornerShape(16.dp))
                                    .padding(16.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Invoice ID", color = TextMuted, fontSize = 13.sp)
                                    Text(text = state.invoiceId, color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Merchant", color = TextMuted, fontSize = 13.sp)
                                    Text(text = "GroupBy Technologies Ltd", color = TextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(text = "Amount", color = TextMuted, fontSize = 13.sp)
                                    Text(text = state.amount, color = BkashPink, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onSubmitStep,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BkashPink),
                                enabled = !state.isLoading
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text(text = "Proceed with bKash", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        2 -> {
                            // Step 2: Account Number & OTP
                            Text(
                                text = "Enter bKash Account",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "A 6-digit verification code will be sent via SMS.",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            OutlinedTextField(
                                value = state.customerPhone,
                                onValueChange = onPhoneChange,
                                label = { Text("bKash Wallet Number") },
                                placeholder = { Text("01XXXXXXXXX") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BkashPink,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedLabelColor = BkashPink
                                )
                            )

                            Spacer(modifier = Modifier.height(14.dp))

                            OutlinedTextField(
                                value = state.otp,
                                onValueChange = onOtpChange,
                                label = { Text("Verification OTP (Use: 1234)") },
                                placeholder = { Text("1234") },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BkashPink,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedLabelColor = BkashPink
                                )
                            )

                            state.errorMessage?.let { err ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = err, color = AlertRed, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onSubmitStep,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BkashPink),
                                enabled = !state.isLoading
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text(text = "Verify OTP & Continue", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        3 -> {
                            // Step 3: 5-digit PIN
                            Text(
                                text = "Enter bKash PIN",
                                color = TextPrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Enter your 5-digit bKash PIN to authorize payment of ${state.amount}.",
                                color = TextSecondary,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(18.dp))

                            OutlinedTextField(
                                value = state.pin,
                                onValueChange = onPinChange,
                                label = { Text("bKash PIN") },
                                placeholder = { Text("•••••") },
                                singleLine = true,
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                modifier = Modifier.fillMaxWidth(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = BkashPink,
                                    unfocusedBorderColor = BorderSubtle,
                                    focusedLabelColor = BkashPink
                                )
                            )

                            state.errorMessage?.let { err ->
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = err, color = AlertRed, fontSize = 12.sp)
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Button(
                                onClick = onSubmitStep,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(50.dp),
                                shape = RoundedCornerShape(25.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BkashPink),
                                enabled = !state.isLoading
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(color = Color.White, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                                } else {
                                    Text(text = "Confirm Payment", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        4 -> {
                            // Step 4: Success & Verified Webhook
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Success",
                                    tint = EasyGreen,
                                    modifier = Modifier.size(64.dp)
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "GroupBy Pro Activated!",
                                    color = TextPrimary,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "Payment of ${state.amount} successfully processed via bKash Webhook. All premium riding features unlocked.",
                                    color = TextSecondary,
                                    fontSize = 13.sp,
                                    textAlign = TextAlign.Center
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = onDismiss,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange)
                                ) {
                                    Text(text = "Done", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
