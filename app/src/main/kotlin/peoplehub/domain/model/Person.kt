package peoplehub.domain.model

data class Person(
    val firstName: String,
    val lastName: String,
    val age: Int?,
    val address: Address?,
    val email: String?
)
