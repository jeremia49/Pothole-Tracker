package my.id.jeremia.potholetracker.ui.common.snackbar

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import my.id.jeremia.potholetracker.ui.theme.black
import my.id.jeremia.potholetracker.ui.theme.info
import my.id.jeremia.potholetracker.ui.theme.success
import my.id.jeremia.potholetracker.ui.theme.warning
import my.id.jeremia.potholetracker.ui.theme.white

@Composable
fun AppSnackbar(
    snackbarHostState: SnackbarHostState,
    messenger: Messenger
) {
    MessageHandler(snackbarHostState, messenger)

    val messageType = messenger.messageType.collectAsStateWithLifecycle()

    val color = when (messageType.value) {
        Message.Type.SUCCESS -> MaterialTheme.colorScheme.success
        Message.Type.ERROR -> MaterialTheme.colorScheme.error
        Message.Type.WARNING -> MaterialTheme.colorScheme.warning
        Message.Type.INFO -> MaterialTheme.colorScheme.info
    }

    SnackbarHost(hostState = snackbarHostState) {
        Snackbar(
            snackbarData = it,
            containerColor = color,
            contentColor = MaterialTheme.colorScheme.white,
            actionColor = MaterialTheme.colorScheme.white
        )
    }
}