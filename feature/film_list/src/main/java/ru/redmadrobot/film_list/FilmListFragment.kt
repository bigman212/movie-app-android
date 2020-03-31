package ru.redmadrobot.film_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import ru.redmadrobot.common.base.BaseFragment
import ru.redmadrobot.film_list.di.component.FilmListComponent
import javax.inject.Inject

class FilmListFragment : BaseFragment() {
    @Inject
    internal lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: FilmListViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//        return inflater.inflate(R.layout.fragment_film_list, container, false) as ViewGroup
        return null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initDagger()
//        initViewModel()
//        initViews()
    }

    private fun initDagger() {
        FilmListComponent.init(appComponent).inject(this)
    }

    companion object {
        fun newInstance() = FilmListFragment()
    }
}
