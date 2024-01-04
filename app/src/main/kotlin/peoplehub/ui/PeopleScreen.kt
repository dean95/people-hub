package peoplehub.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.peoplehub.R
import peoplehub.domain.model.Person
import peoplehub.ui.theme.Purple80
import peoplehub.ui.theme.Spacing

@Composable
fun PeopleScreen(
    people: List<Person>,
    onPersonClick: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        if (people.isEmpty()) {
            EmptyScreen(
                onAddClick = onAddClick,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = Spacing.Large,
                    vertical = Spacing.Medium
                ),
                verticalArrangement = Arrangement.spacedBy(Spacing.Medium)
            ) {
                items(people) {
                    PersonCard(
                        id = it.firstName,
                        name = "${it.firstName} ${it.lastName}",
                        onClick = onPersonClick
                    )
                }
            }

            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(Spacing.Medium)
                    .background(Purple80, CircleShape)
                    .padding(Spacing.Small)
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add",
                    tint = Color.Black
                )
            }
        }
    }
}

@Composable
private fun EmptyScreen(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier
) = Column(
    modifier = modifier,
    horizontalAlignment = Alignment.CenterHorizontally
) {
    Text(
        text = stringResource(id = R.string.people_screen_empty_title),
        color = Color.Black,
        style = MaterialTheme.typography.titleLarge
    )

    Spacer(modifier = Modifier.padding(Spacing.Medium))

    Button(onClick = onAddClick) {
        Text(text = stringResource(id = R.string.add_person))
    }
}

@Composable
private fun PersonCard(
    id: String,
    name: String,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier
) = Text(
    text = name,
    color = Color.Black,
    modifier = modifier
        .fillMaxWidth()
        .clip(RoundedCornerShape(Spacing.Medium))
        .clickable { onClick(id) }
        .background(Color.LightGray)
        .padding(horizontal = Spacing.Large, vertical = Spacing.Medium)
)
