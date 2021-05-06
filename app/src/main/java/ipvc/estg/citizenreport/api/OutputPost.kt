package ipvc.estg.citizenreport.api

data class OutputPost(
        val status: Boolean,
        val MSG: String,
        val username: String,
        val id: Int,
)

// usado para o login


data class OutputReports(
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val descricao: String,
        val imagem: String,
        val users_id: Int,
        val titulo: String,
        val id_tipo: Int
)

data class OutputEliminar(
        val mensagem: String
)

data class OutputInsert(
        val status: Boolean,
        val MSG: String,

)