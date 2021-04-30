package ipvc.estg.citizenreport.api

data class OutputPost(
        val status: Boolean,
        val MSG: String,
        val username: String,
        val id: Int,
)