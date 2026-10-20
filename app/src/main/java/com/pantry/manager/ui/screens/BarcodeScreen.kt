package com.pantry.manager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BarcodeScreen(
    onItemScanned: (String, String) -> Unit = { _, _ -> }
) {
    var manualBarcode by remember { mutableStateOf("") }
    var scannedResult by remember { mutableStateOf<String?>(null) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Barcode Scanner") }) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.CameraAlt,
                        contentDescription = "Camera Viewfinder",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Point camera at barcode", color = Color.White, fontWeight = FontWeight.Medium)
                }
            }

            Button(
                onClick = {
                    val simulatedItem = "Simulated Grocery (EAN-13)"
                    scannedResult = "Scanned: $simulatedItem\nBarcode: 012345678905"
                    onItemScanned(simulatedItem, "012345678905")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.QrCodeScanner, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Simulate Barcode Scan")
            }

            HorizontalDivider()

            Text("Or enter barcode manually:", fontWeight = FontWeight.Bold)
            OutlinedTextField(
                value = manualBarcode,
                onValueChange = { manualBarcode = it },
                label = { Text("Barcode Digits") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                enabled = manualBarcode.isNotBlank(),
                onClick = {
                    scannedResult = "Fetched Item Info for Barcode: $manualBarcode"
                    onItemScanned("Manual Item ($manualBarcode)", manualBarcode)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Fetch Grocery Info from Internet")
            }

            scannedResult?.let { result ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Text(
                        text = result,
                        modifier = Modifier.padding(16.dp),
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
