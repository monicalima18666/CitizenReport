package ipvc.estg.citizenreport.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ipvc.estg.citizenreport.R
import ipvc.estg.citizenreport.api.User



class UserAdapter(val users: List<User>): RecyclerView.Adapter<UsersViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsersViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview2, parent, false)
        return UsersViewHolder(view)
    }

    override fun getItemCount(): Int {
        return users.size
    }

    override fun onBindViewHolder(holder: UsersViewHolder, position: Int) {
        return holder.bind(users[position])
    }
}

class UsersViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
    private val username:TextView = itemView.findViewById(R.id.username)
    private val password:TextView = itemView.findViewById(R.id.password)

    fun bind(user: User) {
        username.text = user.username
        password.text = user.password

    }

}