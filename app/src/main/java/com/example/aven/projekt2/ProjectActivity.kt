package com.example.aven.projekt2

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.support.v7.widget.Toolbar
import kotlinx.android.synthetic.main.activity_project.*

class ProjectActivity() : AppCompatActivity(), ExportDialog.OnExportDialogInputListener {
    var currentProject: Project? = null

    var adapter: BlockListAdapter? = null

    val db = DatabaseManager(this)

    var mToolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project)
        val extras = intent.extras ?: return
        var id = extras.getInt("projectId")

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
        mToolbar = findViewById<View>(R.id.toolbar) as Toolbar

        mToolbar?.setNavigationOnClickListener {
            finish()
        }

        currentProject = DatabaseManager(this).loadProject(id)

        adapter = BlockListAdapter(currentProject!!.listOfNeededBlocks, this)

        projectElementsList.adapter=adapter

        projectElementsList.setOnItemLongClickListener { adapterView, view, i, l ->
            currentProject!!.removeBlockAt(i)
            adapter?.notifyDataSetChanged()
            true
        }

        fabExport.setOnClickListener{
            var dialog = ExportDialog()
            dialog.adapter = MiniBlockListAdapter(db.getAllBlocks(), this)
            dialog.show(fragmentManager, "ExportDialog")
        }
    }

    override fun onInput(nameInput: String) {
        currentProject!!.addBlock(db.fetchSpell(nameInput.toInt())?: Block())
        adapter!!.notifyDataSetChanged()
    }

    override fun finish() {
        db.saveProject(currentProject!!)
        super.finish()
    }

    override fun onPause() {
        db.saveProject(currentProject!!)
        super.onPause()
    }

    override fun onDestroy() {
        db.saveProject(currentProject!!)
        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, mToolbar?.menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.getItemId()==R.id.settings){
            fragmentManager.beginTransaction().replace(android.R.id.content, SettingsFragment()).commit()

            return true
        }else{
            return false
        }
    }
}
