package com.example.searchimage

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class ImageDescriptionActivity : AppCompatActivity() {
    var data: Data? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.image_descriptiona_activity)

        val searchIV = findViewById<ImageView>(R.id.serch_imageView)
        val backIV = findViewById<ImageView>(R.id.back_icon)
        val commentsTV = findViewById<TextView>(R.id.comments_tv)
        val commentsET = findViewById<EditText>(R.id.comments_et)
        val commentsBtn = findViewById<Button>(R.id.enter_comments_btn)
        commentsBtn.setOnClickListener(View.OnClickListener {
            try {
                val imm =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            } catch (e: java.lang.Exception) {
                // TODO: handle exception
            }
            val dbHandler = ImageCommentsOpenHelper(this, null)
            data?.let { it1 ->
                dbHandler.addComments(it1, commentsET.text.toString())
                Toast.makeText(this@ImageDescriptionActivity, "Saved Comment!", Toast.LENGTH_SHORT)
                    .show()
            }
        })
        backIV.setOnClickListener(View.OnClickListener {
            finish()

        })


        val intent = intent
        if (intent != null) {
            try {
                data = intent.getSerializableExtra("data") as Data?
                commentsTV.setText(data!!.name)
                if (data != null) {
                    Glide.with(this@ImageDescriptionActivity)
                        .load(data!!.link)
                        .into(searchIV)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        val dbHandler = ImageCommentsOpenHelper(this, null)
        val cursor = data?.let { dbHandler.getCommnets(it) }
        if (cursor != null && cursor.count != 0) {
            cursor!!.moveToFirst()
            commentsET.append((cursor.getString(cursor.getColumnIndex(ImageCommentsOpenHelper.COLUMN_NAME))))
        }
        cursor!!.close()


    }
}