package peoplehub.domain.model

data class Person(
    val personId: Int,
    val firstName: String,
    val lastName: String,
    val age: Int? = null,
    val address: Address? = null,
    val email: String? = null
)
