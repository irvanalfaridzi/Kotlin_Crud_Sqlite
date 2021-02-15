package com.example.menumakananminuman

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    var id = 0
    var fileUri: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        try {
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                edtNama.setText(bundle.getString("MainActNama"))
                edtHarga.setText(bundle.getDouble("MainActHarga").toString())
                fileUri = bundle.getString("MainActGambar").toString()
                if (fileUri != "@drawable/gambar"){
                    Glide
                        .with(applicationContext)
                        .load(bundle.getString("MainActGambar"))
                        .centerCrop()
                        .placeholder(R.drawable.gambar)
                        .into(icProfilePict);
                }
            }
        } catch (ex: Exception) {
        }
        btAdd.setOnClickListener {
            var dbManager = MenuDbManager(this)

            var values = ContentValues()
            values.put("Nama", edtNama.text.toString())
            values.put("Harga", edtHarga.text.toString())
            if (fileUri == null) {
                fileUri = "@drawable/gambar"
            }
            values.put("Gambar", fileUri)

            if (id == 0) {
                val mID = dbManager.insert(values)

                if (mID > 0) {
                    Toast.makeText(this, "Add menu successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add menu!", Toast.LENGTH_LONG).show()
                }
            } else {
                var selectionArs = arrayOf(id.toString())
                val mID = dbManager.update(values, "Id=?", selectionArs)

                if (mID > 0) {
                    Toast.makeText(this, "Add menu successfully!", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    Toast.makeText(this, "Fail to add menu!", Toast.LENGTH_LONG).show()
                }
            }
        }

        btnPickImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data?.data.toString()
//           glide
            Glide
                .with(applicationContext)
                .load(fileUri)
                .centerCrop()
                .placeholder(R.drawable.gambar)
                .into(icProfilePict);
            Log.d("Response", "onActivityResult: $fileUri")
            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}