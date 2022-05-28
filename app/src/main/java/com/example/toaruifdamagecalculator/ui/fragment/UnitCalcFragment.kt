package com.example.toaruifdamagecalculator.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.compose.ui.text.toLowerCase
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.data.model.AttackType
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitCalcBinding
import com.example.toaruifdamagecalculator.ui.viewmodel.UnitCalcViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UnitCalcFragment : Fragment(), AdapterView.OnItemSelectedListener {
    //todo SharedPreferences? Save last chosen unit (and maybe inputs)
    private val vm: UnitCalcViewModel by viewModels(
        ownerProducer = {requireActivity()}
    )

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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        collectFlows()
    }

    private fun init(){

        binding.charNameTv.setOnClickListener { onUnitSearch() }
        binding.calcButton.setOnClickListener { onCalcButtonPressed() }

        setupSpinner(R.array.attack_type, binding.atkTypeSpinner)
        binding.atkTypeSpinner.onItemSelectedListener = this
        setupSpinner(R.array.color_status, binding.ColorTypeSpinner)
        setupSpinner(R.array.gw_bonus, binding.GwBonusTypeSpinner)

    }

    private fun setupSpinner(textArrayResId: Int, spinner: Spinner){
        ArrayAdapter.createFromResource(
            requireContext(),
            textArrayResId,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
    }

    private fun onCalcButtonPressed() {
        if (binding.atkStatEdit.text.toString().isNotEmpty()) {
            binding.ResultDamageValueTv.text = vm.onCalculate(
                unit = currentUnit,
                atkType = binding.atkTypeSpinner.selectedItem.toString(),
                breakpoint = binding.BreakpointCb.isChecked,
                critical = binding.CriticalCb.isChecked,
                color = binding.ColorTypeSpinner.selectedItem.toString(),
                gwBonus = binding.GwBonusTypeSpinner.selectedItem.toString(),
                atkStat = binding.atkStatEdit.text.toString().toIntOrNull(),
                skillLvl = binding.atkLvlEdit.text.toString().toIntOrNull(),
                passive1 = binding.passive1Edit.text.toString().toIntOrNull(),
                passive2 = binding.passive2Edit.text.toString().toIntOrNull(),
                atkUp = binding.atkTypePercentEdit.text.toString().toIntOrNull(),
                critUp = binding.critAtkPercentEdit.text.toString().toIntOrNull(),
                defDown = binding.defDebuffPercentEdit.text.toString().toIntOrNull(),
                colorResDown = binding.colorDebuffPercentEdit.text.toString().toIntOrNull()
            ).toString()
        } else {
            Snackbar.make(
                binding.root,
                "Please enter your attack stat",
                Snackbar.LENGTH_LONG
            ).show()
        }
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
                    binding.ResultDamageValueTv.text = it
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
                //Change textViews based on character color and primary attack
                when (it.primaryAttack?.lowercase(Locale.ROOT)) {//todo change to non null
                    "mental" -> {
                        this.atkStatTitleTv.text = resources.getString(R.string.MentalAtkStat)
                        this.atkTypeBuffTv.text = resources.getString(R.string.MentalAtkUp)
                        this.DefenceDebuffTv.text = resources.getString(R.string.MentalDefDown)
                    }
                    "physical" -> {
                        this.atkStatTitleTv.text = resources.getString(R.string.PhysicalAtkStat)
                        this.atkTypeBuffTv.text = resources.getString(R.string.PhysicalAtkUp)
                        this.DefenceDebuffTv.text = resources.getString(R.string.PhysicalDefDown)
                    }
                }
                when (it.color?.lowercase(Locale.ROOT)) {//todo change to non null
                    "red" -> this.ColorDebuffTv.text = resources.getString(R.string.RedResDown)
                    "blue" -> this.ColorDebuffTv.text = resources.getString(R.string.BlueResDown)
                    "green" -> this.ColorDebuffTv.text = resources.getString(R.string.GreenResDown)
                    "purple" -> this.ColorDebuffTv.text = resources.getString(R.string.PurpleResDown)
                    "yellow" -> this.ColorDebuffTv.text = resources.getString(R.string.YellowResDown)
                }
                // Remove attack type from spinner if skill/sp doesn't do damage

                val listOfTypes = listOf<AttackType>()



                if (currentUnit.spAtkMultiplier==null || currentUnit.spAtkMultiplier==0 ||
                    currentUnit.skillAtkMultiplier==null || currentUnit.skillAtkMultiplier==0 ){
                    val changedList = resources.getStringArray(R.array.attack_type).toMutableList()
                    if (currentUnit.spAtkMultiplier==null || currentUnit.spAtkMultiplier==0)
                        changedList.remove(resources.getString(R.string.Sp))
                    if (currentUnit.skillAtkMultiplier==null || currentUnit.skillAtkMultiplier==0)
                        changedList.remove(resources.getString(R.string.Skill))
                    binding.atkTypeSpinner.adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        changedList
                    )
                }

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

    fun setListeners(){
        binding.apply {

        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent?.getItemAtPosition(position).toString() == "Normal attack" ) {
            binding.atkLvlTitleTv.visibility = View.INVISIBLE
            binding.atkLvlEdit.visibility = View.INVISIBLE
        } else {
            binding.atkLvlTitleTv.visibility = View.VISIBLE
            binding.atkLvlEdit.visibility = View.VISIBLE
            binding.atkLvlTitleTv.text = "${parent?.getItemAtPosition(position).toString()} level"
            binding.atkTypeSpinner
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.atkLvlTitleTv.visibility = View.INVISIBLE
    }
}