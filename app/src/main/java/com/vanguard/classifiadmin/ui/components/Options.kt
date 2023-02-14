package com.vanguard.classifiadmin.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanguard.classifiadmin.R
import com.vanguard.classifiadmin.router.Destinations

@Composable
fun StudentOptionsListItem(
    modifier: Modifier = Modifier,
    studentOption: StudentOption,
    onSelect: (StudentOption) -> Unit,
) {
    Surface(
        modifier = modifier
            .padding(8.dp)
            .clickable { onSelect(studentOption) }
            .clip(RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colors.primary,
        )
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = studentOption.icon),
                contentDescription = studentOption.label,
                tint = MaterialTheme.colors.primary,
                modifier = modifier
                    .size(24.dp)
                    .padding(2.dp)
            )

            Text(
                text = studentOption.label,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(8.dp)
            )
        }
    }
}


enum class StudentOption(val label: String, val icon: Int) {
    ViewProfile("View Profile", R.drawable.icon_portrait),
    ChangeClass("Change Class", R.drawable.icon_replace),
    RemoveFromClass("Remove from Class", R.drawable.icon_delete)
}
