@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.core.presentation.designsystem.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.ArrowLeftIcon
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.MyMoviesTheme
import com.example.core.presentation.designsystem.Poppins
import com.example.core.presentation.designsystem.R
import com.example.core.presentation.designsystem.SearchIcon
import com.example.core.presentation.designsystem.toolbar.ToolbarNavIcon

@Composable
fun MoviesToolbar(
    title: String,
    modifier: Modifier = Modifier,
    isSearchActive: Boolean = false,
    searchQuery: String = "",
    onSearchQueryChange: (String) -> Unit = {},
    onToggleSearch: () -> Unit = {},
    navIcons: Set<ToolbarNavIcon> = emptySet(),
    onBackClick: () -> Unit = {},
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
    startContent: (@Composable () -> Unit)? = null
) {
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isSearchActive) {
        if (isSearchActive) {
            focusRequester.requestFocus()
            keyboardController?.show()
        } else {
            keyboardController?.hide()
        }
    }

    TopAppBar(
        modifier = modifier,
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),

        navigationIcon = {
            if (ToolbarNavIcon.BACK in navIcons && !isSearchActive) {
                CircleIconButton(
                    icon = ArrowLeftIcon,
                    modifier = Modifier.padding(start = 8.dp),
                    onClick = onBackClick
                )
            }
        },

        title = {
            AnimatedContent(
                targetState = isSearchActive,
                transitionSpec = {
                    if (targetState) {
                        (fadeIn() + slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) { it / 2 })
                            .togetherWith(
                                fadeOut() + slideOutHorizontally { -it / 2 }
                            )
                    } else {
                        (fadeIn() + slideInHorizontally(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        ) { -it / 2 })
                            .togetherWith(
                                fadeOut() + slideOutHorizontally { it / 2 }
                            )
                    }
                },
                label = stringResource(R.string.toolbartitle)
            ) { active ->
                if (active) {
                    TextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        placeholder = {
                            Text(
                                text = stringResource(R.string.search_movies),
                                fontFamily = Poppins,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        },
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = MaterialTheme.colorScheme.primary
                        ),
                        textStyle = MaterialTheme.typography.titleLarge.copy(fontFamily = Poppins),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() })
                    )
                } else {
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
                }
            }
        },

        actions = {
            if (ToolbarNavIcon.SEARCH in navIcons) {
                CircleIconButton(
                    icon = if (isSearchActive) Icons.Rounded.Close else SearchIcon,
                    modifier = Modifier.padding(8.dp),
                    onClick = onToggleSearch
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
            isSearchActive = false,
            searchQuery = "",
            onSearchQueryChange = {},
            onToggleSearch = {},
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