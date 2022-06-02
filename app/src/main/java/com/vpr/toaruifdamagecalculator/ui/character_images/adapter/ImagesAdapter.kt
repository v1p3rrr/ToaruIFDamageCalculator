package com.vpr.toaruifdamagecalculator.ui.character_images.adapter

import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.vpr.toaruifdamagecalculator.R
import com.squareup.picasso.Picasso

class ImagesAdapter(
    private val context: Context,
    private val imageUrls: Array<String>,
    private val picasso: Picasso
) : PagerAdapter() {

    override fun getCount(): Int {
        return imageUrls.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val imageView = ImageView(context)
        if (context.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            picasso.load(imageUrls[position])
                .fit()
                .centerInside()
                .error(R.drawable.image_error_placeholder)
                .into(imageView)
        } else {
            picasso.load(imageUrls[position])
                .fit()
                .centerCrop()
                .error(R.drawable.image_error_placeholder)
                .into(imageView)
        }
        if (imageUrls.isEmpty()) {
            picasso.load(R.drawable.image_error_placeholder)
                .fit()
                .centerCrop()
                .into(imageView)
        }
        container.addView(imageView)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}