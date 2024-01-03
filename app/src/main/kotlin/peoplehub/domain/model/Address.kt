package peoplehub.domain.model

data class Address(
    val street: String,
    val city: String,
    val state: String,
    val country: String,
    val postalCode: String
)
