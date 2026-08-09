package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.QrCode2
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.UserProfileEntity
import com.example.ui.theme.BlueVerified
import com.example.ui.theme.GoldStar
import com.example.ui.theme.RedPrimary

data class SubscriberPackage(
    val id: String,
    val title: String,
    val subscribersCount: Int,
    val bonusCoins: Int,
    val priceInRs: Int,
    val isPopular: Boolean = false,
    val isBestValue: Boolean = false,
    val deliveryTime: String = "12 - 24 Hours"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BuySubscribersScreen(
    userProfile: UserProfileEntity?,
    onProcessUpiPayment: (
        packageTitle: String,
        amountRs: Int,
        coinCredit: Int,
        subscriberCredit: Int,
        utrReference: String
    ) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val upiId = "premisehjal@upi"

    val packages = listOf(
        SubscriberPackage(
            id = "pack_100",
            title = "Starter Booster Pack",
            subscribersCount = 100,
            bonusCoins = 1000,
            priceInRs = 99,
            isPopular = false,
            deliveryTime = "Instant - 2 Hours"
        ),
        SubscriberPackage(
            id = "pack_500",
            title = "Pro Channel Creator Pack",
            subscribersCount = 500,
            bonusCoins = 5000,
            priceInRs = 449,
            isPopular = true,
            deliveryTime = "6 - 12 Hours"
        ),
        SubscriberPackage(
            id = "pack_1000",
            title = "100K Silver Play Button Rush",
            subscribersCount = 1000,
            bonusCoins = 12000,
            priceInRs = 799,
            isBestValue = true,
            deliveryTime = "12 - 24 Hours"
        ),
        SubscriberPackage(
            id = "pack_5000",
            title = "Viral Superstar Mega Pack",
            subscribersCount = 5000,
            bonusCoins = 60000,
            priceInRs = 3499,
            deliveryTime = "24 - 48 Hours"
        )
    )

    var selectedPackageForUpi by remember { mutableStateOf<SubscriberPackage?>(null) }
    var utrInput by remember { mutableStateOf("") }
    var isVerifying by remember { mutableStateOf(false) }

    fun openUpiApp(appName: String, amount: Int) {
        val uri = Uri.parse("upi://pay?pa=$upiId&pn=PremiSehjalOfficial&am=$amount&cu=INR&tn=SubscriberPackage")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            Toast.makeText(context, "No $appName installed. Copy UPI ID below to pay.", Toast.LENGTH_LONG).show()
        }
    }

    fun copyUpiToClipboard() {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("UPI ID", upiId)
        clipboard.setPrimaryClip(clip)
        Toast.makeText(context, "UPI ID copied: $upiId", Toast.LENGTH_SHORT).show()
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag("buy_subscribers_screen"),
        contentPadding = PaddingValues(bottom = 90.dp)
    ) {
        // Hero Banner
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(
                                Brush.horizontalGradient(
                                    colors = listOf(
                                        RedPrimary,
                                        Color(0xFF0F172A),
                                        BlueVerified
                                    )
                                )
                            )
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    ) {
                        Surface(
                            color = GoldStar,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "⚡ INSTANT UPI PAYMENT",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Buy Real YouTube Subscribers via UPI",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        Text(
                            text = "Pay via GPay, PhonePe, Paytm, BHIM & get instant channel boost",
                            fontSize = 12.sp,
                            color = Color.LightGray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Current Channel Progress Indicator
                        Surface(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(14.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Current Subscribers Count",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${"%,d".format(userProfile?.channelSubscribersCount ?: 85400)} Subs",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = RedPrimary
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "100K Silver Button Goal",
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = "${"%,d".format((userProfile?.channelGoalSubscribers ?: 100000) - (userProfile?.channelSubscribersCount ?: 85400))} needed",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldStar
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // UPI Gateways Row
        item {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "💳 Supported UPI Payment Apps",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val apps = listOf("GPay", "PhonePe", "Paytm", "BHIM UPI")
                    apps.forEach { app ->
                        Surface(
                            color = MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Row(
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AccountBalanceWallet,
                                    contentDescription = null,
                                    tint = RedPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = app,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        }

        // Packages Section Title
        item {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "📦 Choose Subscriber Boost Package",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }

        // Packages List
        items(packages) { pack ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .clickable { selectedPackageForUpi = pack },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(
                    width = if (pack.isPopular || pack.isBestValue) 2.dp else 1.dp,
                    color = if (pack.isPopular) RedPrimary else if (pack.isBestValue) GoldStar else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (pack.isPopular) {
                            Surface(color = RedPrimary, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "🔥 MOST POPULAR",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        } else if (pack.isBestValue) {
                            Surface(color = GoldStar, shape = RoundedCornerShape(8.dp)) {
                                Text(
                                    text = "🌟 BEST VALUE",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        } else {
                            Text(
                                text = pack.title,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "₹${pack.priceInRs}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RedPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.People,
                            contentDescription = null,
                            tint = RedPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+${"%,d".format(pack.subscribersCount)} Subscribers",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.MonetizationOn,
                                contentDescription = null,
                                tint = GoldStar,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "+${"%,d".format(pack.bonusCoins)} Bonus Coins",
                                fontSize = 12.sp,
                                color = GoldStar,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Text(
                            text = "Delivery: ${pack.deliveryTime}",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = { selectedPackageForUpi = pack },
                        colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("buy_pack_button_${pack.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalanceWallet,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Pay ₹${pack.priceInRs} with UPI",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }

    // UPI Payment Sheet Dialog
    if (selectedPackageForUpi != null) {
        val pack = selectedPackageForUpi!!
        ModalBottomSheet(
            onDismissRequest = {
                selectedPackageForUpi = null
                utrInput = ""
            },
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            containerColor = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "💳 UPI Direct Payment Gateway",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    IconButton(onClick = { selectedPackageForUpi = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Summary Box
                Surface(
                    color = RedPrimary.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, RedPrimary.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = pack.title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "+${pack.subscribersCount} Subscribers & +${pack.bonusCoins} Coins",
                                fontSize = 12.sp,
                                color = RedPrimary
                            )
                        }

                        Text(
                            text = "₹${pack.priceInRs}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = RedPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // UPI Copy Box
                Surface(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Official UPI ID:",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = upiId,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        IconButton(
                            onClick = { copyUpiToClipboard() }
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.ContentCopy,
                                contentDescription = "Copy UPI ID",
                                tint = RedPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Tap your preferred app to pay ₹${pack.priceInRs}:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Launch App Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { openUpiApp("Google Pay", pack.priceInRs) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A73E8)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("GPay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { openUpiApp("PhonePe", pack.priceInRs) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5F259F)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("PhonePe", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    Button(
                        onClick = { openUpiApp("Paytm", pack.priceInRs) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00B9F1)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Paytm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Enter UTR / Transaction Reference ID
                OutlinedTextField(
                    value = utrInput,
                    onValueChange = { utrInput = it },
                    label = { Text("Enter 12-Digit UPI Transaction Ref / UTR No.") },
                    placeholder = { Text("e.g. 423819001234") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Verify Button
                Button(
                    onClick = {
                        val ref = if (utrInput.isNotBlank()) utrInput else "UPI_REF_${System.currentTimeMillis()}"
                        onProcessUpiPayment(
                            pack.title,
                            pack.priceInRs,
                            pack.bonusCoins,
                            pack.subscribersCount,
                            ref
                        )
                        selectedPackageForUpi = null
                        utrInput = ""
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("verify_upi_payment_button")
                ) {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Verify Payment & Credit ${pack.subscribersCount} Subs",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
