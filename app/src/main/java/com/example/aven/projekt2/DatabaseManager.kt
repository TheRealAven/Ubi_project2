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

    private val DB_NAME = "BrickListDB.db"

    private var myDataBase: SQLiteDatabase? = null

    private val myContext: Context


    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    constructor(context: Context):super(context, "BrickListDB.db", null, 4){
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

    fun fetchName(code: String): String{
        val query = "SELECT NAME FROM Parts WHERE CODE LIKE \"$code\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var name: String = ""
        if(cursor.moveToFirst()){
            name = cursor.getString(0)
        }
        cursor.close()
        return name
    }

    fun fetchColorName(colorId: Int): String{
        val query = "SELECT NAME FROM Colors WHERE CODE=$colorId"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var colorName: String = ""
        if(cursor.moveToFirst()){
            colorName = cursor.getString(0)
        }
        cursor.close()
        return colorName
    }

    fun fetchTypeName(code: String): String{
        val query = "SELECT NAME FROM ItemTypes WHERE CODE LIKE \"$code\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var typeName: String = ""
        if(cursor.moveToFirst()){
            typeName = cursor.getString(0)
        }
        cursor.close()
        return typeName
    }

    fun fetchProjects(): ObservableArrayList<Project>{
        val query = "SELECT _id, Name, Active FROM Inventories"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var tempList = ObservableArrayList<Project>()
        if(cursor.moveToFirst()) {
            tempList.add(Project(cursor.getString(1), cursor.getInt(0), cursor.getInt(2)))
        }
        while(cursor.moveToNext()){
            tempList.add(Project(cursor.getString(1), cursor.getInt(0), cursor.getInt(2)))
        }
        cursor.close()
        return tempList
    }

    fun fetchCode(itemId: Int, colorId: Int): String{
        val query = "SELECT Code FROM Codes WHERE ItemID=$itemId AND ColorID=$colorId"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var code: String = ""
        if(cursor.moveToFirst()){
            code = cursor.getString(0)
        }
        cursor.close()
        return code
    }

    fun saveProject(project: Project){
        val db = this.writableDatabase
        var values = ContentValues()
        db.beginTransaction()
        values.put("_id", project.projectId)
        values.put("Name", project.name)
        if(project.active)
            values.put("Active", 1)
        else
            values.put("Active", 0)
        values.put("LastAccessed", 0)

        db.delete("Inventories", "_id=${project.projectId}", null)

        db.insert("Inventories", null, values)

        db.delete("InventoriesParts", "InventoryID=${project.projectId}", null)

        for(block in project.listOfNeededBlocks){
            values = ContentValues()
            values.put("_id", getId("InventoriesParts") )
            values.put("InventoryID", project.projectId)
            values.put("TypeID", block.blockTypeCode)
            values.put("ItemID", block.blockIdCode)
            values.put("QuantityInSet", block.maxNumber)
            values.put("QuantityInStore", block.actualNumber)
            values.put("ColorID", block.colorCode)
            values.put("Extra", 0)
            db.insert("InventoriesParts", null, values)
        }
        db.setTransactionSuccessful()
        db.endTransaction()
    }

    fun getREALBlockId(fakeId: String): Int{
        val query = "SELECT _id FROM Parts WHERE Code LIKE \"$fakeId\""
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        return if(cursor.moveToFirst()) cursor.getInt(0) else -1
    }

    fun getId(whichTable: String): Int{
        val query = "SELECT max(_id) FROM $whichTable;"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)
        var lastId = 0
        if (cursor.moveToFirst()) {
            lastId = cursor.getInt(0)
        }
        cursor.close()
        return lastId+1
    }

    fun addProjectFromUrl(name: String, url: String, c: Context): Int{
        val newProject = Project(name, getId("Inventories"), 1)
        newProject.makeProjectFromLink(url, c)

        saveProject(newProject)

        return newProject.projectId
    }

    fun loadProject(id: Int): Project{
        val db = this.readableDatabase
        val queryItems = "SELECT * FROM InventoriesParts WHERE InventoryID=$id"
        var cursor = db.rawQuery(queryItems, null)
        var blocks = ObservableArrayList<Block>()

        cursor.moveToFirst()
        var blockId = getREALBlockId(cursor.getString(3))
        blocks.add(Block(fetchName(cursor.getString(3)), fetchTypeName(cursor.getString(2)), cursor.getInt(6), cursor.getString(3), cursor.getString(2), cursor.getInt(5), cursor.getInt(4), fetchColorName(cursor.getInt(6)),fetchCode(blockId, cursor.getInt(6))))
        while(cursor.moveToNext()){
            blockId = getREALBlockId(cursor.getString(3))
            blocks.add(Block(fetchName(cursor.getString(3)), fetchTypeName(cursor.getString(2)), cursor.getInt(6), cursor.getString(3), cursor.getString(2), cursor.getInt(5), cursor.getInt(4), fetchColorName(cursor.getInt(6)),fetchCode(blockId, cursor.getInt(6))))
        }
        cursor.close()

        val queryProject = "SELECT * FROM Inventories WHERE _id=$id"
        cursor = db.rawQuery(queryProject, null)
        cursor.moveToFirst()
        var loadedProject = Project(cursor.getString(1), cursor.getInt(0), cursor.getInt(2))
        loadedProject.listOfNeededBlocks = blocks

        cursor.close()
        return loadedProject
    }
}