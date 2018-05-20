package com.example.aven.projekt2

import android.databinding.ObservableArrayList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import java.util.*

class ProjectActivity : AppCompatActivity() {
    private val listOfNeededBlocks: ObservableArrayList<Block> = ObservableArrayList()
    private var projectId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        projectId = intent.extras.getInt("newID")
    }
}
