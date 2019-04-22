package com.example.tristanlelievre.my_very_app

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.activity_task_description.*

class TaskDescriptionActivity : AppCompatActivity() {
    companion object { // seems to make it a global var
        val EXTRA_TASK_DESCRIPTION = "task"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_description)
    }

    fun doneClicked(view: View) {
        val taskDescription = descriptionText.text.toString()
        Log.d("TAG", taskDescription)
        if (!taskDescription.isEmpty()) {
            Log.d("TAG", "c'est pas vide")
            // 2
            val result = Intent()
            result.putExtra(EXTRA_TASK_DESCRIPTION, taskDescription)
            setResult(Activity.RESULT_OK, result)
        } else {
            // 3
            Log.d("TAG", "c'est vide")
            setResult(Activity.RESULT_CANCELED)
        }

// 4
        finish()
    }
}
