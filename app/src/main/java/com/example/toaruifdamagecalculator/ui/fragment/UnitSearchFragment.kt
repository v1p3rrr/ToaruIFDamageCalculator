package com.example.toaruifdamagecalculator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.data.api.RetrofitBuilder
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.database.AppRoomDatabase
import com.example.toaruifdamagecalculator.databinding.FragmentUnitSearchBinding
import com.example.toaruifdamagecalculator.ui.adapter.UnitsAdapter
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitSearchViewModel
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitSearchViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class UnitSearchFragment : Fragment(), OnRecyclerViewItemClick<Long> {

    private lateinit var binding: FragmentUnitSearchBinding

    private lateinit var unitSearchViewModel: UnitSearchViewModel

    private lateinit var unitsAdapter: UnitsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnitSearchBinding.inflate(inflater, container, false)
        unitSearchViewModel = ViewModelProvider(
            this, UnitSearchViewModelFactory(
                UnitApiHelper(
                    RetrofitBuilder.unitApiService
                ),
                AppRoomDatabase.getDatabase(requireContext()).battleUnitDao()
            )
        ).get(
            UnitSearchViewModel::class.java
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        collectFlows()
    }

    private fun init() {
        unitsAdapter = UnitsAdapter(this@UnitSearchFragment)
        unitSearchViewModel.getAllUnits()
        binding.searchRecyclerView.apply {
            adapter = unitsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.unitSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                (binding.searchRecyclerView.adapter as UnitsAdapter).filter(newText)
                return true
            }

        })
    }

    private fun collectFlows() {
        collectUnitsFlow()
        collectErrorFlow()
    }

    private fun collectUnitsFlow(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                unitSearchViewModel.unitsStateFlow.collectLatest {
                    unitsAdapter.modifyList(it)
                }
            }
        }
    }

    private fun collectErrorFlow(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                unitSearchViewModel.errorSharedFlow.collectLatest {
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    override fun onItemClick(view: View?, arg: Long) {
        when (view?.id) {
            R.id.unitName -> {
                onUnitSelect(arg)
            }
        }
    }

    private fun onUnitSelect(id: Long) {
        val directions = UnitSearchFragmentDirections.actionUnitSearchFragmentToUnitCalcFragment(
            id = id
        )
        findNavController().navigate(directions)
    }
}