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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.components.GroupByOrangeButton
import com.example.ui.theme.AccentCyan
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.BorderHighlight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.EasyGreen
import com.example.ui.theme.PrimaryOrange
import com.example.ui.theme.SurfaceDark
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun CreateGroupDialog(
    onDismiss: () -> Unit,
    onCreate: (name: String, description: String, initials: String, colorHex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf("#FF6B1A") }

    val colors = listOf("#FF6B1A", "#00E5FF", "#10B981", "#8B5CF6", "#EF4444")

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
                        text = "Create Riding Group",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Group Name") },
                    placeholder = { Text("e.g. Angeles Crest Riders") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Group Description") },
                    placeholder = { Text("e.g. Weekend morning sport rides") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Select Badge Color",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    colors.forEach { hex ->
                        val color = Color(android.graphics.Color.parseColor(hex))
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (selectedColor == hex) 3.dp else 0.dp,
                                    color = TextPrimary,
                                    shape = CircleShape
                                )
                                .clickable { selectedColor = hex }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                GroupByOrangeButton(
                    text = "Create Group",
                    enabled = name.isNotBlank(),
                    onClick = {
                        val initials = name.split(" ")
                            .filter { it.isNotBlank() }
                            .take(2)
                            .map { it.first().uppercase() }
                            .joinToString("")
                            .ifEmpty { "GP" }
                        onCreate(name, description, initials, selectedColor)
                    }
                )
            }
        }
    }
}

@Composable
fun JoinByCodeDialog(
    onDismiss: () -> Unit,
    onJoin: (String) -> Unit
) {
    var code by remember { mutableStateOf("") }

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
                        text = "Join by Invite Code",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Enter the 6-character code provided by your ride leader.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it.uppercase() },
                    label = { Text("Invite Code") },
                    placeholder = { Text("e.g. CC-9021") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentCyan,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                GroupByOrangeButton(
                    text = "Join Ride Group",
                    enabled = code.isNotBlank(),
                    onClick = { onJoin(code) }
                )
            }
        }
    }
}

@Composable
fun CreateRideDialog(
    onDismiss: () -> Unit,
    onCreate: () -> Unit
) {
    var rideTitle by remember { mutableStateOf("Sunset Malibu Run") }

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
                        text = "Create New Ride",
                        color = TextPrimary,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = TextSecondary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = rideTitle,
                    onValueChange = { rideTitle = it },
                    label = { Text("Ride Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryOrange,
                        unfocusedBorderColor = BorderSubtle
                    )
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Default Group: Canyon Carvers\nDefault Route: Mulholland Loop (42 mi)",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                GroupByOrangeButton(
                    text = "Start Ride Session",
                    onClick = {
                        onCreate()
                    }
                )
            }
        }
    }
}
