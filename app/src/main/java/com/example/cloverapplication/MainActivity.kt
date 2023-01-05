package com.example.cloverapplication

import android.accounts.Account
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clover.sdk.util.CloverAccount
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


class MainActivity: AppCompatActivity() {
    private lateinit var activityBinding: ActivityMainBinding
    private lateinit var managerBinding: RecyclerView.LayoutManager

    private lateinit var updateItemService: UpdateItemService

    private var mAccount: Account? = null
    private var mOrderConnector: OrderConnector? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        EventBus.getDefault().register(this)

        activityBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityBinding.root)
        managerBinding = LinearLayoutManager(this)

        mAccount = CloverAccount.getAccount(this)
    }

    override fun onResume() {
        super.onResume()

        checkingCloverAccount()
        connectToCloverConnector()

        updateItemService = UpdateItemService(applicationContext, mOrderConnector!!)

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
        disconnectToCloverConnector()

        EventBus.getDefault().unregister(this)
    }

    private fun connectToCloverConnector() {
        disconnectToCloverConnector()
        if (mAccount != null) {
            mOrderConnector = OrderConnector(this, mAccount, null)
            mOrderConnector!!.connect()
        }
    }

    private fun disconnectToCloverConnector() {
        if (mOrderConnector != null) {
            mOrderConnector!!.disconnect()
            mOrderConnector = null
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
        val percent = 25
        CoroutineScope(Job() + Dispatchers.Default).launch {
            updateItemService.updateItemsPriceOnPercentByOrderId(
                percent = 25,
                orderId = event.orderId,
                lineItemsId = event.lineItems
            )
        }
    }

}


