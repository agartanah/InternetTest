package com.bignerdranch.android.internettest

import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : AppCompatActivity() {
    val TAG = "Flickr cats"
    val TAGok = "Flickr OkCats"

    lateinit var button: Button
    lateinit var buttonOk: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById(R.id.btnHTTP)
        buttonOk = findViewById(R.id.btnOkHTTP)

        button.setOnClickListener {
            Thread( Runnable {
                val connection = URL("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
                    .openConnection() as HttpURLConnection
                connection.apply {
                    connectTimeout = 5000
                    requestMethod = "GET"
                    doInput = true
                }

                connection.inputStream.bufferedReader().use { reader ->
                    Log.d(TAG, reader.readText())
                }
            }).start()
        }

        buttonOk.setOnClickListener {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url("https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=ff49fcd4d4a08aa6aafb6ea3de826464&tags=cat&format=json&nojsoncallback=1")
                .build()

            client.newCall(request).enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    response.use {
                        val data = it.body?.string()

                        Log.i(TAGok, data.toString())
                    }
                }
            })
        }
    }
}