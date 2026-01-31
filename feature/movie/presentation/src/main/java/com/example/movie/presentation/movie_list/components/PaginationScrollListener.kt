package com.example.movie.presentation.movie_list.components

import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.distinctUntilChanged

@Composable
fun PaginationScrollListener(
    gridState: LazyGridState, // ✅ التغيير هنا: LazyGridState
    itemCount: Int,
    isPaginationLoading: Boolean,
    isEndReached: Boolean,
    onLoadMore: () -> Unit // ✅ التغيير هنا: اسم أوضح
) {
    val updatedItemCount by rememberUpdatedState(itemCount)
    val isPaginationLoading by rememberUpdatedState(isPaginationLoading)
    val isEndReached by rememberUpdatedState(isEndReached)

    var lastTriggerItemCount by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(gridState) {
        snapshotFlow {
            val info = gridState.layoutInfo
            val totalItems = info.totalItemsCount

            // بنجيب آخر عنصر ظاهر في الشاشة عشان نعرف إحنا وصلنا لفين
            val lastVisibleItemIndex = info.visibleItemsInfo.lastOrNull()?.index ?: 0

            // بنحسب فاضل كام عنصر تحت
            val remainingItems = totalItems - lastVisibleItemIndex - 1

            PaginationScrollState(
                currentItemCount = updatedItemCount,
                isEligible = totalItems > 0 && // تأكد إن فيه داتا أصلاً
                        remainingItems <= 5 && // لو فاضل 5 عناصر أو أقل
                        !isPaginationLoading && // ومش بنحمل حالياً
                        !isEndReached // ولسه موصلناش للنهاية
            )
        }
            .distinctUntilChanged()
            .collect { (itemCount, isEligible) ->
                val shouldTrigger = isEligible && itemCount > lastTriggerItemCount

                if(shouldTrigger) {
                    lastTriggerItemCount = itemCount
                    onLoadMore() // نادينا الأكشن الجديد
                }
            }
    }
}

data class PaginationScrollState(
    val currentItemCount: Int,
    val isEligible: Boolean
)