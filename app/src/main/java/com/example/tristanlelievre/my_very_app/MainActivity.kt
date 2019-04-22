package com.example.tristanlelievre.my_very_app

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun countMe (view: View) {
        // Get the text view
        val showCountTextView = findViewById<TextView>(R.id.textView)

        // Get the value of the text view.
        val countString = showCountTextView.text.toString()

        // Convert value to a number and increment it
        var count: Int = Integer.parseInt(countString)
        count++

        // Display the new value in the text view.
        showCountTextView.text = count.toString();
    }

    fun randomMe(view: View){
        // Create an Intent to start the second activity
        val randomIntent = Intent(this, Main2Activity::class.java)
        val countString = textView.text.toString()

        // Convert the count to an int
        val count = Integer.parseInt(countString)

        // Add the count to the extras for the Intent.
        randomIntent.putExtra(Main2Activity.TOTAL_COUNT, count)

        // Start the new activity.
        startActivity(randomIntent)
    }

    fun fragMe(view: View){
        val secondIntent = Intent(this, secondFragment::class.java)
        startActivity(secondIntent)
    }

    fun forgetMeNot(view: View){
        val secondIntent = Intent(this, ForgetMeNotActivity::class.java)
        startActivity(secondIntent)
    }

    fun recyclerMe(view: View){
        val fourthIntent = Intent(this, Recycler_main_activity::class.java)
        startActivity(fourthIntent)
    }

    }
