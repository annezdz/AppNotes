package com.example.applicationcontentprovider.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri

class NoteProvider : ContentProvider() {

    /*delete - deleta dados da nossa Provider
    * getType - serve para validar uma URL
    * insert - serve para inserir dados na aplicação atraves do Content Provider
    * onCreate - inicializa tudo no content provider. Aqui que fazemos as instânicas dos DBs, as instânicas das URLs
    * que serão possíveis de ser retornadas
    * query - serve para ser o select do Content Provider, e aqui que fazemos o processo
    * de selecionar arquivos que estão dentro do Content Provider, selecionar um banco de dados e
    * os dados de retorno dele e a query sempre retornará um cursor
    * update - é a atualização do ID do Content Provider
    *
    * */

    /*Para fazer a validacao de uma URL de requisição de um Content Provider */

    private lateinit var mUriMatcher: UriMatcher
    private lateinit var dbHelper : NotesDatabaseHelper

    override fun onCreate(): Boolean {
        mUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        mUriMatcher.addURI(AUTHORITY,"notes", NOTES)
        mUriMatcher.addURI(AUTHORITY, "notes/#",NOTES_BY_ID)
        if(context != null){

        }
        return true
        /*Aqui é onde define os enderecos e as identificações que o nosso Content Provider vai ter
        * O onCreate é um método de instancias, serve para decidir o que vamos trabalhar no nosso contect provider.*/
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }


    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        TODO("Implement this to handle query requests from clients.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }

    /* Sempre que criarmos uma Content Provider devemos criar uma constraint chamada authority.
    * O Authority define o endereço do Provider e toda a requisição que será feita chamará o Provider*/

    companion object{
        const val AUTHORITY ="com.example.applicationcontentrpovider.provider"
        val BASE_URI = Uri.parse("content://$AUTHORITY")
        /*Aqui estamos nomeando uma Url Notas, ou seja, content://com.example.applicationcontentrpovider.provider/notes
        * e ele é responsável por acessar todos os dados que chegam na nossa Content Provider*/

        val URI_NOTES = Uri.withAppendedPath(BASE_URI,"notes")
        const val NOTES = 1
        const val NOTES_BY_ID = 2
    }
}