package my.id.jeremia.potholetracker.ui.navigation


object Destination {

    data object Splash : Screen("/splash")

    data object Login : Screen("/login")

    data object Register: Screen("/register")

    data object ServerUnreachable: Screen("/server-unreachable")

    data object Home:Screen("/home"){

        data object Find:Screen("/home/find")

    }

    abstract class Screen(baseRoute: String) {
        open val route = baseRoute
    }


}