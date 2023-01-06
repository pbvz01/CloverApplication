package com.example.cloverapplication

import android.accounts.Account
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.math.MathUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
import com.clover.sdk.v3.inventory.InventoryConnector
import com.clover.sdk.v3.order.OrderConnector
import com.example.cloverapplication.adapter.UpdateItemAdapter
import com.example.cloverapplication.databinding.ActivityMainBinding
import com.example.cloverapplication.receiver.event.AddedLineItemsReceiver
import com.example.cloverapplication.service.UpdateItemService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import kotlin.random.Random


class MainActivity: AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var managerBinding: RecyclerView.LayoutManager

    private lateinit var updateItemService: UpdateItemService

    private var mAccount: Account? = null
    private var mOrderConnector: OrderConnector? = null
    private var mInventoryConnector: InventoryConnector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        mAccount = CloverAccount.getAccount(this)
        checkingCloverAccount()
        connectToCloverOrder()
        connectToCloverInventory()

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        managerBinding = LinearLayoutManager(this)


    }

    override fun onResume() {
        super.onResume()

        updateItemService =
            UpdateItemService(applicationContext, mOrderConnector!!, mInventoryConnector!!)

        CoroutineScope(Job() + Dispatchers.Main).launch {
            val items = updateItemService.getAllUpdateItems()

            activityBinding.recyclerView.apply {
                adapter = UpdateItemAdapter(items)
                layoutManager = managerBinding
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromCloverOrder()
        disconnectFromCloverInventory()

        EventBus.getDefault().unregister(this)
    }

    private fun connectToCloverOrder() {
        disconnectFromCloverOrder()
        if (mAccount != null) {
            mOrderConnector = OrderConnector(this, mAccount, null)
            mOrderConnector!!.connect()
        }
    }

    private fun disconnectFromCloverOrder() {
        if (mOrderConnector != null) {
            mOrderConnector!!.disconnect()
            mOrderConnector = null
        }
    }

    private fun connectToCloverInventory() {
        disconnectFromCloverInventory()
        if (mAccount != null) {
            mInventoryConnector = InventoryConnector(this, mAccount, null)
            mInventoryConnector!!.connect()
        }
    }

    private fun disconnectFromCloverInventory() {
        if (mInventoryConnector != null) {
            mInventoryConnector!!.disconnect()
            mInventoryConnector = null
        }
    }

    private fun checkingCloverAccount() {
        if (mAccount == null) {
            mAccount = CloverAccount.getAccount(this)

            if (mAccount == null) {
                return
            }
        }
    }

    @Subscribe
    fun handelRequestFromOrderReceiver(event: AddedLineItemsReceiver) {
        val percent = Random.nextInt(5, 26)
        CoroutineScope(Job() + Dispatchers.Default).launch {
            updateItemService.updateItemsPriceOnPercentByOrderId(
                percent = percent,
                orderId = event.orderId,
                lineItemsId = event.lineItems
            )
        }
    }

}


