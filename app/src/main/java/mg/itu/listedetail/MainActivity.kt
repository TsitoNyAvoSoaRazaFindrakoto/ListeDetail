package mg.itu.listedetail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Mini-TP 5 — « Relier deux écrans »
 *
 * Les DEUX ÉCRANS sont fournis et fonctionnels :
 *   - EcranListe  : la liste des produits (LazyColumn)
 *   - EcranDetail : le détail d'un produit
 * ... mais ils ne sont PAS reliés : au lancement, seule la liste s'affiche,
 * et cliquer sur un produit ne fait rien.
 *
 * Votre travail : compléter la navigation — trois TODO dans AppNavigation().
 * Rien d'autre n'est à modifier.
 */

data class Produit(
    val id: Int,
    val nom: String,
    val origine: String,
    val prixKg: Double?,
)

val produits = listOf(
    Produit(1, "Vanille Bourbon", "Sambava", 250_000.0),
    Produit(2, "Café Arabica", "Itasy", 12_000.0),
    Produit(3, "Girofle", "Analanjirofo", 38_000.0),
    Produit(4, "Litchi", "Toamasina", null),
    Produit(5, "Poivre noir", "Vatovavy", 45_000.0),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

// ---------------------------------------------------------------------------
// LA NAVIGATION — c'est ici que tout se joue (3 TODO)
// ---------------------------------------------------------------------------

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = "liste") {

        composable("liste") {
            EcranListe(
                produits = produits,
                onProduitClick = { produitId ->
                    // TODO 2 : naviguer vers le détail du produit cliqué.
                    navController.navigate("detail/$produitId")
                }
            )
        }

        // TODO 1 : déclarer la route du détail, avec son argument produitId.
        // Modèle :
        composable("detail/{produitId}") { backStackEntry ->
            val id = backStackEntry.arguments
                ?.getString("produitId")?.toIntOrNull()
            val produit = produits.find { it.id == id }
            if (produit != null) {
                EcranDetail(
                    produit = produit,
                    onRetour = {
                        // TODO 3 : revenir à la liste (dépiler).
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}

// ---------------------------------------------------------------------------
// LES DEUX ÉCRANS — fournis, rien à modifier
// ---------------------------------------------------------------------------

@Composable
fun EcranListe(
    produits: List<Produit>,
    onProduitClick: (Int) -> Unit,
) {
    Column(Modifier.padding(16.dp)) {
        Text("Produits de la coopérative", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        LazyColumn {
            items(produits) { p ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onProduitClick(p.id) },
                ) {
                    Column(Modifier.padding(14.dp)) {
                        Text(p.nom, style = MaterialTheme.typography.titleMedium)
                        Text(
                            p.prixKg?.let { "${formatAriary(it)} / kg" } ?: "prix non fixé",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EcranDetail(
    produit: Produit,
    onRetour: () -> Unit,
) {
    Column(Modifier.padding(24.dp)) {
        Text(produit.nom, style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        Text("Origine : ${produit.origine}", style = MaterialTheme.typography.bodyLarge)
        Text(
            produit.prixKg?.let { "Prix : ${formatAriary(it)} / kg" } ?: "Prix non fixé",
            style = MaterialTheme.typography.bodyLarge,
        )
        Spacer(Modifier.height(24.dp))
        Button(onClick = onRetour) { Text("Retour à la liste") }
    }
}

/** Formate un montant en ariary : 1250000.0 -> "1 250 000 Ar". */
fun formatAriary(montant: Double): String {
    val entier = montant.toLong().toString()
    val groupes = entier.reversed().chunked(3).joinToString(" ").reversed()
    return "$groupes Ar"
}
