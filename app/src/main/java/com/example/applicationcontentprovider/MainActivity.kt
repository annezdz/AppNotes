package com.example.applicationcontentprovider
/*In NotesAdapter created setCursor, changed NotesAdapter, created layout Note_Detail and Created
* NotesDetailFragment*/
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.BaseColumns._ID
import androidx.loader.app.LoaderManager
import androidx.loader.content.CursorLoader
import androidx.loader.content.Loader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationcontentprovider.database.NoteProvider.Companion.URI_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<Cursor>{

    lateinit var notesRecyclerView: RecyclerView
    lateinit var noteAdd: FloatingActionButton

    lateinit var adapter : NotesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        noteAdd = findViewById(R.id.note_add)
        noteAdd.setOnClickListener{}
        NotesDetailFragment().show(supportFragmentManager,"dialog")

        /*Implementando a interface e os seus métodos*/

        adapter = NotesAdapter(object : NoteClickedListener{
            override fun noteClickedItem(cursor: Cursor) {
                /*Pegamos o id do click do RV.
                //Precisa do supportFragment Manager para poder trazer o dialog manager aqui, para que possamos
                trabalhar com ele e esse supportfragmentmanager fica somente dentro da Activity, então é por isso
                que criamos essa interface e estamos utilizando ela assim.
                */
                val id:Long = cursor?.getLong(cursor.getColumnIndex(_ID))
                val fragment = NotesDetailFragment.newInstance(id)
                fragment.show(supportFragmentManager,"dialog")
            }

            override fun noteRemoveItem(cursor: Cursor?) {
                val id:Long? = cursor?.getLong(cursor.getColumnIndex(_ID))
                /*ContentResolver que é o objeto de comunicação com o ContentProvider e dar o delete
                * Aqui ja mostramos como interagi o Content Provider*/

                contentResolver.delete(Uri.withAppendedPath(URI_NOTES,id.toString()),null,null)
            }

        })
        adapter.setHasStableIds(true)
        notesRecyclerView = findViewById(R.id.notes_recycler)
        //criando o layoutManager e passando o nosso adapter para a nossa Recycler View
        notesRecyclerView.layoutManager = LinearLayoutManager(this)
        notesRecyclerView.adapter = adapter

        // Nesse caso o this é a Main Activity


        LoaderManager.getInstance(this).initLoader(0,null,this)
    }

    /*Serve para instanciar aquilo que será buscado.
    * No caso será a pesquisa no Content Provider*/
    /*Agora vamos iniciar esses métodos (onCreateLoader, onLoadFinished, onLoaderReset
    * e iniciar o background thread que é onde o provider content vai poder
    * rodar em background sem prejudicar a performance do aplicativo*/


    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Cursor> =
        CursorLoader(this,URI_NOTES, null,null,null,TITLE_NOTES)


    /* Pega os dados recebidos e manipulá-los da formas que desejarmos*/

    override fun onLoadFinished(loader: Loader<Cursor>, data: Cursor?) {
        if(data != null){adapter.setCursor(data)}
        }

    /*Serve para acabar com a pesquisa em segundo plano*/
    override fun onLoaderReset(loader: Loader<Cursor>) {
        adapter.setCursor(null)
    }
}