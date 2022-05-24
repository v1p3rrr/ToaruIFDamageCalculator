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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.data.api.RetrofitBuilder
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.database.AppRoomDatabase
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitSearchBinding
import com.example.toaruifdamagecalculator.ui.adapter.UnitsAdapter
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitSearchViewModel
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitSearchViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class UnitSearchFragment : Fragment(), OnRecyclerViewItemClick<Long> {

    private lateinit var binding: FragmentUnitSearchBinding

    private lateinit var vm: UnitSearchViewModel

    private lateinit var unitsAdapter: UnitsAdapter

    private var unfilteredList = listOf<BattleUnit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUnitSearchBinding.inflate(inflater, container, false)
        vm = ViewModelProvider(
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
        vm.getAllUnits()
        binding.searchRecyclerView.apply {
            adapter = unitsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        binding.unitSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterUnitsInRecycler(newText)
                return true
            }

        })

        binding.searchSwipeRefreshLayout.setOnRefreshListener {
            vm.onRefreshUpdateUnitsFromApiToDb(binding.searchSwipeRefreshLayout)
        }
    }

    private fun collectFlows() {
        collectUnitsFlow()
        collectErrorFlow()
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


    private fun collectUnitsFlow(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.unitsStateFlow.collectLatest {
                    unfilteredList = arrayListOf()
                    (unfilteredList as ArrayList<BattleUnit>).addAll(it)
                    (unfilteredList as ArrayList<BattleUnit>).addAll(it)
                    (unfilteredList as ArrayList<BattleUnit>).addAll(it)
                    (unfilteredList as ArrayList<BattleUnit>).addAll(it)

                    unitsAdapter.submitList(unfilteredList)
                }
            }
        }
    }

    private fun collectErrorFlow(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.errorSharedFlow.collectLatest {
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    fun filterUnitsInRecycler(query: CharSequence?) {
        val list = mutableListOf<BattleUnit>()
        val queryList = query?.split(Regex("\\W"))

        // perform the data filtering
        if (query.isNullOrEmpty()) {
            list.addAll(unfilteredList)
        } else {
            list.addAll(unfilteredList.filter {
                checkQueryEntry(it, queryList)
            })
        }
        unitsAdapter.submitList(list)
    }

    private fun checkQueryEntry(unit: BattleUnit, queryList: List<String>?) : Boolean{
        queryList?.let {
            for (queryWord in it) {
                queryWord.let {
                    if (!(unit.charName.contains(queryWord, ignoreCase = true) || unit.cardName.contains(queryWord, ignoreCase = true)))
                        return false
                }
            }
            return true
        }
        return false
    }


}