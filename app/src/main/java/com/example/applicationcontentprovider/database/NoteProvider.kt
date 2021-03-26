package com.example.applicationcontentprovider.database

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.UnsupportedSchemeException
import android.net.Uri
import android.provider.BaseColumns._ID
import android.provider.ContactsContract.Intents.Insert.NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TABLE_NOTES

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
        /*Utilizando o Cast (as) para evitar o double bang do Kotlin  pois se é
        * feita a verificação para ver se o context não é nulo, então ele realmente não é
        * nulo. Usando o cast ele não fica incomodando*/

        if(context != null){dbHelper = NotesDatabaseHelper(context as Context)

        }
        return true
        /*Aqui é onde define os enderecos e as identificações que o nosso Content Provider vai ter
        * O onCreate é um método de instancias, serve para decidir o que vamos trabalhar no nosso contect provider.*/
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        /* Antes de tudo, precisamos verificar a URL.
        * Criamos a variável db que vai permitir mexer no DB
        * dbHelper.writableDatabase permite que está habilitado para que consigamos mexer no SQLLite
        * a val linesAffect
        * uri.lastPathSegment será um elemento que será deletado, o elemento desse ID*/


        if(mUriMatcher.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val linesAffect = db.delete(TABLE_NOTES,"$_ID = ?", arrayOf(uri.lastPathSegment))
            db.close()

            /*Agora vamos manusear o Content Provider .
            * Aqui estamos notificando para o Content Provider tudo o que foi modificado no DB.
            * Para tudo o que acontece no Content Provider precisamos dar um notifyChange.
            * No final damos um return das linhas afetadas para posteriormente fazer os tratamentos daquilo
            * que foi deletado.  */

            context?.contentResolver?.notifyChange(uri,null)
            return linesAffect
        }else{
            throw UnsupportedSchemeException("URL inválida para exclusão")
        }
    }

    /*O etType só serve quando temos requisições de arquivos, como não vamos usar isso nesse exemplo
    * vamos apenas lançar um throw*/

    override fun getType(uri: Uri): String? = throw UnsupportedSchemeException("Uri não implementada")

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        if(mUriMatcher.match(uri) == NOTES){
            /*Aqui Uri.withAppendedPath(BASE_URI,id.toString()) estamos interagindo com o Content Provider*/
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val id = db.insert(TABLE_NOTES,null,values)
            val insertUri = Uri.withAppendedPath(BASE_URI,id.toString())
            db.close()
            //Aqui dizemos que houve registro novo inserido.
            context?.contentResolver?.notifyChange(uri,null)
            return insertUri
        }else{
            throw UnsupportedSchemeException("URL inválida para inserção.")
        }
    }


    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        /*Porque when? porque o select pode autorizar uma pesquisa  tanto por id ou elemento da coluna ou tabela
        * toda requisição feita no db.query vai para o cursor , que é basicamente o retorno de dados de
        * um Content Provider */
        return when {
            mUriMatcher.match(uri) == NOTES -> {
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor =
                    db.query(TABLE_NOTES,projection,selection,selectionArgs,null,null,sortOrder)
                cursor.setNotificationUri(context?.contentResolver,uri)
                cursor
            }
            mUriMatcher.match(uri) == NOTES_BY_ID ->{
                val db: SQLiteDatabase = dbHelper.writableDatabase
                val cursor =
                    db.query(TABLE_NOTES, projection,"$_ID = ?", arrayOf(uri.lastPathSegment),null,null,sortOrder)
                cursor.setNotificationUri((context as Context).contentResolver,uri)
                cursor
            }else ->{
                throw UnsupportedSchemeException("URL não implementada")

            }
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if(mUriMatcher.match(uri) == NOTES_BY_ID){
            val db: SQLiteDatabase = dbHelper.writableDatabase
            val linesAffect = db.update(TABLE_NOTES,values,"$_ID = ?", arrayOf(uri.lastPathSegment))
            db.close()
            context?.contentResolver?.notifyChange(uri,null)
            return linesAffect
        }else{
            throw UnsupportedSchemeException("URL não implementada")

        }
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