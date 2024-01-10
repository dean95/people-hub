package peoplehub.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import annotation.GenerateMapper

@Entity(tableName = "address")
@GenerateMapper(name = "DbMapper")
data class AddressEntity(
    @PrimaryKey(autoGenerate = true) val addressId: Int = 0,
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    @ColumnInfo(name = "postal_code") val postalCode: String
)
