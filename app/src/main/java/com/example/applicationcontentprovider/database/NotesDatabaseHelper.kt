package com.example.applicationcontentprovider.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns._ID

class NotesDatabaseHelper(context:Context): SQLiteOpenHelper(context,"databaseNotes",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        /*Criando o Script de criação do DB */
        db?.execSQL("CREATE_TABLE $TABLE_NOTES  ("+
                "$_ID INTEGER NOT NULL PRIMARY KEY, " +
        "        $TITLE_NOTES TEXT NOT NULL," +
                "$DESCRIPTION_NOTES TEXT NOT NULL")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    /*Mapeando o banco, criando um companion object e dentro dele criar constantes com
    * as tabelas e colunas do nosso DB*/

    companion object{
        const val TABLE_NOTES: String = "Notes"
        const val TITLE_NOTES: String = "title"
        const val DESCRIPTION_NOTES: String = "description"
    }
}