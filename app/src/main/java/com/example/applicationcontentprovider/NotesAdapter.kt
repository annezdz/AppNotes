package com.example.applicationcontentprovider

import android.database.Cursor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.DESCRIPTION_NOTES
import com.example.applicationcontentprovider.database.NotesDatabaseHelper.Companion.TITLE_NOTES

class NotesAdapter(private val listener : NoteClickedListener): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    /*vamos instanciar uma variável do tipo cursor e será ela que irá armazenar os dados
    * que virão do Load Manager*/

    private var mCursor: Cursor? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder =
        /*Aqui está instanciado o layout de adapter*/
        NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.note_item, parent, false))

    /*Aqui se o mCursor é diferente de nullo ele faz a contagem, senão retorna 0*/

    override fun getItemCount(): Int = if(mCursor!= null) mCursor?.count as Int else 0

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        /*ele vai pegar na consulta aquela posição que foi pesquisada*/
        mCursor?.moveToPosition(position)
        holder.noteTitle.text = mCursor?.getString(mCursor?.getColumnIndex(TITLE_NOTES) as Int)
        holder.descriptionTitle.text = mCursor?.getString(mCursor?.getColumnIndex(DESCRIPTION_NOTES) as Int)
        /*Como pegamos o valor dentro do cursor. */
        /*a posição precisa ser pega novamente, não somente acima, para que o cursor não se perca, já que
        * seu resultado é externo*/
        holder.noteButtomRemove.setOnClickListener {
            mCursor?.moveToPosition(position) //evento removeItem e passamos uma notificação de mudanças
            listener.noteRemoveItem(mCursor)
            notifyDataSetChanged()
        }
        //
        holder.itemView.setOnClickListener{listener.noteClickedItem(mCursor as Cursor)}
    }

    /* O set cursor vai pegar o cursor que receber do metodo e atribuir para o método da variável cursor da data.
    *  */
    fun setCursor(newCursor:Cursor?){
        mCursor = newCursor
        notifyDataSetChanged()
}

/*Aqui vamos instanciar as variáveis do nosso layout */

class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val noteTitle = itemView.findViewById(R.id.note_title) as TextView
    val descriptionTitle = itemView.findViewById(R.id.note_description) as TextView
    val noteButtomRemove = itemView.findViewById(R.id.note_button_remove) as Button
}
}