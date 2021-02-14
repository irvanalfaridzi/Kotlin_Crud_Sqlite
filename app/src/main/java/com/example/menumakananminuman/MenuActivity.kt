package com.example.menumakananminuman

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_menu.*

class MenuActivity : AppCompatActivity() {
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        try {
            var bundle: Bundle = intent.extras!!
            id = bundle.getInt("MainActId", 0)
            if (id != 0) {
                edtNama.setText(bundle.getString("MainActNama"))
                edtHarga.setText(bundle.getString("MainActHarga"))
            }
        } catch (ex: Exception) {
        }
        btAdd.setOnClickListener {
            var dbManager = MenuDbManager(this)

            var values = ContentValues()
            values.put("Nama", edtNama.text.toString())
            values.put("Harga", edtHarga.text.toString())

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
    }
}