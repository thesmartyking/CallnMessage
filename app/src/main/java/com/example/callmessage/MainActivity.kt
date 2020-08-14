package com.example.callmessage

import android.Manifest
import android.content.Intent
import android.content.Intent.ACTION_CALL
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MainActivity : AppCompatActivity() {
    private val REQUEST_CALL = 1
    private val REQUEST_msg = 1
    var permissioncode:Int=0
    private lateinit var mEditTextNumber: EditText
    private lateinit var meditmsg: EditText
    private lateinit var imageCall: ImageButton
    private lateinit var imagemsg: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditTextNumber = findViewById(R.id.callphoneTxt)
        imageCall= findViewById(R.id.call)

        meditmsg = findViewById(R.id.messageEditTxt)
        imagemsg = findViewById(R.id.msg1)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED)
        {
            /*ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE),1)
            ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS),1)
            Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show()*/

            val permissions = arrayOf(Manifest.permission.CALL_PHONE,Manifest.permission.SEND_SMS)
            requestPermissions(permissions, 100)
        }
        else
            permissiongranted()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.SEND_SMS)
            requestPermissions(permissions, 100)

        }
        else
            permissiongranted()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED) {
            val permissions = arrayOf(Manifest.permission.CALL_PHONE)
            requestPermissions(permissions, 100)

        }
        else
            permissiongranted()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED) {
            permissiongranted()

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            100 -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissiongranted()
                } else {
                    Toast.makeText(this, "Permission denied...! So Restart The App", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun permissiongranted()
    {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
            == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
            == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this,"Permission Accessed",Toast.LENGTH_SHORT).show()
            mEditTextNumber.setOnFocusChangeListener { v, hasFocus ->
                callphone_til.setError(null)
                if (mEditTextNumber.text.toString().trim().isEmpty()) {
                    callphone_til.setError("Enter Phone Number")
                }
                else if(!mEditTextNumber.text.toString().trim().isEmpty())
                {
                    if (mEditTextNumber.text.toString().trim().length<10) {

                        callphone_til.setError("Enter 10 Digit Phone Number")
                        Log.d("Invalid Number:- ","${mEditTextNumber.text} ")
                    }
                    else {
                        if(mEditTextNumber.text.toString().trim().startsWith("9",true)||
                            mEditTextNumber.text.toString().trim().startsWith("8",true)||
                            mEditTextNumber.text.toString().trim().startsWith("7",true)||
                            mEditTextNumber.text.toString().trim().startsWith("6",true)
                        )
                        {
                            Log.d("Valid Number:- ","${mEditTextNumber.text} ")
                            callphone_til.setError(null)


                        }
                        else
                        {
                            callphone_til.setError("Enter Valid Phone Number\nStarting with 9,8,7,6")
                            Log.d("Invalid Number:- ","${mEditTextNumber.text} ")
                        }

                    }
                }

            }

            meditmsg.setOnFocusChangeListener { v, hasFocus ->
                message_til.setError(null)
                if (meditmsg.text.toString().trim().isEmpty()) {
                    message_til.setError("Enter Message")
                }
                else if(!meditmsg.text.toString().trim().isEmpty())
                {
                    /*if (meditmsg.text.toString().trim().length<3) {
                        message_til.setError("Enter Valid Name with Last Name")
                        Log.d("Invalid Fullname :- ","${meditmsg.text} ")
                    }
                    else {*/
                    Log.d("Message:- ","${meditmsg.text} ")
                    message_til.setError(null)
//                }
                }

            }

            imageCall.setOnClickListener { makePhoneCall() }

            imagemsg.setOnClickListener { makeMsg() }
        }
    }

    private fun makePhoneCall() {
        callphone_til.setError(null)
        val number = mEditTextNumber.text.toString()
        if (number.trim().length > 0) {


                val dial = "tel:+91 $number"
                startActivity(Intent(ACTION_CALL, Uri.parse(dial)))
            Toast.makeText(this, "Calling...", Toast.LENGTH_SHORT).show()

        } else {
            callphone_til.setError("Enter Phone Number")
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
    }

    private fun makeMsg() {
        callphone_til.setError(null)
        message_til.setError(null)
        val number = mEditTextNumber.text.toString()
        val msg = meditmsg.text.toString()
        if (number.trim().length > 0 && msg.trim().length > 0) {

                val smsManager = SmsManager.getSmsManagerForSubscriptionId(0)
                smsManager.sendTextMessage(number, null, msg, null, null)


            /*val send = "smsto:$number"
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(send)))*/
            Toast.makeText(this, "Message Sent", Toast.LENGTH_SHORT).show()
        }
        else if(!number.trim().isEmpty() && msg.trim().isEmpty())
        {
            callphone_til.setError(null)
            message_til.setError("Enter Message")
            Toast.makeText(this, "Enter Message", Toast.LENGTH_SHORT).show()
        }
        else if(number.trim().isEmpty() && !msg.trim().isEmpty())
        {
            message_til.setError(null)
            callphone_til.setError("Enter Phone Number")
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show()
        }
        else {
            callphone_til.setError("Enter Phone Number")
            message_til.setError("Enter Message")
            Toast.makeText(this, "Enter Phone Number \nEnter Message", Toast.LENGTH_SHORT).show()
        }
    }


  /* open fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String?>?, @NonNull grantResults: IntArray) {
        if (requestCode == REQUEST_CALL) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
        if (requestCode == REQUEST_msg) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeMsg()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }
    }*/
}
