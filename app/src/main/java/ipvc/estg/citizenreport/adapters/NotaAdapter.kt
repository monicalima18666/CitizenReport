package ipvc.estg.citizenreport.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.citizenreport.R
import ipvc.estg.citizenreport.entities.Nota

class NotaAdapter(
        context: Context
) : RecyclerView.Adapter<NotaAdapter.NotaViewHolder>()  {


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var notas = emptyList<Nota>()

    class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val notaItemView: TextView = itemView.findViewById(R.id.textView)
       // val descricaoItemView: TextView = itemView.findViewById(R.id.descricao)
       // val delete : ImageButton = itemView.findViewById(R.id.delete)
    //val edit : ImageButton = itemView.findViewById(R.id.edit)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val itemView = inflater.inflate(R.layout.recyclerline, parent, false)
        return NotaViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        val current = notas[position]
        holder.notaItemView.text = current.id.toString() + " - " + current.titulo+ "-" + current.descricao
        //holder.descricaoItemView.text = current.descricao
        //chamar os botoes

    }

    internal fun setNotas (notas: List<Nota>) {
        this.notas = notas
        notifyDataSetChanged()
    }

    override fun getItemCount() = notas.size
}