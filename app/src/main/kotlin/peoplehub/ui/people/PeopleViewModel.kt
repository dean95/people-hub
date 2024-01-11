package peoplehub.ui.people

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import peoplehub.data.repository.PeopleRepository

class PeopleViewModel(peopleRepository: PeopleRepository) : ViewModel() {

    val state = peopleRepository.getPeople().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
}
