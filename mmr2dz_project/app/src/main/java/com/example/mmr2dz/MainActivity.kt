package com.example.mmr2dz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlin.random.Random

data class ImgItem(
    val uid: Int,
    val name: String,
    val src: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MmrTheme {
                GalleryHost()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryHost() {
    var query by remember { mutableStateOf("") }
    var useGrid by remember { mutableStateOf(false) }
    var images by remember { mutableStateOf(sampleImgs()) }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = query,
                    onValueChange = { query = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = { Text("Поиск по автору...") },
                    singleLine = true
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { useGrid = !useGrid }) {
                        Icon(
                            imageVector = if (useGrid) Icons.Default.List else Icons.Default.Menu,
                            contentDescription = "Переключить вид"
                        )
                    }
                    IconButton(onClick = { images = emptyList() }) {
                        Icon(Icons.Default.Clear, contentDescription = "Очистить всё")
                    }
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val next = randomNewImg()
                if (images.none { it.uid == next.uid || it.src == next.src }) {
                    images = images + next
                }
            }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }
        }
    ) { paddingValues ->
        val shown = images.filter {
            it.name.contains(query, ignoreCase = true)
        }

        if (useGrid) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(shown) { img ->
                    ImgCard(item = img, onClick = { images = images - img })
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(paddingValues),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(shown) { img ->
                    ImgCard(item = img, onClick = { images = images - img })
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ImgCard(item: ImgItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column {
            GlideImage(
                model = item.src,
                contentDescription = "Картинка от ${item.name}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Text(
                text = item.name,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

private fun sampleImgs(): List<ImgItem> = listOf(
    ImgItem(1, "Александр Шляпик", "https://yandex-images.clstorage.net/iNMW98357/dc3a69BPwK/jS3FcaWGjxcSrrkC5T_XSD0N2kqc9k-Df_l1rz74L6HM3oP6thM_CfFoxxE4QSBfuQrwvU4p1aC4G0UqN0i2Ds940D3mxZlLRO-vQ6k43kwiRscCnLFT0XTubv1q6yX_l47Sx4s2SHOaEKE7SWlcEbwH9e1UYZex1H1SAk98ugaVIut1gqLOLwKc-RrtOPdILFvxbrm8Q9BG0AHJScx5_dnQkfqAHEN0ijiwIV1ifiS1CPvcTiy1se5OOAlLRaU-ix3VfJmS7DMsh_Yx4zXwWwNX8Huun1PVcNYY-0PibP_5-5XTuRV9RIEPpwptWl06iS_721kWvb3JbBw5UUCRMqsP6XWOluAoP43tPcg69XUqet55m7dL6V7cEdVCz03S0-6o0505QnuiHoIZWGdZI7k81M5NDqu02GkBLXttvBuVHdxen5jiOAK1-DL8GsdnJGD-XpuNcsRz-Af3RM9R3c3UtvabDV5dpxSyPmpvdR2fIMLtSAudj8lYJy1qeYMdnSrkRY6a9xcdjvAJ7yv2bjBn-WyJqE7Ufssx6kXaWPbk1qbvoxx-Q4kdtD5dTn05qQrX-3k8j6LaZRUCUlOLOJY1-lSThNk8DK__E9wawX8fc9JOqoh_yE7yOv5d2kvE7uix65A8TGaRJYc0Ym1-Iroh2OdON7Gc4VQ6AnZOvyOOLfh9n7fLDjW88QLPG_JiLmDRQqeQaN918i7SQOpE4c33pNusEnxakxqXEW94Qj6NEN_bZia3kdpbERlRXbAfqg_5Qqe62Ao0i_QK9DLwTy17_Fyxt1Hue9YNzEzXd9PG2qDCpDpuaJEVkgVBbXgllyfC-kAgpIXtQTQtVW-YArQI-EGnhvIfD7LGPMAp1GccV9dhn75IzmfeCvVr0X3j38qD4Zw7fn-KLbE5QkZKGYUk4dpoDZCrz1MlPkRBvyejINNqtL7nDiaH3A7eGvFoP27xWJKWX_hO8x3zafE"),
    ImgItem(2, "Иван Золочевский", "https://avatars.mds.yandex.net/i?id=dd1d7a087bdd292495d8b11e583e4f91_l-5224299-images-thumbs&n=13"),
    ImgItem(3, "Сергей Почкин", "https://avatars.mds.yandex.net/i?id=0dab1a566bab28eb1a0205366e44051c_l-5658951-images-thumbs&n=13"),
    ImgItem(4, "Тимофей Белый", "https://i.ytimg.com/vi/0_sYqHzmqhg/maxresdefault.jpg"),
    ImgItem(5, "Руслан Бомбовский", "https://avatars.mds.yandex.net/i?id=22e202105529d6c0727b89fe84d1f445_l-5340221-images-thumbs&n=13")
)

private fun randomNewImg(): ImgItem {
    val id = Random.nextInt(6, 1000)
    val names = listOf(
        "Александр Шляпик",
        "Иван Золочевский",
        "Сергей Почкин",
        "Тимофей Белый",
        "Руслан Бомбовский"
    )
    return ImgItem(id, names.random(), "https://picsum.photos/seed/$id/400")
}
