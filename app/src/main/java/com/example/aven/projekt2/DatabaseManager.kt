package com.example.aven.projekt2

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.databinding.ObservableArrayList
import java.io.FileOutputStream
import java.io.IOException


/**
 * Created by Aven on 2018-05-14.
 */
class DatabaseManager: SQLiteOpenHelper {

    private val DB_PATH = "/data/data/com.example.aven.projekt2/databases/"

    private val DB_NAME = "spells.db"

    private var myDataBase: SQLiteDatabase? = null

    private val myContext: Context


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    constructor(context: Context):super(context, "spells.db", null, 4){
        this.myContext = context
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     */
    @Throws(IOException::class)
    fun createDataBase() {

        val dbExist = checkDataBase()

        if (dbExist) {
            //do nothing - database already exist
        } else {

            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.readableDatabase

            try {

                copyDataBase()

            } catch (e: IOException) {

                throw Error("Error copying database")

            }

        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private fun checkDataBase(): Boolean {

        var checkDB: SQLiteDatabase? = null

        try {
            val myPath = DB_PATH + DB_NAME
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY)

        } catch (e: SQLiteException) {

            //database does't exist yet.

        }

        if (checkDB != null) {

            checkDB.close()

        }

        return if (checkDB != null) true else false
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    @Throws(IOException::class)
    private fun copyDataBase() {

        //Open your local db as the input stream
        val myInput = myContext.assets.open(DB_NAME)

        // Path to the just created empty db
        val outFileName = DB_PATH + DB_NAME

        //Open the empty db as the output stream
        val myOutput = FileOutputStream(outFileName)

        //transfer bytes from the inputfile to the outputfile
        val buffer = ByteArray(1024)
        var length = myInput.read(buffer)
        while (length > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }

        //Close the streams
        myOutput.flush()
        myOutput.close()
        myInput.close()

    }

    @Throws(SQLException::class)
    fun openDataBase() {

        //Open the database
        val myPath = DB_PATH + DB_NAME
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE)

    }

    @Synchronized override fun close() {

        if (myDataBase != null)
            myDataBase!!.close()

        super.close()

    }

    override fun onCreate(db: SQLiteDatabase) {

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if(newVersion>oldVersion){
            copyDataBase()
        }
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    fun fetchProjects(): ObservableArrayList<Project>{
        val db = this.readableDatabase
        val cursor2 = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name NOT LIKE 'sqlite_%'", null)
        if(cursor2.moveToFirst()) {
            var x = cursor2.getString(0)
        }
        while(cursor2.moveToNext()){
            var x = cursor2.getString(0)
        }
        cursor2.close()
        val query = "SELECT * FROM Spellbooks"
        val cursor = db.rawQuery(query, null)
        var tempList = ObservableArrayList<Project>()
        if(cursor.moveToFirst()) {
            tempList.add(Project(cursor.getString(1), cursor.getInt(0)))
        }
        while(cursor.moveToNext()){
            tempList.add(Project(cursor.getString(1), cursor.getInt(0)))
        }
        cursor.close()
        return tempList
    }

    fun fetchSpell(itemId: Int): Block?{
        val query = "SELECT Name, Ing, Plevel, Ctime, Dtime, Opis, Tra, Id FROM spells WHERE Id=$itemId"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var block: Block? = null
        if(cursor.moveToFirst()){
            block = Block(cursor.getString(0), cursor.getString(5), cursor.getString(1), cursor.getString(6), cursor.getString(3), cursor.getString(4), cursor.getInt(2), cursor.getInt(7))
        }
        cursor.close()
        return block
    }

    fun addProject(name: String): Int{
        val query = "SELECT MAX(Id) FROM spellbooks"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var id = 0
        if(cursor.moveToFirst()) {
            id = cursor.getInt(0)
        }
        saveProject(Project(name, id))

        return id
    }

    fun saveProject(project: Project){
        val db = this.writableDatabase
        var values = ContentValues()
        db.beginTransaction()
        values.put("Id", project.projectId)
        values.put("Name", project.name)

        db.delete("spellbooks", "Id=${project.projectId}", null)

        db.insert("spellbooks", null, values)

        db.delete("spells_spellbooks", "Id_spellbook=${project.projectId}", null)

        for(block in project.listOfNeededBlocks){
            values = ContentValues()
            values.put("Id_spell", block.id)
            values.put("Id_spellbook", project.projectId)
            db.insert("spells_spellbooks", null, values)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun getAllBlocks(): ObservableArrayList<MiniBlock> {
        val query = "SELECT Name, Id FROM spells"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var spells = ObservableArrayList<MiniBlock>()
        if(cursor.moveToFirst()){
            spells.add(MiniBlock(cursor.getInt(1), cursor.getString(0)))
        }
        while (cursor.moveToNext()){
            spells.add(MiniBlock(cursor.getInt(1), cursor.getString(0)))
        }
        cursor.close()
        return spells
    }

    fun loadProject(id: Int): Project{
        val db = this.readableDatabase
        val queryItems = "SELECT Id_spell FROM spells_spellbooks WHERE Id_spellbook=$id"
        var cursor = db.rawQuery(queryItems, null)
        var blocks = ObservableArrayList<Block>()

        if(cursor.moveToFirst()) {
            blocks.add(fetchSpell(cursor.getInt(0)))
        }
        while(cursor.moveToNext()){
            blocks.add(fetchSpell(cursor.getInt(0)))
        }
        cursor.close()

        val queryProject = "SELECT * FROM spellbooks WHERE Id=$id"
        cursor = db.rawQuery(queryProject, null)
        cursor.moveToFirst()
        var loadedProject = Project(cursor.getString(1), cursor.getInt(0))
        loadedProject.listOfNeededBlocks = blocks

        cursor.close()
        return loadedProject
    }
}