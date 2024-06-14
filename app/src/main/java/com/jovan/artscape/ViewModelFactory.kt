package com.jovan.artscape

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jovan.artscape.data.Injection
import com.jovan.artscape.data.ProvideRepository
import com.jovan.artscape.ui.login.LoginViewModel
import com.jovan.artscape.ui.login.address.AddressViewModel
import com.jovan.artscape.ui.login.artist.UserViewModel
import com.jovan.artscape.ui.login.interest.InterestViewModel
import com.jovan.artscape.ui.main.MainViewModel
import com.jovan.artscape.ui.main.account.AccountViewModel
import com.jovan.artscape.ui.main.home.HomeViewModel
import com.jovan.artscape.ui.main.painting.DetailPaintingViewModel
import com.jovan.artscape.ui.mypainting.MyPaintingViewModel
import com.jovan.artscape.ui.mypainting.detail.MyPaintingDetailsViewModel
import com.jovan.artscape.ui.profile.EditProfileViewModel
import com.jovan.artscape.ui.search.SearchViewModel
import com.jovan.artscape.ui.upload.UploadViewModel

class ViewModelFactory(
    private val repository: ProvideRepository,
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(AddressViewModel::class.java) -> {
                AddressViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AccountViewModel::class.java) -> {
                AccountViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(repository) as T
            }
            modelClass.isAssignableFrom(UserViewModel::class.java) -> {
                UserViewModel(repository) as T
            }
            modelClass.isAssignableFrom(InterestViewModel::class.java) -> {
                InterestViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EditProfileViewModel::class.java) -> {
                EditProfileViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyPaintingViewModel::class.java) -> {
                MyPaintingViewModel(repository) as T
            }

            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(repository) as T
            }

            modelClass.isAssignableFrom(DetailPaintingViewModel::class.java) -> {
                DetailPaintingViewModel(repository) as T
            }
            modelClass.isAssignableFrom(MyPaintingDetailsViewModel::class.java) -> {
                MyPaintingDetailsViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }

    companion object {
        @Suppress("ktlint:standard:property-naming")
        @Volatile
        private var INSTANCE: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return INSTANCE as ViewModelFactory
        }
    }
}
