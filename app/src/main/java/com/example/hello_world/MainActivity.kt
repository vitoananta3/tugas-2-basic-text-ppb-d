package com.example.hello_world

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hello_world.ui.theme.HelloworldTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HelloworldTheme {
                MyApp(modifier = Modifier.fillMaxSize())
            }
        }
    }
}

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val ingredients: String,
    val instructions: String
)

@Composable
fun MyApp(modifier: Modifier = Modifier) {
    var shouldShowOnboarding by rememberSaveable { mutableStateOf(true) }

    Surface(modifier) {
        if (shouldShowOnboarding) {
            OnboardingScreen(onContinueClicked = { shouldShowOnboarding = false })
        } else {
            RecipeList(onBackClicked = { shouldShowOnboarding = true })
        }
    }
}

@Composable
fun OnboardingScreen(
    onContinueClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome to Recipe World!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Discover delicious recipes for every occasion",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(
            modifier = Modifier.padding(vertical = 24.dp),
            onClick = onContinueClicked
        ) {
            Text("Explore Recipes")
        }
    }
}

@Composable
fun RecipeList(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit
) {
    val recipes = remember {
        List(13) { index ->
            Recipe(
                id = index,
                name = "Recipe ${index + 1}",
                description = "A delicious recipe for all occasions.",
                ingredients = "• ${(index % 5 + 2)} cups flour\n• ${index % 3 + 1} eggs\n• ${index % 2 + 1} cup sugar\n• 1 tsp vanilla extract\n• ${index % 4 + 1} cup milk",
                instructions = "1. Preheat oven to ${320 + index * 5}°F\n2. Mix all ingredients in a bowl\n3. Pour into a baking dish\n4. Bake for ${20 + index % 10} minutes\n5. Let cool and enjoy!"
            )
        }
    }

    val scrollState = rememberLazyListState()

    LazyColumn(
        state = scrollState,
        modifier = modifier.padding(vertical = 4.dp)
    ) {
        items(items = recipes) { recipe ->
            RecipeCard(recipe = recipe)
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(onClick = onBackClicked) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Go back",
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text("Back to Welcome")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun RecipeCard(recipe: Recipe, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        modifier = modifier.padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        RecipeCardContent(recipe)
    }
}

@Composable
fun RecipeCardContent(recipe: Recipe) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .padding(12.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(12.dp)
            ) {
                Text(
                    text = recipe.name,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = recipe.description,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            IconButton(onClick = { expanded = !expanded }) {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
                    contentDescription = if (expanded) "Show less" else "Show more"
                )
            }
        }

        if (expanded) {
            Column(
                modifier = Modifier.padding(
                    start = 12.dp,
                    end = 12.dp,
                    bottom = 12.dp
                )
            ) {
                Text(
                    text = "Ingredients:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                Text(
                    text = recipe.ingredients,
                    style = MaterialTheme.typography.bodyMedium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Instructions:",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 4.dp, top = 8.dp)
                )
                Text(
                    text = recipe.instructions,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 320)
@Composable
fun OnboardingPreview() {
    HelloworldTheme {
        OnboardingScreen(onContinueClicked = {})
    }
}

@Preview(
    showBackground = true,
    widthDp = 320,
    uiMode = UI_MODE_NIGHT_YES,
    name = "RecipeCardPreviewDark"
)
@Preview(showBackground = true, widthDp = 320)
@Composable
fun RecipeCardPreview() {
    HelloworldTheme {
        RecipeCard(
            recipe = Recipe(
                id = 1,
                name = "Chocolate Cake",
                description = "Delicious dessert for chocolate lovers",
                ingredients = "• 2 cups flour\n• 2 eggs\n• 1 cup sugar\n• 1 tsp vanilla extract\n• 1 cup milk",
                instructions = "1. Preheat oven to 350°F\n2. Mix all ingredients in a bowl\n3. Pour into a baking dish\n4. Bake for 30 minutes\n5. Let cool and enjoy!"
            )
        )
    }
}

@Preview
@Composable
fun MyAppPreview() {
    HelloworldTheme {
        MyApp(Modifier.fillMaxSize())
    }
}