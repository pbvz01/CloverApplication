package com.example.cloverapplication.service

import android.content.Context
import com.clover.sdk.v3.order.OrderConnector
import com.example.cloverapplication.database.AppDatabase
import com.example.cloverapplication.database.entity.UpdateItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.*

class UpdateItemService(
    private val context: Context,
    private val orderConnector: OrderConnector
) {

    suspend fun getAllUpdateItems(): List<UpdateItem> {
        return AppDatabase.getDatabase(context)
            .updateItemDao()
            .selectAllUpdateItem()
    }

    suspend fun updateItemsPriceOnPercentByOrderId(
        percent: Int, orderId: String, lineItemsId: List<String>)
            = CoroutineScope(Job() + Dispatchers.Default).launch {
        val updateItems = mutableListOf<UpdateItem>()
        val lineItems = orderConnector.getOrder(orderId).lineItems

        lineItems
            .filter { lineItemsId.contains(it.id) }
            .forEach {
                val oldPrice = (it.price / 100).toDouble()
                val newPrice = oldPrice + (percent * oldPrice / 100)
                updateItems.add(
                    UpdateItem(oldPrice, newPrice, Date().time, orderId, it.id)
                )
                it.price = (newPrice * 100).toLong()
                //For showing that the item line obj have new price
                println(it.toString())
            }
            .also {
                if (updateItems.isNotEmpty()) {
                    addUpdateItemsToDB(updateItems)
                }
            }
    }

    private suspend fun addUpdateItemsToDB(updateItems: List<UpdateItem>) {
        AppDatabase.getDatabase(context)
            .updateItemDao()
            .insertAllUpdateItem(updateItems)
    }

}