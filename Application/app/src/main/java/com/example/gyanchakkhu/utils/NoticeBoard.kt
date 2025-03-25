import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gyanchakkhu.ui.theme.MyPurple100
import com.example.gyanchakkhu.ui.theme.MyRed80
import com.example.gyanchakkhu.ui.theme.poppinsFontFamily

@Composable
fun NoticeBoard(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(32.dp)
                .wrapContentSize(),
            colors = CardDefaults.cardColors(Color.White)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "20/03/2025\nNotice No. 001",
                        color = MyPurple100,
                        fontFamily = poppinsFontFamily,
                        lineHeight = 16.sp,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Notice Board",
                        tint = MyRed80,
                        modifier = Modifier
                            .clickable { onDismiss() }
                            .size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                LazyColumn {
                    item {
                        Text(
                            text = "Library Timings Updated – New operational hours starting from next month.",
                            color = MyPurple100,
                            fontFamily = poppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Text(
                            text = "Dear Members,\n" +
                                    "\n" +
                                    "This is to inform you that the operational hours of Gyanchakkhu Library will be revised starting from 31/03/25. Please take note of the updated schedule:\n\n" +
                                    "New Library Timings:\nMonday – Friday: 10:00AM - 06:00PM\nSaturday – Sunday: 10:00AM - 09:00PM\n" +
                                    "We request all members to plan their visits accordingly. For any queries, please contact the library help desk.\n" +
                                    "We appreciate your cooperation.\n" +
                                    "Regards,\n\n" +
                                    "Gyanchakkhu Library\n25/03/25",
                            color = MyPurple100,
                            fontFamily = poppinsFontFamily,
                            lineHeight = 16.sp,
                        )
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}