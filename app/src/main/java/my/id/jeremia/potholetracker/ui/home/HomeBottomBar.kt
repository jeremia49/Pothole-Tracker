package my.id.jeremia.potholetracker.ui.home

import androidx.annotation.DrawableRes
import androidx.annotation.Keep
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import my.id.jeremia.potholetracker.R
import my.id.jeremia.potholetracker.ui.navigation.Destination
import my.id.jeremia.potholetracker.ui.theme.PotholeTrackerTheme


@Keep
enum class HomeTab(
    @StringRes val title: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
    val route: String,
    val destRoute: String,
) {
    FIND(
        R.string.FIND_title,
        R.drawable.compass_outline,
        R.drawable.compass,
        Destination.Home.Find.route,
        Destination.Home.Find.route,
    ),

    DATA(
        R.string.DATA_title,
        R.drawable.view_list_outline,
        R.drawable.view_list,
        Destination.Home.Data.route,
        Destination.Home.Data.route,
    ),

    CONTRIBUTE(
        R.string.KONTRIBUSI_title,
        R.drawable.map_plus,
        R.drawable.map_plus,
        Destination.Home.Contribute.route,
        Destination.Home.Contribute.route,
    ),

    MYACCOUNT(
        R.string.MYACCOUNT_title,
        R.drawable.account_outline,
        R.drawable.account,
        Destination.Home.MyAccount.route,
        Destination.Home.MyAccount.route,
    ),

}

@Composable
fun HomeBottomBar(navController: NavController) {

    val tabs = remember { HomeTab.entries.toTypedArray().asList() }
    val routes = remember { HomeTab.entries.map { it.route } }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
        ?: Destination.Home.Find.route

    HomeBottomBarView(
        tabs = tabs,
        routes = routes,
        currentRoute = currentRoute,
        tabClick = {
            if (it.route != currentRoute) {
                navController.navigate(it.destRoute) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    )
}

@Composable
private fun HomeBottomBarView(
    tabs: List<HomeTab>,
    routes: List<String>,
    currentRoute: String,
    tabClick: (HomeTab) -> Unit
) {

    if (currentRoute in routes) {
        NavigationBar(
            Modifier
                .windowInsetsBottomHeight(
                    WindowInsets.navigationBars.add(WindowInsets(bottom = 60.dp))
                ),
            tonalElevation = 5.dp
        ) {
            tabs.forEach { tab ->
                val selected = currentRoute == tab.route
                NavigationBarItem(
                    icon = {
                        Icon(
                            painterResource(if (selected) tab.selectedIcon else tab.unselectedIcon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    },
                    label={
                        Text(stringResource(id = tab.title))
                    },
                    selected = selected,
                    onClick = { tabClick(tab) },
                    alwaysShowLabel = false,
                    modifier = Modifier.navigationBarsPadding(),
                )
            }
        }
    }
}

@Preview("Light")
@Composable
private fun HomeBottomBarLightPreview() {
    PotholeTrackerTheme {
        HomeBottomBarView(
            tabs = HomeTab.entries.toTypedArray().asList(),
            routes = HomeTab.entries.map { it.route },
            currentRoute = HomeTab.FIND.route,
        ) {}
    }
}

@Preview("Dark")
@Composable
private fun HomeBottomBarDarkPreview() {
    PotholeTrackerTheme(darkTheme = true) {
        HomeBottomBarView(
            tabs = HomeTab.entries.toTypedArray().asList(),
            routes = HomeTab.entries.map { it.route },
            currentRoute = HomeTab.FIND.route,
        ) {}
    }
}