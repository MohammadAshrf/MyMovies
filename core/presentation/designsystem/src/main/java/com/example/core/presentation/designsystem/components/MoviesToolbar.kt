@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.core.presentation.designsystem.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.ArrowLeftIcon
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.designsystem.Poppins
import com.example.core.presentation.designsystem.SearchIcon
import com.example.core.presentation.designsystem.toolbar.ToolbarNavIcon

@Composable
fun MoviesToolbar(
    title: String,
    modifier: Modifier = Modifier,
    navIcons: Set<ToolbarNavIcon> = emptySet(),
    onBackClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null
) {
    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),

        navigationIcon = {
            if (ToolbarNavIcon.BACK in navIcons) {
                CircleIconButton(
                    icon = ArrowLeftIcon,
                    modifier = Modifier.padding(4.dp),
                    onClick = onBackClick
                )
            }
        },

        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                startContent?.invoke()
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        },

        actions = {
            if (ToolbarNavIcon.SEARCH in navIcons) {
                CircleIconButton(
                    icon = SearchIcon,
                    onClick = onSearchClick
                )
            }
        }
    )
}


@PreviewLightDark
@Composable
private fun MoviesToolbarPreview() {
    MyMoviesTheme {
        MoviesToolbar(
            title = "Movies",
            navIcons = setOf(
                ToolbarNavIcon.BACK,
                ToolbarNavIcon.SEARCH
            ),
            startContent = {
                Icon(
                    imageVector = LogoIcon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(32.dp)
                )
            }
        )
    }
}