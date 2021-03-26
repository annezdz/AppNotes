package com.example.applicationcontentprovider

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ClipDescription
import android.content.ContentValues
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.applicationcontentprovider.database.NoteProvider.Companion.URI_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES

class NotesDetailFragment : DialogFragment(), DialogInterface.OnClickListener{

    /*criar os lateinits dos nossos objetos criados no layout*/

    private lateinit var noteEditTitle: EditText
    private lateinit var noteEditDescription: EditText

    /*Recebendo o id do cursor do nosso Content Provider, lembrando que esse Dialog serve tanto para imputar dados
    quanto para receber atualizações daqueles dados que virão do nosso cursor*/

    private var id: Long = 0

    companion object{
        private const val EXTRA_ID = "id"
        fun newInstance(id:Long): NotesDetailFragment{
            //bundle recebe o id da activity
            val bundle = Bundle()
            bundle.putLong(EXTRA_ID,id)

            val notesFragment = NotesDetailFragment()
            notesFragment.arguments = bundle
            return notesFragment
        }
    }

    /*método estático para inicialização do nosso NotesDetailFragment, pois fica mais fácil de instanciar
    * ele na nossa Activity*/


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater?.inflate(R.layout.note_detail,null)
        noteEditTitle = view.findViewById(R.id.note_edt_title) as EditText
        noteEditDescription = view.findViewById(R.id.note_edt_description) as EditText
        
        var newNote = true

        if(arguments != null && arguments?.getLong(EXTRA_ID) != 0L){
            id = arguments?.getLong(EXTRA_ID) as Long
            //Vai montar a URI, a Content de Pesquisa.
            val uri = Uri.withAppendedPath(URI_NOTES, id.toString())
            //Aqui ele passa ela para o cursor. Lembrando que é um Fragment (pode acessar de qualquer canto)
            //ContentResolver é o objeto que se comunica com a nossa Content Provider
            //Aqui na query não vamos informar quase nada, praticamente vamos mandar o endereco para a pesquisa da activity

            val cursor =
                    activity?.contentResolver?.query(uri,null,null,null,null)
            if(cursor?.moveToNext() as Boolean)
            {
                //Aqui é false pois não estamos imputando dados, apenas repassando as informações do arguments
                newNote = false
                noteEditTitle.setText(cursor.getString(cursor.getColumnIndex(TITLE_NOTES)))
                noteEditDescription.setText(cursor.getString(cursor.getColumnIndex(DESCRIPTION_NOTES)))
            }
            cursor.close()
        }
        return AlertDialog.Builder(activity as Activity)
                .setTitle(if(newNote) "Nova Mensagem" else "Editar Mensagem")
                .setView(view)
                .setPositiveButton("Salvar", this)
                .setNegativeButton("Cancelar",this)
                .create()
    }

/*Agora vamos criar o input dos dados. Todos os dados que inputamos para dentro do Content Provider precisa ser
* do dado vindo da classe Content Values*/

    override fun onClick(dialog: DialogInterface?, which: Int) {
        val values = ContentValues()
        values.put(TITLE_NOTES,noteEditTitle.text.toString())
        values.put(DESCRIPTION_NOTES,noteEditDescription.text.toString())

        /*Aqui se o id passado já existe na base, ele apenas será alterado;
        * Caso não exista, será criado*/
        if(id != 0L){
            val uri = Uri.withAppendedPath(URI_NOTES,id.toString())
            context?.contentResolver?.update(uri,values,null,null)
        }else{
            context?.contentResolver?.insert(URI_NOTES,values)
        }
    }


}