package com.example.tubearhai.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.tubearhai.databinding.BearlayoutBinding
import com.example.tubearhai.databinding.BottomSheetDialogBinding
import com.example.tubearhai.model.BearDataItem
import com.example.tubearhai.utilities.volumeLitres
import com.google.android.material.bottomsheet.BottomSheetDialog

class BearAdapter : PagingDataAdapter<BearDataItem, BearAdapter.ViewHolderBear>(differCallback){
    lateinit var context: Context
    class ViewHolderBear(val bearLayout : BearlayoutBinding) : ViewHolder(bearLayout.root)
    companion object {
        val differCallback = object : DiffUtil.ItemCallback<BearDataItem>() {
            override fun areItemsTheSame(oldItem: BearDataItem, newItem: BearDataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: BearDataItem, newItem: BearDataItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolderBear, position: Int) {
        val currentItem = getItem(position)
        holder.bearLayout.apply {
            name.text = currentItem?.name
            tagline.text = currentItem?.tagline
            Glide.with(context).load(currentItem?.image_url).into(imageBear)
        }

        holder.bearLayout.shareIcon.setOnClickListener {
            val message = "Name: ${currentItem?.name}\n TagLine: ${currentItem?.tagline}"
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.`package` = "com.whatsapp"
            context.startActivity(Intent.createChooser(shareIntent, "Share with"))
        }

        holder.bearLayout.cardView.setOnClickListener {
            val  bottomSheetDialog = BottomSheetDialog(context)
            val view = BottomSheetDialogBinding.inflate(LayoutInflater.from(context))
            bottomSheetDialog.setContentView(view.root)
            Glide.with(context).load(currentItem?.image_url).into(view.imageBear)
            view.apply {
                fullName.text = "Name: "+currentItem?.name
                tagLineF.text = "TagLine: "+currentItem?.tagline
                brewersTips.text = "BrewersTips: "+currentItem?.brewers_tips
                description.text = "Description: "+currentItem?.description
                firstBrewed.text = "First Brewed: "+currentItem?.first_brewed
                valume.text = "Volume: "+currentItem?.volume?.volumeLitres()
            }
            bottomSheetDialog.show()

            view.cross.setOnClickListener {
                bottomSheetDialog.dismiss()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderBear {
        context = parent.context
        return ViewHolderBear(BearlayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
}
