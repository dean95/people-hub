package peoplehub.ui.personDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import peoplehub.data.repository.PeopleRepository
import peoplehub.domain.model.Person

class PersonDetailsViewModel(
    personId: String,
    peopleRepository: PeopleRepository
) : ViewModel() {

    val state = peopleRepository.getPerson(personId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = Person("", "", "")
    )
}
