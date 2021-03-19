package ipvc.estg.citizenreport.adapters

import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.citizenreport.R
import ipvc.estg.citizenreport.dao.NotaDao
import ipvc.estg.citizenreport.entities.Nota
import ipvc.estg.citizenreport.update.UpdateNota
import kotlinx.android.synthetic.main.recyclerline.view.*

const val TITULO = "TITULO"
const val DESCRICAO = "DESCRICAO"


class NotaAdapter(context: Context, private val intID:EnviarInformacao
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>()  {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()


    interface EnviarInformacao {
        fun sendID(id: Int?)
    }


    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tituloItemView: TextView = itemView.findViewById(R.id.titulo)
        val descricaoItemView: TextView = itemView.findViewById(R.id.descricao)
        val delete : ImageButton = itemView.findViewById(R.id.delete)
        val edit : ImageButton = itemView.findViewById(R.id.edit)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotaViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.tituloItemView.text = current.titulo
        holder.descricaoItemView.text = current.descricao
        val id = current.id

        //chamar os botoes

        holder.edit.setOnClickListener{
            val context=holder.tituloItemView.context
            val titulo= holder.tituloItemView.text.toString()
            val descricao= holder.descricaoItemView.text.toString()

            val intent = Intent( context, UpdateNota::class.java).apply {
                putExtra(TITULO, titulo )
                putExtra(DESCRICAO, descricao )
                putExtra(Build.ID,id)
            }
            context.startActivity(intent)

        }

        holder.delete.setOnClickListener{
            val titulo = holder.tituloItemView.text.toString()
         // ENVIA O ID
           intID.sendID(id)
        }

    }

    internal fun setNotas (notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }


    override fun getItemCount():Int {

     return notas.size
   }


}


