package peoplehub.ui.personDetails

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.example.peoplehub.R
import peoplehub.domain.model.Person
import peoplehub.ui.theme.Spacing

@Composable
fun PersonDetailsScreen(
    person: Person,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.Large)
    ) {
        Text(
            text = stringResource(id = R.string.personal_info),
            style = MaterialTheme.typography.displaySmall
        )

        Spacer(modifier = Modifier.padding(bottom = Spacing.ExtraLarge))

        InfoRow(
            label = stringResource(id = R.string.first_name),
            value = person.firstName
        )

        Spacer(modifier = Modifier.padding(bottom = Spacing.Medium))

        InfoRow(
            label = stringResource(id = R.string.last_name),
            value = person.lastName
        )

        Spacer(modifier = Modifier.padding(bottom = Spacing.Medium))

        person.age?.let { age ->
            InfoRow(
                label = stringResource(id = R.string.age),
                value = age.toString()
            )

            Spacer(modifier = Modifier.padding(bottom = Spacing.Medium))
        }

        person.address?.let { address ->

            Spacer(modifier = Modifier.padding(bottom = Spacing.Medium))

            Text(
                text = stringResource(id = R.string.address),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.padding(bottom = Spacing.Medium))

            Text(
                text = address.street,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Spacing.Medium)
            )

            Text(
                text = address.city,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Spacing.Medium)
            )

            Text(
                text = address.state,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Spacing.Medium)
            )

            Text(
                text = address.country,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Spacing.Medium)
            )

            Text(
                text = address.postalCode,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = Spacing.Medium, bottom = Spacing.Medium)
            )

            Spacer(modifier = Modifier.padding(bottom = Spacing.Medium))
        }

        person.email?.let { email ->
            InfoRow(
                label = stringResource(id = R.string.email),
                value = email
            )
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge
        )
    }
}
