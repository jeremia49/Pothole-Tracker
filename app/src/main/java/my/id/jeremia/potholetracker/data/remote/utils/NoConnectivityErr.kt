package my.id.jeremia.potholetracker.data.remote.utils

import okio.IOException

class NoConnectivityErr : IOException() {
    override val message: String = "No internet connection"
}