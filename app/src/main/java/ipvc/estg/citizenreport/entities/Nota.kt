package ipvc.estg.citizenreport.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "nota_table")

class Nota (
        @PrimaryKey(autoGenerate = true) val id: Int? = null,
        @ColumnInfo(name = "titulo") val titulo: String,
        @ColumnInfo(name = "descricao") val descricao: String,

        )