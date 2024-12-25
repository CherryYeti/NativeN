import android.content.Context
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ramcosta.composedestinations.generated.destinations.DoujinScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
fun DoujinComponent(
    context: Context,
    imageUrl: String,
    title: String,
    width: Int,
    height: Int,
    id:Int,
    destinationsNavigator: DestinationsNavigator
) {
    val painter = rememberAsyncImagePainter(imageUrl)
    val painterState by painter.state.collectAsState()

    Box(
        modifier = Modifier
            .padding(16.dp)
            .clip(MaterialTheme.shapes.medium)
    ) {

        if (painterState is coil3.compose.AsyncImagePainter.State.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(width.toFloat() / height)
                    .align(Alignment.Center)
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(width.toFloat() / height.toFloat())
                    .clip(RoundedCornerShape(8.dp)) // Rounded corners
                .clickable(onClick = {
                    destinationsNavigator.navigate(DoujinScreenDestination(id = id))
                })
            )
            Text(
                text = title,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp)
            )
//        Box(modifier = Modifier
//            .fillMaxWidth()
//            .align(Alignment.TopStart)
//            .background(Color.Black.copy(alpha = 0.7F))
//        ) {
//
//        }
        }

    }
}