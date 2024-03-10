package com.android.msend

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.webkit.PermissionRequest
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.wafflecopter.multicontactpicker.ContactResult
import com.wafflecopter.multicontactpicker.LimitColumn
import com.wafflecopter.multicontactpicker.MultiContactPicker


class MainActivity : AppCompatActivity() {
    val CONTACT_PICKER_REQUEST: Int= 202
    lateinit var results: List<ContactResult>
    lateinit var  txt_mobile:EditText
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val txt_message =findViewById<EditText>(R.id.txt_message);
         txt_mobile=findViewById<EditText>(R.id.txt_mobile);
        val txt_count=findViewById<EditText>(R.id.txt_count);
        val btn_manual=findViewById<Button>(R.id.btn_manual);
        val btn_sms=findViewById<Button>(R.id.btn_sms);
        val btn_whatsapp=findViewById<Button>(R.id.btn_whatsapp);
        val btn_choose=findViewById<Button>(R.id.choose);
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_CONTACTS,

            ).withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { /* ... */
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: MutableList<com.karumi.dexter.listener.PermissionRequest>?,
                    p1: PermissionToken?
                ) {
                    
                }


            }).check()
        btn_sms.setOnClickListener{
          try{
              if(!results.isEmpty()){
                  for (j in 0 until results.size){
                      for (i in 0 until txt_count.text.toString().toInt()) {
                          val smsManager: SmsManager=SmsManager.getDefault();
                          smsManager.sendTextMessage(results[j].phoneNumbers[0].number,null,txt_message.getText().toString(),null,null)
                          Toast.makeText(this,"SMS sending",Toast.LENGTH_SHORT).show()

                      }
                  }
              }


          }catch (e: Exception){
              Toast.makeText(this,"SMS sending  failed !",Toast.LENGTH_SHORT).show()
          }


        }
        btn_choose.setOnClickListener{


            MultiContactPicker.Builder(this) // Activity/fragment context

                .hideScrollbar(false) // Optional - default: false
                .showTrack(true) // Optional - default: true
                .searchIconColor(Color.WHITE) // Option - default: White
                .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) // Optional - default: CHOICE_MODE_MULTIPLE
                .handleColor(ContextCompat.getColor(this, R.color.black)) // Optional - default: Azure Blue
                .bubbleColor(ContextCompat.getColor(this, R.color.white)) // Optional - default: Azure Blue
                .bubbleTextColor(Color.WHITE) // Optional - default: White
                .setTitleText("Select Contacts") // Optional - default: Select Contacts

                .setLoadingType(MultiContactPicker.LOAD_ASYNC) // Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                .limitToColumn(LimitColumn.NONE) // Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                .setActivityAnimations(
                    android.R.anim.fade_in, android.R.anim.fade_out,
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
                ) // Optional - default: No animation overrides
                .showPickerForResult(CONTACT_PICKER_REQUEST)


        }
        btn_manual.setOnClickListener{

        }
        btn_whatsapp.setOnClickListener{

        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
              results = MultiContactPicker.obtainResult(data)
                val names= StringBuilder(results[0].displayName)
                for(j in 0 until results.size){
                    if (j!=0){
                        names.append(", ").append(results[j].displayName);
                    }
                }
                txt_mobile.setText(names)
                Log.d("MyTag", results[0].displayName)
            } else if (resultCode == RESULT_CANCELED) {
                println("User closed the picker without selecting items.")
            }
        }
    }

}