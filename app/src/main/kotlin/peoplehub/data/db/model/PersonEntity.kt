package peoplehub.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import annotation.GenerateMapper

@Entity(tableName = "person")
@GenerateMapper(name = "DbMapper")
data class PersonEntity(
    @PrimaryKey val personId: String,
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    val age: Int?,
    @Embedded val address: AddressEntity?,
    val email: String?
)
