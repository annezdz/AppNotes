package com.example.applicationcontentprovider

import android.database.Cursor

/*Va ser responsável pelas funções de click dentro do nosso adapter, então a gente cria essa interface e implementa
* ela na Activity e levamos ela para o NotesAdapter*/

interface NoteClickedListener {
    fun noteClickedItem(cursor: Cursor)
    fun noteRemoveItem(cursor:Cursor?)
}