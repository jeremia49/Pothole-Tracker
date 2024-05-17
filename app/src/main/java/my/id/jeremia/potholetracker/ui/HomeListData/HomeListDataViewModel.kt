package my.id.jeremia.potholetracker.ui.HomeListData

import dagger.hilt.android.lifecycle.HiltViewModel
import my.id.jeremia.potholetracker.ui.base.BaseViewModel
import my.id.jeremia.potholetracker.ui.common.loader.Loader
import my.id.jeremia.potholetracker.ui.common.snackbar.Messenger
import my.id.jeremia.potholetracker.ui.navigation.Navigator
import javax.inject.Inject

@HiltViewModel
class HomeListDataViewModel @Inject constructor(
    val navigator: Navigator,
    val messenger: Messenger,
    val loader: Loader,
) : BaseViewModel(
    loader,
    messenger,
    navigator,
) {

    companion object{
        const val TAG = "HomeListDataViewModel"
    }


}