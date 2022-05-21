package com.example.toaruifdamagecalculator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.toaruifdamagecalculator.data.api.RetrofitBuilder
import com.example.toaruifdamagecalculator.data.api.UnitApiHelper
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitCalcBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitCalcViewModel
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitCalcViewModelFactory
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest

class UnitCalcFragment : Fragment() {

    private lateinit var unitCalcViewModel: UnitCalcViewModel

    private lateinit var binding: FragmentUnitCalcBinding

    private val safeArgs: UnitCalcFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitCalcBinding.inflate(inflater, container, false)
        unitCalcViewModel = ViewModelProvider(
            this,
            UnitCalcViewModelFactory(UnitApiHelper(RetrofitBuilder.unitApiService))
        ).get(UnitCalcViewModel::class.java)

        collectFlows()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.charNameTv.setOnClickListener { onUnitSearch() }
    }

    private fun collectFlows() {
        collectErrorFlow()
        collectUnitFlow()
    }

    private fun collectUnitFlow(){
        unitCalcViewModel.getUnitById(safeArgs.id)

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                unitCalcViewModel.unitStateFlow.collectLatest {
                    setUnitViews(it)
                }
            }
        }
    }

    private fun collectErrorFlow(){
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                unitCalcViewModel.errorSharedFlow.collectLatest {
                    binding.ResultDamage.text = it
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun onUnitSearch() {
        val directions = UnitCalcFragmentDirections.actionUnitCalcFragmentToUnitSearchFragment()
        findNavController().navigate(directions)
    }

    private fun setUnitViews(unit: BattleUnit) {
        binding.apply {
            charNameTv.text = "${unit.charName} | ${unit.cardName}"
            atkStat.setText(unit.imageUrl)
        }
    }
}