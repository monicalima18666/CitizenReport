package ipvc.estg.citizenreport.api

data class Reports (
        val id: Int,
        val latitude: Double,
        val longitude: Double,
        val descricao: String,
        val imagem: String,
        val users_id: Int,
        val titulo: String,
        val id_tipo: Int

        )
