package com.example.prak5

import android.database.sqlite.SQLiteDatabase

class DatabaseHelper(context: Context) : SQLiteOpenHelper(
    context, DATABASE_NAME, null, DATABASE_VERSION
) {
    // Implement your methods here
}


companion object {
}

private val DATABASE_NAME = "pizza"
private val DATABASE_VERSION = 1

// Table name
private val TABLE_ACCOUNT = "account"

// Columns of the account table
private val COLUMN_EMAIL = "email"
private val COLUMN_NAME = "name"
private val COLUMN_LEVEL = "level"
private val COLUMN_PASSWORD = "password"


// Create table account SQL query
private val CREATE_ACCOUNT_TABLE = (
        "CREATE TABLE " + TABLE_ACCOUNT + " (" +
                COLUMN_EMAIL + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_LEVEL + " TEXT, " +
                COLUMN_PASSWORD + " TEXT)"
        )

// Drop table account SQL query
private val DROP_ACCOUNT_TABLE = "DROP TABLE IF EXISTS $TABLE_ACCOUNT"


override fun onCreate(db: SQLiteDatabase?) {
    db?.execSQL(CREATE_ACCOUNT_TABLE)
}

override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    db?.execSQL(DROP_ACCOUNT_TABLE)
    onCreate(db)
}

// Login check
fun checkLogin(email: String, password: String): Boolean {
    val columns = arrayOf(COLUMN_NAME)
    val db = this.readableDatabase

    // Selection criteria
    val selection = "$COLUMN_EMAIL = ? AND $COLUMN_PASSWORD = ?"

    // Selection arguments
    val selectionArgs = arrayOf(email, password)

    val cursor = db.query(
        TABLE_ACCOUNT,    // Table to query
        columns,          // Columns to return
        selection,        // Columns for WHERE clause
        selectionArgs,    // The values for the WHERE clause
        null,             // Group the rows
        null,             // Filter by row groups
        null              // The sort order
    )

    val cursorCount = cursor.count
    cursor.close()
    db.close()

    // Check data available or not
    return cursorCount > 0
}

// Event button Login
btnLogin.setOnClickListener { view: View ->
    // Object class databaseHelper
    val databaseHelper = DatabaseHelper(context = this)
    val email = txtUsername.text.toString().trim()
    val password = txtPassword.text.toString().trim()

    // Check Login
    val result: Boolean = databaseHelper.checkLogin(email, password)
    if (result) {
        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_SHORT).show()
        val intentLogin = Intent(this@LoginActivity, MainActivity::class.java)
        startActivity(intentLogin)
    } else {
        Toast.makeText(this@LoginActivity, "Login Failed, Try Again !!!", Toast.LENGTH_SHORT).show()
    }
}

// Add User
fun addAccount(email: String, name: String, level: String, password: String) {
    val db = this.writableDatabase
    val values = ContentValues().apply {
        put(COLUMN_EMAIL, email)
        put(COLUMN_NAME, name)
        put(COLUMN_LEVEL, level)
        put(COLUMN_PASSWORD, password)
    }
    db.insert(TABLE_ACCOUNT, null, values)
    db.close()
}

fun checkData(email: String): String {
    val columns = arrayOf(COLUMN_NAME)
    val db = this.readableDatabase

    val selection = "$COLUMN_EMAIL = ?"
    val selectionArgs = arrayOf(email)
    var name: String = ""

    val cursor = db.query(
        TABLE_ACCOUNT,       // Table to query
        columns,             // Columns to return
        selection,           // Columns for WHERE clause
        selectionArgs,       // The values for the WHERE clause
        null,                // Group the rows
        null,                // Filter by row groups
        null                 // The sort order
    )

    if (cursor.moveToFirst()) {
        name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
    }

    cursor.close()
    db.close()

    return name
}

// Event button Login
btnLogin.setOnClickListener { view: View ->
    // Object class databaseHelper
    val databaseHelper = DatabaseHelper(context = this)

    // Check data
    val email = "stevi.ema@amikom.ac.id"
    val data: String = databaseHelper.checkData(email)
    Toast.makeText(this@LoginActivity, "Result: $data", Toast.LENGTH_SHORT).show()

    if (data.isEmpty()) {
        // Insert data
        databaseHelper.addAccount(
            email = email,
            name = "Stevi Ema W",
            level = "Cashier",
            password = "12345"
        )
    }
}
