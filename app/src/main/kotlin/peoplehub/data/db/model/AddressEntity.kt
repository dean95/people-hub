package peoplehub.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "address")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    @ColumnInfo(name = "postal_code") val postalCode: String
)
