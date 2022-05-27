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
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitCalcBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitCalcViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@AndroidEntryPoint
class UnitCalcFragment : Fragment() {
    //todo SharedPreferences? Save last chosen unit (and maybe inputs)
    private val vm: UnitCalcViewModel by viewModels()

    private lateinit var binding: FragmentUnitCalcBinding

    private val safeArgs: UnitCalcFragmentArgs by navArgs()

    lateinit var currentUnit: BattleUnit

    @Inject
    lateinit var picasso: Picasso

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
        binding.calcButton.setOnClickListener( { onCalcButtonPressed() })
    }

    private fun onCalcButtonPressed() {
        binding.ResultDamage.text = vm.onCalculate(
            unit = currentUnit,
            atkType = binding.atkType.selectedItem.toString(),
            breakpoint = binding.Breakpoint.isChecked,
            critical = binding.Critical.isChecked,
            color = binding.ColorType.selectedItem.toString(),
            gwBonus = binding.GwBonusType.selectedItem.toString(),
            atkStat = binding.atkStat.text.toString().toInt(),
            skillLvl = binding.atkLvl.text.toString().toInt(),
            passive1 = binding.passive1.text.toString().toInt(),
            passive2 = binding.passive2.text.toString().toInt(),
            atkUp = binding.atkTypePercent.text.toString().toInt(),
            critUp = binding.critAtkPercent.text.toString().toInt(),
            defDown = binding.defDebuffPercent.text.toString().toInt(),
            colorResDown =  binding.colorDebuffPercent.text.toString().toInt()
        ).toString()
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
                    currentUnit = it
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
                picasso.load(it.imageUrl)
                    .apply { if (it.imageUrl.isNullOrEmpty()) placeholder(R.drawable.questionmark_placeholder) }
                    .error(R.drawable.image_error_placeholder).fit()
                    .centerCrop().into(this.charImg, object : Callback {
                        override fun onSuccess() {

                        }

                        override fun onError(e: Exception?) {
                            Snackbar.make(
                                binding.root,
                                "Unable to load image",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }

                    })
            }
        }
    }
}