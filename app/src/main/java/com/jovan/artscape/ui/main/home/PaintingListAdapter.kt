package com.jovan.artscape.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jovan.artscape.databinding.ItemGridArtBinding
import com.jovan.artscape.remote.response.painting.PaintingDetails
import com.jovan.artscape.utils.GenericDiffCallback

class PaintingListAdapter: RecyclerView.Adapter<PaintingListAdapter.ViewHolder>()  {
    private var list: List<PaintingDetails> = emptyList()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }
    fun setList(user: List<PaintingDetails>) {
        val diffResult = DiffUtil.calculateDiff(
            GenericDiffCallback(list, user, { it.id },
            { it })
        )
        list = user
        diffResult.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGridArtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
    inner class ViewHolder(private val binding: ItemGridArtBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: PaintingDetails) {

            binding.apply {

                tvItemName.text = user.title
                tvItemDescription.text = user.description

                Glide.with(itemView)
                    .load(user.photo)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivItemPhoto)
            }
            binding.root.setOnClickListener {
                //TODO send ID when clicked
                onItemClickCallBack?.onItemClicked()
            }
        }
    }

/*    private class UserDiffCallback(
        private val oldList: List<PaintingResponse>,
        private val newList: List<PaintingResponse>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }*/

    interface OnItemClickCallBack {
        fun onItemClicked()
    }

}


