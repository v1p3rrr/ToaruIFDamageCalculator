package com.vpr.toaruifdamagecalculator.ui.calculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.vpr.toaruifdamagecalculator.R
import com.vpr.toaruifdamagecalculator.common.AttackType
import com.vpr.toaruifdamagecalculator.common.ColorType
import com.vpr.toaruifdamagecalculator.common.GwBonusType
import com.vpr.toaruifdamagecalculator.data.model.BattleUnit
import com.vpr.toaruifdamagecalculator.databinding.FragmentUnitCalcBinding
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class UnitCalcFragment : Fragment() {
    //todo SharedPreferences? (preferences data store) Save last chosen unit (and maybe inputs)
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
        binding.charImg.setOnClickListener{ onImageClick() }

        setupSpinner(R.array.attack_type_all, binding.atkTypeSpinner)
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
                    setupUnitViews(it)
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
                        atkStatEdit.setText((it.atkStat ?: "").toString())
                        atkStatEdit.setSelection(atkStatEdit.length())

                        if (skillLvlEdit.isVisible) {
                            skillLvlEdit.setText((it.skillLevel ?: "").toString())
                            skillLvlEdit.setSelection(skillLvlEdit.length())
                        }

                        atkUpPercentTv.text = it.atkUp.toString()+"%"
                        atkUpSlider.value = it.atkUp?.toFloat() ?: 0F

                        critUpPercentTv.text = it.critUp.toString()+"%"
                        critUpSlider.value = it.critUp?.toFloat() ?: 0F

                        defDownPercentTv.text = it.defDown.toString()+"%"
                        defDownSlider.value = it.defDown?.toFloat() ?: 0F

                        colorResDownPercentTv.text = it.colorResDown.toString()+"%"
                        colorResDownSlider.value = it.colorResDown?.toFloat() ?: 0F

                        passive1Edit.setText((it.passive1Level ?: "").toString())
                        passive1Edit.setSelection(passive1Edit.length())

                        passive2Edit.setText((it.passive2Level ?: "").toString())
                        passive2Edit.setSelection(passive2Edit.length())

                        breakpointCb.isChecked = it.breakpoint
                        criticalCb.isChecked = it.critical
                        spBonusCb.isChecked = it.spBonus

                        resultDamageValueTv.text = it.expectedDamage.toString()

                        var spinnerAdapter = binding.atkTypeSpinner.adapter as ArrayAdapter<String>
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

    private fun onImageClick() {
        var imageUrlsArray = arrayOf<String>()
        if (!currentUnit.imageUrl.isNullOrEmpty() || !currentUnit.imageSecondUrl.isNullOrEmpty()) {
            currentUnit.imageUrl?.let { imageUrlsArray = imageUrlsArray.plus(it) }
            currentUnit.imageSecondUrl?.let { imageUrlsArray = imageUrlsArray.plus(it) }
            val directions =
                UnitCalcFragmentDirections.actionUnitCalcFragmentToUnitImagesFragment(imageUrls = imageUrlsArray)
            findNavController().navigate(directions)
        }
    }

    //setup views according to selected units, views updates according to state occurs in collectStateFlow()
    private fun setupUnitViews(unit: BattleUnit?) {
        binding.apply {
            unit?.let {
                charNameTv.text = "${it.charName} | ${it.cardName}"
                //Change textViews based on character color and primary attack
                when (it.primaryAttack?.lowercase(Locale.ROOT)) {//todo change to non null
                    "mental" -> {
                        this.atkStatTitleTv.text = resources.getString(R.string.MentalAtkStat)
                        this.atkUpTitleTv.text = resources.getString(R.string.MentalAtkUp)
                        this.defDownTitleTv.text = resources.getString(R.string.MentalDefDown)
                    }
                    "physical" -> {
                        this.atkStatTitleTv.text = resources.getString(R.string.PhysicalAtkStat)
                        this.atkUpTitleTv.text = resources.getString(R.string.PhysicalAtkUp)
                        this.defDownTitleTv.text = resources.getString(R.string.PhysicalDefDown)
                    }
                }
                when (it.color?.lowercase(Locale.ROOT)) {//todo change to non null
                    "red" -> this.colorResDownTitleTv.text = resources.getString(R.string.RedResDown)
                    "blue" -> this.colorResDownTitleTv.text = resources.getString(R.string.BlueResDown)
                    "green" -> this.colorResDownTitleTv.text = resources.getString(R.string.GreenResDown)
                    "purple" -> this.colorResDownTitleTv.text = resources.getString(R.string.PurpleResDown)
                    "yellow" -> this.colorResDownTitleTv.text = resources.getString(R.string.YellowResDown)
                }

                when (it.spBonusType?.lowercase(Locale.ROOT)) {
                    "magic" -> this.spBonusCb.text = resources.getString(R.string.MagicSpBonus)
                    "science" -> this.spBonusCb.text = resources.getString(R.string.ScienceSpBonus)
                    "fullhp" -> this.spBonusCb.text = resources.getString(R.string.FullHpSpBonus)
                    "belowhalfhp" -> this.spBonusCb.text = resources.getString(R.string.BelowHalfHpSpBonus)
                    else -> this.spBonusCb.visibility = View.INVISIBLE
                }
                this.spBonusCb.isChecked = false


                // Remove attack type from spinner if skill/sp doesn't do damage
                if ((currentUnit.spAtkMultiplier == null || currentUnit.spAtkMultiplier == 0)
                    && (currentUnit.skillAtkMultiplier == null || currentUnit.skillAtkMultiplier == 0))
                    setupSpinner(R.array.attack_type_normal, binding.atkTypeSpinner)
                else if (currentUnit.spAtkMultiplier == null || currentUnit.spAtkMultiplier == 0)
                    setupSpinner(R.array.attack_type_no_sp, binding.atkTypeSpinner)
                else if (currentUnit.skillAtkMultiplier == null || currentUnit.skillAtkMultiplier == 0)
                    setupSpinner(R.array.attack_type_no_skill, binding.atkTypeSpinner)
                binding.atkTypeSpinner.onItemSelectedListener = MyOnItemSelectedListener()

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

        binding.atkUpSlider.addOnChangeListener { _, value, _ ->
            vm.onAtkUpSliderChange(value.toInt())
        }

        binding.critUpSlider.addOnChangeListener {  _, value, _ ->
            vm.onCritUpSliderChange(value.toInt())
        }

        binding.defDownSlider.addOnChangeListener {  _, value, _ ->
            vm.onDefDownSliderChange(value.toInt())
        }

        binding.colorResDownSlider.addOnChangeListener {  _, value, _ ->
            vm.onColorResDownSliderChange(value.toInt())
        }

        binding.apply {
            atkStatEdit.addTextChangedListener(MyTextWatcher(atkStatEdit))
            skillLvlEdit.addTextChangedListener(MyTextWatcher(skillLvlEdit))
            passive1Edit.addTextChangedListener(MyTextWatcher(passive1Edit))
            passive2Edit.addTextChangedListener(MyTextWatcher(passive2Edit))

            breakpointCb.setOnCheckedChangeListener { _, isChecked ->
                vm.onBreakpointCheckBoxChange(
                    isChecked
                )
            }

            criticalCb.setOnCheckedChangeListener { _, isChecked ->
                vm.onCriticalCheckBoxChange(
                    isChecked
                )
            }

            spBonusCb.setOnCheckedChangeListener { _, isChecked ->
                vm.onSpBonusCheckBoxChange(
                    isChecked
                )
            }

            atkTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

            atkTypeSpinner.onItemSelectedListener = MyOnItemSelectedListener()
            colorTypeSpinner.onItemSelectedListener = MyOnItemSelectedListener()
            gwBonusTypeSpinner.onItemSelectedListener = MyOnItemSelectedListener()

        }

    }

    inner class MyTextWatcher(val view: View) : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            binding.run {
                when (view) {
                    atkStatEdit -> vm.onAtkStatEditChange(s.toString().toIntOrNull())
                    skillLvlEdit -> vm.onSkillLevelEditChange(s.toString().toIntOrNull())
                    passive1Edit -> vm.onPassive1LevelEditChange(s.toString().toIntOrNull())
                    passive2Edit -> vm.onPassive2LevelEditChange(s.toString().toIntOrNull())
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    inner class MyOnItemSelectedListener : AdapterView.OnItemSelectedListener{
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            binding.apply {
                when (parent) {
                    atkTypeSpinner -> {
                        if (parent.getItemAtPosition(position).toString() != resources.getString(R.string.Sp) || currentUnit.spBonusType.isNullOrEmpty()) {
                            spBonusCb.visibility = View.INVISIBLE
                        }
                        else  {
                            spBonusCb.visibility = View.VISIBLE
                        }
                        if (parent.getItemAtPosition(position).toString() == resources.getString(R.string.NormalAttack)) {
                            skillLvlTitleTv.visibility = View.INVISIBLE
                            skillLvlEdit.visibility = View.INVISIBLE
                        } else {
                            skillLvlTitleTv.visibility = View.VISIBLE
                            skillLvlEdit.visibility = View.VISIBLE
                            skillLvlTitleTv.text = "${parent.getItemAtPosition(position)} level"
                        }
                        AttackType.fromString(parent.getItemAtPosition(position).toString())
                            ?.let { vm.onAtkTypeSpinnerChange(it) }
                    }
                    colorTypeSpinner -> ColorType.fromString(
                        parent.getItemAtPosition(position).toString()
                    )?.let { vm.onColorTypeSpinnerChange(it) }

                    gwBonusTypeSpinner -> GwBonusType.fromString(
                        parent.getItemAtPosition(position).toString()
                    )?.let { vm.onGwBonusTypeSpinnerChange(it) }
                }
            }
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
            binding.skillLvlTitleTv.visibility = View.INVISIBLE
        }

    }
}