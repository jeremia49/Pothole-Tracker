package my.id.jeremia.potholetracker.ui.base

import androidx.lifecycle.ViewModel
import dagger.hilt.android.scopes.ActivityRetainedScoped


abstract class BaseViewModel(
) : ViewModel() {

    companion object {
        const val TAG = "BaseViewModel"
    }

}