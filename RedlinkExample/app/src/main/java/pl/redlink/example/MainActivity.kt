package pl.redlink.example

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_event.view.*
import kotlinx.android.synthetic.main.dialog_user_data.view.*
import pl.redlink.push.analytics.RedlinkAnalytics
import pl.redlink.push.fcm.PushMessage
import pl.redlink.push.fcm.RedlinkFirebaseMessagingService
import pl.redlink.push.lifecycle.RedlinkActivity
import pl.redlink.push.manager.token.FcmTokenManager
import pl.redlink.push.manager.user.RedlinkUser
import pl.redlink.push.service.RegisterDeviceService
import java.util.*

class MainActivity : RedlinkActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvVersion.text = getString(R.string.version, BuildConfig.VERSION_NAME)
        initInteractions()
        initTokenAndUserData()
        registerReceivers()
    }

    private fun initInteractions() {
        btEditUser.setOnClickListener { showUserEditDialog() }
        btShareFcm.setOnClickListener { runShareIntent() }
        btRandomEvent.setOnClickListener { sendRandomEventWithParams() }
        btNewEvent.setOnClickListener { showEventDialog() }
    }

    override fun onDestroy() {
        unregisterReceivers()
        super.onDestroy()
    }

    private fun runShareIntent() {
        FcmTokenManager.get()?.let { token ->
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, token)
                type = "text/plain"
            }
            startActivity(sendIntent)
        } ?: Toast.makeText(this, R.string.token_no_registered, Toast.LENGTH_SHORT).show()
    }

    private fun initTokenAndUserData() {
        val userData = RedlinkUser.get()
        val values = TextUtils.join(
            USER_DATA_DELIMITER, listOf(
                userData?.lastName,
                userData?.firstName,
                userData?.email,
                userData?.phone,
                userData?.companyName
            )
        )
        tvUser.text = values
        token.text = FcmTokenManager.get()
    }

    /**
     * Shows dialog to edit user date
     */
    private fun showUserEditDialog() {
        val view = layoutInflater.inflate(
            R.layout.dialog_user_data,
            findViewById(android.R.id.content),
            false
        )
        fillDialogUserData(view)
        AlertDialog.Builder(this).apply {
            setPositiveButton(R.string.save) { _, _ ->
                val firstName = view.etFirstName.text.toString()
                val lastName = view.etLastName.text.toString()
                val email = view.etEmail.text.toString()
                val phone = view.etPhone.text.toString()
                val company = view.etCompany.text.toString()
                updateUserData(firstName, lastName, email, phone, company)
            }
            setTitle(R.string.edit_user_data)
            setView(view)
        }.show()
    }

    private fun updateUserData(
        firstName: String, lastName: String, email: String, phone: String, company: String) {
        //only local validation
        val isSucceed = RedlinkUser.Edit()
            .firstName(firstName)
            .lastName(lastName)
            .email(email)
            .phone(phone)
            .companyName(company)
            .removeCustomValues()
            .save()
        if (isSucceed) {
            initTokenAndUserData()
        } else {
            Toast.makeText(this, R.string.edit_user_error, Toast.LENGTH_SHORT).show()
        }
    }

    private fun fillDialogUserData(view: View) {
        val user = RedlinkUser.get()
        view.apply {
            etFirstName.setText(user?.firstName)
            etLastName.setText(user?.lastName)
            etEmail.setText(user?.email)
            etPhone.setText(user?.phone)
            etCompany.setText(user?.companyName)
        }
    }

    /**
     * Send random event with parameters
     */
    private fun sendRandomEventWithParams() {
        RedlinkAnalytics.trackEvent(
            name = "RandomEvent_${Random().nextInt(1000)}",
            userDataJson = "{\"test\": \"test\"}",
            params = mapOf(
                "randomString" to "paramValue",
                "randomInteger" to 152,
                "randomBoolean" to true,
                "randomDate" to Date()
            )
        )
    }

    /**
     * Send custom event without parameters
     */
    private fun showEventDialog() {
        val view =
            layoutInflater.inflate(R.layout.dialog_event, findViewById(android.R.id.content), false)
        AlertDialog.Builder(this).apply {
            setPositiveButton(R.string.save) { _, _ ->
                val eventName = view.etEventName.text.toString()
                RedlinkAnalytics.trackEvent(name = eventName)
            }
            setTitle(R.string.send_new_event)
            setView(view)
        }.show()
    }

    /**
     * Broadcast listeners
     */

    private fun registerReceivers() {
        val filter = IntentFilter(RedlinkFirebaseMessagingService.PUSH_ACTION)
        registerReceiver(messageListener, filter)
        val registerDeviceFilter = IntentFilter(RegisterDeviceService.ACTION_DEVICE_REGISTERED)
        registerReceiver(registerDeviceListener, registerDeviceFilter)
    }

    private fun unregisterReceivers() {
        unregisterReceiver(messageListener)
        unregisterReceiver(registerDeviceListener)
    }

    /**
     * Broadcast receiver for push messages
     */
    private val messageListener = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val pushMessage =
                intent?.getParcelableExtra<PushMessage>(RedlinkFirebaseMessagingService.EXTRA_PUSH_MESSAGE)
            Log.i("MainActivity", "Push received in MainActivity: ${pushMessage?.id}")
        }

    }

    /**
     * Broadcast receiver for registered token.
     */
    private val registerDeviceListener = object : BroadcastReceiver() {

        override fun onReceive(context: Context?, intent: Intent?) {
            val fcmToken = intent?.getStringExtra(RegisterDeviceService.EXTRA_REGISTERED_TOKEN)
            token.text = fcmToken
        }

    }

    companion object {
        private const val USER_DATA_DELIMITER = " \u2022 "
    }

}