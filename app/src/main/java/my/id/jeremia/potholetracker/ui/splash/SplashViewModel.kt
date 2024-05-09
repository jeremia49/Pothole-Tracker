package my.id.jeremia.potholetracker.ui.splash

//@HiltViewModel
//class SplashViewModel @Inject constructor(
//    loader: Loader,
//    firebaseRemote: FirebaseRemoteConfig,
//    private val userRepository: UserRepository,
//    val navigator: Navigator,
//    val messenger: Messenger
//) : BaseViewModel(loader, messenger, navigator) {
//
//    companion object {
//        const val TAG = "SplashViewModel"
//    }
//
//    init {
//        // TODO: think of this for open source use case
//        firebaseRemote.ensureInitialized().addOnCompleteListener {
//            val exists = userRepository.userExists()
//            if (exists) {
//                if (userRepository.isOnBoardingComplete()) {
//                    navigator.navigateTo(Destination.Home.route, true)
//                } else {
//                    navigator.navigateTo(Destination.Onboarding.route, true)
//                }
//            } else {
//                navigator.navigateTo(Destination.Login.route, true)
//            }
//        }
//    }
//}