package peoplehub.ui.addPerson

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import peoplehub.data.repository.PeopleRepository
import peoplehub.domain.model.Person

class AddPersonViewModel(private val peopleRepository: PeopleRepository) : ViewModel() {

    fun onSaveClick(person: Person) {
        viewModelScope.launch {
            peopleRepository.insertPerson(person)
        }
    }
}
