package com.example.toaruifdamagecalculator.ui.character_images

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsetsController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.toaruifdamagecalculator.databinding.FragmentUnitImagesBinding
import com.example.toaruifdamagecalculator.ui.character_images.adapter.ImagesAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class UnitImagesFragment : Fragment() {

    private val vm: UnitImagesViewModel by viewModels()

    private lateinit var binding: FragmentUnitImagesBinding

    @Inject
    lateinit var picasso: Picasso

    private val safeArgs: UnitImagesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUnitImagesBinding.inflate(inflater, container, false)
        //hideStatusBar() //todo hide status bar, change bottom bar color to black
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ImagesAdapter(requireContext(), safeArgs.imageUrls, picasso)
        binding.imagesViewPager.adapter = adapter
    }

    fun hideStatusBar(){
        requireActivity().window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val currentApiVersion = Build.VERSION.SDK_INT
        val flags = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            requireActivity().window.decorView.systemUiVisibility = flags
            val decorView = requireActivity().window.decorView
            decorView.setOnSystemUiVisibilityChangeListener { visibility ->
                if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                    decorView.systemUiVisibility = flags
                }
            }
        }
    }

}