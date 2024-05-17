package my.id.jeremia.potholetracker.ui.HomeContribute

import dagger.hilt.android.lifecycle.HiltViewModel
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class HomeContributeViewModel @Inject constructor(
    val loader: Loader,
    val navigator: Navigator,
    val messenger: Messenger,
    ): BaseViewModel(
    loader,messenger,navigator
) {

    companion object{
        const val TAG = "HomeContributeViewModel"
    }

}