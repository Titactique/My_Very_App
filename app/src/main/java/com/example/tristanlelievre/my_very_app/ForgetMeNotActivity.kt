package com.example.tristanlelievre.my_very_app

import android.app.Activity
import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter

import kotlinx.android.synthetic.main.activity_forget_me_not.*
import kotlinx.android.synthetic.main.content_forget_me_not.*
import java.text.SimpleDateFormat
import java.util.*

class ForgetMeNotActivity : AppCompatActivity() {
    private val ADD_TASK_REQUEST = 1
    private val taskList: MutableList<String> = mutableListOf()

    private val adapter by lazy { makeAdapeter(taskList) }
    private val tickReceiver by lazy { makeBroadcastReceiver() }

    private val PREFS_TASKS = "prefs_tasks" // persistant data
    private val KEY_TASKS_LIST = "tasks_list"

    companion object {
        private const val LOG_TAG = "ForgetMeNotActivityLog"

        private fun getCurrentTimeStamp(): String {
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
            val now = Date()
            return simpleDateFormat.format(now)
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forget_me_not)
        setSupportActionBar(toolbar)
        taskListView.adapter = adapter // set up the adapter for taskListView
        taskListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            taskSelected(position)
        } // add an empty OnItemClickListener() to the ListView to capture the user’s taps on individual list entries
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val savedList = getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE).getString(KEY_TASKS_LIST, null) // get back saved infos
        if (savedList != null) {
            val items = savedList.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            taskList.addAll(items)
        }

    }
    override fun onResume() {
        // 1
        super.onResume()
        // 2
        dateTimeTextView.text = getCurrentTimeStamp()
        // 3
        registerReceiver(tickReceiver, IntentFilter(Intent.ACTION_TIME_TICK))
    }

    override fun onPause() {
        // 4
        super.onPause()
        // 5
        try {
            unregisterReceiver(tickReceiver)
        } catch (e: IllegalArgumentException) {
            Log.e(ForgetMeNotActivity.LOG_TAG, "Time tick Receiver not registered", e)
        }
    }
    override fun onStop() { // when the app is closed
        super.onStop()

        // Save all data which you want to persist.
        val savedList = StringBuilder()
        for (task in taskList) {
            savedList.append(task)
            savedList.append(",")
        }

        getSharedPreferences(PREFS_TASKS, Context.MODE_PRIVATE).edit()
            .putString(KEY_TASKS_LIST, savedList.toString()).apply()
    }
    override fun onConfigurationChanged(newConfig: Configuration?) { // needed if wants an update when turning screen e.g
        super.onConfigurationChanged(newConfig)
    }


    fun addNote(view: View){ // linked to click on button to open new activity
        val thirdIntent = Intent(this, TaskDescriptionActivity::class.java)
        //startActivity(thirdIntent)
        startActivityForResult(thirdIntent, ADD_TASK_REQUEST) // imperative to call startActivityForResult with ADD_TASK_REQUEST in order to retrieve the data from the third screen
    }

    private fun makeAdapeter(list: List<String>): ArrayAdapter<String> =
        ArrayAdapter(this, android.R.layout.simple_list_item_1,list)

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { // retrieve the data from the task description activity
        Log.d("STATUS", "onActivityResult is called")
        // 1
        if (requestCode == ADD_TASK_REQUEST) {
            // 2
            if (resultCode == Activity.RESULT_OK) {
                // 3
                val task = data?.getStringExtra(TaskDescriptionActivity.EXTRA_TASK_DESCRIPTION)
                Log.d("TASK", task)
                task?.let {
                    taskList.add(task)
                    // 4
                    adapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun makeBroadcastReceiver(): BroadcastReceiver {
        return object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent?) {
                if (intent?.action == Intent.ACTION_TIME_TICK) {
                    dateTimeTextView.text = getCurrentTimeStamp()
                }
            }
        }
    }

    private fun taskSelected(position: Int) {
        // 1
        AlertDialog.Builder(this) // facilitates the creation of an AlertDialog
            // 2
            .setTitle(R.string.alert_title) // set title
            // 3
            .setMessage(taskList[position])
            .setPositiveButton(R.string.delete, { _, _ ->
                taskList.removeAt(position)
                adapter.notifyDataSetChanged()
            })
            .setNegativeButton(R.string.cancel, {
                    dialog, _ -> dialog.cancel()
            })
            // 4
            .create()
            // 5
            .show()
    }


}
