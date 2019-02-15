package pl.redlink.redlinkkotlinexample

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import pl.redlink.push.fcm.PushMessage
import pl.redlink.push.fcm.RedlinkFirebaseMessagingService
import pl.redlink.push.lifecycle.RedlinkActivity

class MainActivity : RedlinkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intentFilter = IntentFilter(RedlinkFirebaseMessagingService.PUSH_ACTION)
        registerReceiver(pushBroadcast, intentFilter)
    }

    override fun onDestroy() {
        unregisterReceiver(pushBroadcast)
        super.onDestroy()
    }

    private val pushBroadcast: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val pushMessage = intent?.getParcelableExtra<PushMessage>(RedlinkFirebaseMessagingService.EXTRA_PUSH_MESSAGE)
            //todo do something with the push message
        }
    }

}
