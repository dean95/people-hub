package peoplehub.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import peoplehub.domain.model.Person
import peoplehub.ui.addPerson.AddPersonScreen
import peoplehub.ui.addPerson.AddPersonViewModel
import peoplehub.ui.people.PeopleScreen
import peoplehub.ui.people.PeopleViewModel
import peoplehub.ui.personDetails.PersonDetailsScreen
import peoplehub.ui.personDetails.PersonDetailsViewModel

private const val Id = "id"
private const val People = "people"

private const val PeopleScreenRoute = People
private const val PersonDetailsRoute = "$People/{$Id}"
private const val AddPersonScreenRoute = "$People/add"

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PeopleScreenRoute
    ) {
        composable(route = PeopleScreenRoute) {
            val viewModel = koinViewModel<PeopleViewModel>()
            val state = viewModel.state.collectAsState(initial = emptyList())

            PeopleScreen(
                people = state.value,
                onPersonClick = {
                    navController.navigate("$People/$it")
                },
                onAddClick = {
                    navController.navigate(AddPersonScreenRoute)
                }
            )
        }

        composable(
            route = PersonDetailsRoute,
            arguments = listOf(navArgument(Id) { type = NavType.StringType })
        ) {
            val id = it.arguments?.getString(Id) ?: throw IllegalStateException("Id cannot be null")

            val viewModel = koinViewModel<PersonDetailsViewModel>(parameters = { parametersOf(id) })
            val state = viewModel.state.collectAsState(initial = Person("", "", ""))

            PersonDetailsScreen(
                person = state.value
            )
        }

        composable(route = AddPersonScreenRoute) {
            val viewModel = koinViewModel<AddPersonViewModel>()

            AddPersonScreen(
                onSaveClick = {
                    viewModel.onSaveClick(it)
                    navController.popBackStack()
                }
            )
        }
    }
}
