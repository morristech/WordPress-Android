package org.wordpress.android.ui.stats.refresh

import org.wordpress.android.R
import org.wordpress.android.fluxc.model.SiteModel
import org.wordpress.android.fluxc.model.stats.InsightsAllTimeModel
import org.wordpress.android.fluxc.store.InsightsStore
import org.wordpress.android.ui.stats.refresh.BlockListItem.Item
import org.wordpress.android.ui.stats.refresh.BlockListItem.Title
import javax.inject.Inject

class InsightsAllTimeDomain
@Inject constructor(private val insightsStore: InsightsStore) {
    suspend fun allTimeInsights(site: SiteModel, forced: Boolean = false): InsightsItem {
        val response = insightsStore.fetchAllTimeInsights(site, forced)
        val model = response.model
        val error = response.error
        return when {
            model != null -> allTimeInsightsItem(model)
            error != null -> Failed(R.string.stats_insights_all_time, error.message ?: error.type.name)
            else -> throw Exception("All times stats response should contain a model or an error")
        }
    }

    private fun allTimeInsightsItem(model: InsightsAllTimeModel): AllTimeInsightsItem {
        val items = mutableListOf<BlockListItem>()
        items.add(Title(R.string.stats_insights_all_time))
        items.add(Item(R.drawable.ic_posts_grey_dark_24dp, R.string.posts, model.posts.toFormattedString()))
        items.add(Item(R.drawable.ic_posts_grey_dark_24dp, R.string.stats_views, model.views.toFormattedString()))
        items.add(Item(R.drawable.ic_user_grey_24dp, R.string.stats_visitors, model.visitors.toFormattedString()))
        items.add(
                Item(
                        R.drawable.ic_user_grey_24dp,
                        R.string.stats_insights_best_ever,
                        model.viewsBestDayTotal.toFormattedString()
                )
        )
        return AllTimeInsightsItem(items)
    }
}
