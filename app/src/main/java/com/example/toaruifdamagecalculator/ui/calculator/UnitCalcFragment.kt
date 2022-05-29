package com.example.toaruifdamagecalculator.ui.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.toaruifdamagecalculator.R
import com.example.toaruifdamagecalculator.common.AttackType
import com.example.toaruifdamagecalculator.common.ColorType
import com.example.toaruifdamagecalculator.common.GwBonusType
import com.example.toaruifdamagecalculator.data.model.BattleUnit
import com.example.toaruifdamagecalculator.databinding.FragmentUnitCalcBinding
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
        ownerProducer = { requireActivity() }
    )

    private lateinit var binding: FragmentUnitCalcBinding

    private val safeArgs: UnitCalcFragmentArgs by navArgs()

    private lateinit var currentUnit: BattleUnit

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

    private fun init() {

        binding.charNameTv.setOnClickListener { onUnitSearch() }
        //binding.calcButton.setOnClickListener { onCalcButtonPressed() }

        setupSpinner(R.array.attack_type, binding.atkTypeSpinner)
        setupSpinner(R.array.color_status, binding.colorTypeSpinner)
        setupSpinner(R.array.gw_bonus, binding.gwBonusTypeSpinner)
        setListeners()
    }

    private fun setupSpinner(textArrayResId: Int, spinner: Spinner) {
        ArrayAdapter.createFromResource(
            requireContext(),
            textArrayResId,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = it
        }
    }


    private fun collectFlows() {
        collectErrorFlow()
        collectUnitFlow()
        collectStateFlow()
    }

    private fun collectUnitFlow() {
        vm.getUnitById(safeArgs.id)

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.unitFlow.collectLatest {
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
                    binding.resultDamageValueTv.text = it
                    Snackbar.make(
                        binding.root,
                        it,
                        Snackbar.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun collectStateFlow() {
        lifecycleScope.launchWhenCreated {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.state.collectLatest {
                    binding.apply {
                        //todo battleunit id??
                        atkStatEdit.setText((it.atkStat ?: "").toString())
                        atkStatEdit.setSelection(atkStatEdit.length())

                        if (skillLvlEdit.isVisible) {
                            skillLvlEdit.setText((it.skillLevel ?: "").toString())
                            skillLvlEdit.setSelection(skillLvlEdit.length())
                        }

                        //critUpEdit.setText((it.critUp ?: "").toString())
                        //critUpEdit.setSelection(critUpEdit.length())

                        //atkUpEdit.setText((it.atkUp ?: "").toString())
                        //atkUpEdit.setSelection(atkUpEdit.length())

                        //defDebuffEdit.setText((it.defDown ?: "").toString())
                        //defDebuffEdit.setSelection(defDebuffEdit.length())

                        //colorDebuffEdit.setText((it.colorResDown ?: "").toString())
                        //colorDebuffEdit.setSelection(colorDebuffEdit.length())

                        passive1Edit.setText((it.passive1Level ?: "").toString())
                        passive1Edit.setSelection(passive1Edit.length())

                        passive2Edit.setText((it.passive2Level ?: "").toString())
                        passive2Edit.setSelection(passive2Edit.length())

                        breakpointCb.isChecked = it.breakpoint
                        criticalCb.isChecked = it.critical
                        resultDamageValueTv.text = it.expectedDamage.toString()


                        var spinnerAdapter = binding.atkTypeSpinner.adapter as ArrayAdapter<String> //todo
                        var itemPos = spinnerAdapter.getPosition(it.atkType.toString())
                        binding.atkTypeSpinner.setSelection(itemPos)

                        spinnerAdapter = binding.colorTypeSpinner.adapter as ArrayAdapter<String>
                        itemPos = spinnerAdapter.getPosition(it.colorType.toString())
                        binding.colorTypeSpinner.setSelection(itemPos)

                        spinnerAdapter = binding.gwBonusTypeSpinner.adapter as ArrayAdapter<String>
                        itemPos = spinnerAdapter.getPosition(it.gwBonusType.toString())
                        binding.gwBonusTypeSpinner.setSelection(itemPos)
                    }
                }
            }
        }
    }

    private fun onUnitSearch() {
        val directions = UnitCalcFragmentDirections.actionUnitCalcFragmentToUnitSearchFragment()
        findNavController().navigate(directions)
    }

    //setup views according to selected units, views updates according to state occurs in collectStateFlow()
    private fun setUnitViews(unit: BattleUnit?) {
        binding.apply {
            unit?.let {
                charNameTv.text = "${it.charName} | ${it.cardName}"
                //Change textViews based on character color and primary attack
                when (it.primaryAttack?.lowercase(Locale.ROOT)) {//todo change to non null
                    "mental" -> {
                        this.atkStatTitleTv.text = resources.getString(R.string.MentalAtkStat)
                        this.skillLvlTitleTv.text = resources.getString(R.string.MentalAtkUp)
                        this.DefenceDebuffTv.text = resources.getString(R.string.MentalDefDown)
                    }
                    "physical" -> {
                        this.atkStatTitleTv.text = resources.getString(R.string.PhysicalAtkStat)
                        this.skillLvlTitleTv.text = resources.getString(R.string.PhysicalAtkUp)
                        this.DefenceDebuffTv.text = resources.getString(R.string.PhysicalDefDown)
                    }
                }
                when (it.color?.lowercase(Locale.ROOT)) {//todo change to non null
                    "red" -> this.ColorDebuffTv.text = resources.getString(R.string.RedResDown)
                    "blue" -> this.ColorDebuffTv.text = resources.getString(R.string.BlueResDown)
                    "green" -> this.ColorDebuffTv.text = resources.getString(R.string.GreenResDown)
                    "purple" -> this.ColorDebuffTv.text =
                        resources.getString(R.string.PurpleResDown)
                    "yellow" -> this.ColorDebuffTv.text =
                        resources.getString(R.string.YellowResDown)
                }


                // Remove attack type from spinner if skill/sp doesn't do damage
                if (currentUnit.spAtkMultiplier == null || currentUnit.spAtkMultiplier == 0 ||
                    currentUnit.skillAtkMultiplier == null || currentUnit.skillAtkMultiplier == 0
                ) {
                    val changedList: MutableList<String> =
                        resources.getStringArray(R.array.attack_type).toMutableList()
                    if (currentUnit.spAtkMultiplier == null || currentUnit.spAtkMultiplier == 0)
                        changedList.remove(resources.getString(R.string.Sp))
                    if (currentUnit.skillAtkMultiplier == null || currentUnit.skillAtkMultiplier == 0)
                        changedList.remove(resources.getString(R.string.Skill))
                    binding.atkTypeSpinner.adapter = ArrayAdapter(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        changedList
                    )
                } else setupSpinner(R.array.attack_type, binding.atkTypeSpinner)

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

    //Spinners listeners implementations are onItemSelected/onNothingSelected
    private fun setListeners() {

        class MyTextWatcher(val view: View) : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.run {
                    when (view) {
                        atkStatEdit -> vm.onAtkStatEditChangeEvent(s.toString().toIntOrNull())
                        skillLvlEdit -> vm.onSkillLevelEditChangeEvent(s.toString().toIntOrNull())
                        passive1Edit -> vm.onPassive1LevelEditChangeEvent(s.toString().toIntOrNull())
                        passive2Edit -> vm.onPassive2LevelEditChangeEvent(s.toString().toIntOrNull())
                        //atkUpEdit -> vm.onAtkUpEditChangeEvent(s.toString().toIntOrNull())
                        //critUpEdit -> vm.onCritUpEditChangeEvent(s.toString().toIntOrNull())
                        //defDebuffEdit -> vm.onDefDownEditChangeEvent(s.toString().toIntOrNull())
                        //colorDebuffEdit -> vm.onColorResDownEditChangeEvent(s.toString().toIntOrNull())
                    }

                }

            }

            override fun afterTextChanged(s: Editable?) {
            }
        }

        binding.defDebuffSlider.addOnChangeListener {}

        binding.apply {
            atkStatEdit.apply { addTextChangedListener(MyTextWatcher(this)); }
            skillLvlEdit.apply { addTextChangedListener(MyTextWatcher(this)) }
            passive1Edit.apply { addTextChangedListener(MyTextWatcher(this)); }
            passive2Edit.apply { addTextChangedListener(MyTextWatcher(this)); }
            //atkUpEdit.apply { addTextChangedListener(MyTextWatcher(this)); }
            //critUpEdit.apply { addTextChangedListener(MyTextWatcher(this)); }
            //defDebuffEdit.apply { addTextChangedListener(MyTextWatcher(this)); }
            //colorDebuffEdit.apply { addTextChangedListener(MyTextWatcher(this)); }
            breakpointCb.setOnCheckedChangeListener { _, isChecked ->
                vm.onBreakpointCheckBoxChangeEvent(
                    isChecked
                )
            }
            criticalCb.setOnCheckedChangeListener { _, isChecked ->
                vm.onCriticalCheckBoxChangeEvent(
                    isChecked
                )
            }
            atkTypeSpinner.onItemSelectedListener = this@UnitCalcFragment
            colorTypeSpinner.onItemSelectedListener = this@UnitCalcFragment
            gwBonusTypeSpinner.onItemSelectedListener = this@UnitCalcFragment
        }
    }

    //Spinner Listener
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        binding.apply {
            when (parent) {
                atkTypeSpinner -> {
                    if (parent.getItemAtPosition(position).toString() == "Normal attack") {
                        skillLvlTitleTv.visibility = View.INVISIBLE
                        skillLvlEdit.visibility = View.INVISIBLE
                    } else {
                        skillLvlTitleTv.visibility = View.VISIBLE
                        skillLvlEdit.visibility = View.VISIBLE
                        skillLvlTitleTv.text =
                            "${parent.getItemAtPosition(position)} level"
                    }
                    AttackType.fromString(parent.getItemAtPosition(position).toString())
                        ?.let { vm.onAtkTypeSpinnerChangeEvent(it) }
                }
                colorTypeSpinner -> ColorType.fromString(
                    parent.getItemAtPosition(position).toString()
                )
                    ?.let { vm.onColorTypeSpinnerChangeEvent(it) }
                gwBonusTypeSpinner -> GwBonusType.fromString(
                    parent.getItemAtPosition(position).toString()
                )
                    ?.let { vm.onGwBonusTypeSpinnerChangeEvent(it) }
            }
        }
    }

    //Spinner Listener
    override fun onNothingSelected(parent: AdapterView<*>?) {
        binding.skillLvlTitleTv.visibility = View.INVISIBLE
    }

}