package com.example.toaruifdamagecalculator.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitCalcBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitCalcViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class UnitCalcFragment : Fragment() {
//todo SharedPreferences? Save last chosen unit (and maybe inputs)
    private val vm: UnitCalcViewModel by viewModels()

    private lateinit var binding: FragmentUnitCalcBinding

    private val safeArgs: UnitCalcFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitCalcBinding.inflate(inflater, container, false)
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

    private fun collectUnitFlow() {
        vm.getUnitById(safeArgs.id)

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.unitStateFlow.collectLatest {
                    setUnitViews(it)
                }
            }
        }
    }

    private fun collectErrorFlow() {
        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.errorSharedFlow.collectLatest {
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

    private fun setUnitViews(unit: BattleUnit?) {
        binding.apply {
            unit?.let {
                charNameTv.text = "${it.charName} | ${it.cardName}"
                atkStat.setText(it.imageUrl)
            }
        }
    }
}