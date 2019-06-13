package com.example.aven.projekt2

import android.content.Intent
import android.databinding.ObservableArrayList
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import android.os.StrictMode
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.aven.projekt2.NewProjectDialog.OnDialogInputListener
import java.util.*


class MainActivity : AppCompatActivity(), OnDialogInputListener {

    override fun onInput(nameInput: String) {
        var newProjectId = db.addProject(nameInput)
        openProject(newProjectId)
    }

    var projectList: ObservableArrayList<Project> = ObservableArrayList<Project>()
    var adapter: ProjectListAdapter? = null
    val db: DatabaseManager = DatabaseManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PreferenceManager.setDefaultValues(this, R.xml.preference_xml, false)

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }
        try{
            db.createDataBase()
        }catch (e: Exception){
            e.printStackTrace()
        }
        try{
            db.openDataBase()
        }catch (e: Exception){
            e.printStackTrace()
        }

        loadProjects()

        adapter = ProjectListAdapter(projectList, this)

        listView.adapter=adapter

        listView.setOnItemClickListener { parent, view, i, l ->
            var item = listView.getItemAtPosition(i) as Project
            openProject(item.projectId)
        }

        fab.setOnClickListener{
            var dialog = NewProjectDialog()
            dialog.show(fragmentManager, "CreationDialog")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.getItemId()==R.id.settings){
            fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).addToBackStack("settings").commit()

            return true
        }else{
            return false
        }
    }

    override fun onResume() {
        loadProjects()
        super.onResume()
    }

    override fun onRestart() {
        loadProjects()
        super.onRestart()
    }


    fun loadProjects(){
        var tempList = db.fetchProjects()
        Log.e("PROJECT NO", "${tempList.count()}")
        projectList.clear()
        projectList.addAll(tempList)
    }

    fun openProject(id: Int){
        var intent = Intent(this, ProjectActivity::class.java)
        intent.putExtra("projectId", id)
        startActivity(intent)
    }

}
