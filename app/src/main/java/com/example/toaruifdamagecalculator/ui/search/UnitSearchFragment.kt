package com.example.toaruifdamagecalculator.ui.search

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitSearchBinding
import com.example.toaruifdamagecalculator.ui.search.adapter.UnitsAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UnitSearchFragment : Fragment() {

    private val vm: UnitSearchViewModel by viewModels(
        ownerProducer = {
            requireActivity()
        }
    )

    private lateinit var binding: FragmentUnitSearchBinding

    private lateinit var unitsAdapter: UnitsAdapter

    private var unfilteredList = listOf<BattleUnit>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        Log.v("UnitSearch", vm.toString())
        collectFlows()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.unitSearchView.setQuery("", false)
    }

    private fun init() {
        unitsAdapter = UnitsAdapter{ unitId -> unitId.apply { onUnitSelect(unitId) } }
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
                vm.onSearchQueryChange(newText?:"")
                return true
            }

        })

        binding.searchSwipeRefreshLayout.setOnRefreshListener {
            vm.onRefreshUpdateUnitsFromApiToDb {
                binding.searchSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun collectFlows() {
        collectUnitsFlow()
        collectErrorFlow()
        collectState()
    }



    private fun onUnitSelect(id: Long) {
        val directions = UnitSearchFragmentDirections.actionUnitSearchFragmentToUnitCalcFragment(
            id = id
        )
        binding.unitSearchView.setQuery("", false)
        findNavController().navigate(directions)
    }


    private fun collectUnitsFlow(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.unitsStateFlow.collectLatest {
                    it.sortedBy { list -> list.charName }
                    unfilteredList = arrayListOf()
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

    private fun collectState(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collectLatest {
                    binding.unitSearchView.setQuery(it, false)
                    filterUnitsInRecycler(it)
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