package com.example.searchimage

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.searchimage.ImageAdapter.HeroViewHolder
import java.util.*

class ImageAdapter(
    var mCtx: Activity,
    var imageList: ArrayList<Data>?
) : RecyclerView.Adapter<HeroViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        val view =
            LayoutInflater.from(mCtx).inflate(R.layout.recyclerview_layout, parent, false)
        return HeroViewHolder(view)
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        val hero = imageList!![position]
        Glide.with(mCtx)
            .load(hero.link)
            .into(holder.imageView)
        holder.imageView.setOnClickListener {
            val intent = Intent(mCtx, ImageDescriptionActivity::class.java)
            intent.putExtra("data", hero)
            mCtx.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return if (null != imageList) {
            imageList!!.size
        } else 0
    }

    inner class HeroViewHolder(itemView: View) :
        ViewHolder(itemView) {
        var imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.imageView)
        }
    }

}