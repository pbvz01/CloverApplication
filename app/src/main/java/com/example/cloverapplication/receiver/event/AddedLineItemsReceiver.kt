package com.example.cloverapplication.receiver.event

import com.clover.sdk.v3.order.LineItem

data class AddedLineItemsReceiver(
    val orderId: String,
    val lineItems: List<String>
)