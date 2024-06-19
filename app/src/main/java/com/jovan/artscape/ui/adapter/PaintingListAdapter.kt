package com.jovan.artscape.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jovan.artscape.databinding.ItemGridArtBinding
import com.jovan.artscape.remote.response.painting.AllPaintingResponse
import com.jovan.artscape.utils.GenericDiffCallback

class PaintingListAdapter : RecyclerView.Adapter<PaintingListAdapter.ViewHolder>() {
    private var list: List<AllPaintingResponse> = emptyList()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setUserPaintingList(
        painting: List<AllPaintingResponse>,
        artistId: String,
    ) {
        val filteredList =
            painting.filter { it.artistId == artistId } // Assuming `artistId` is a property of `AllPaintingResponse`

        /*val sortedList =
            if (userInterests != null) {
                filteredList.sortedWith(compareByDescending<AllPaintingResponse> { it.genre in userInterests }.thenBy { it.id })
            } else {
                filteredList.sortedBy { it.id }
            }*/

        val diffResult =
            DiffUtil.calculateDiff(
                GenericDiffCallback(
                    list,
                    filteredList,
                    { it.id },
                    { it },
                ),
            )

        list = filteredList.reversed()
        diffResult.dispatchUpdatesTo(this)
    }

    fun setHomePaintingList(painting: List<AllPaintingResponse>) {
        val diffResult =
            DiffUtil.calculateDiff(
                GenericDiffCallback(
                    list,
                    painting,
                    { it.id },
                    { it },
                ),
            )

        list = emptyList()
        list = painting.reversed()
        diffResult.dispatchUpdatesTo(this)
    }

    fun setRecomendedPaintingList(
        paintingList: List<AllPaintingResponse>,
        userInterests: List<String>,
    ) {
        linkedMapOf<String, AllPaintingResponse>()

        val sortedList =
            paintingList.sortedWith(compareByDescending<AllPaintingResponse> { it.genre in userInterests }.thenBy { it.id })
        val diffResult =
            DiffUtil.calculateDiff(
                GenericDiffCallback(
                    list,
                    sortedList,
                    { it.id },
                    { it },
                ),
            )

        list = sortedList
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemGridArtBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int,
    ) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    inner class ViewHolder(
        private val binding: ItemGridArtBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AllPaintingResponse) {
            binding.apply {
                tvItemName.text = item.title
                tvItemPrice.text = "Rp${item.price}"
                Glide
                    .with(itemView)
                    .load(item.photo)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivItemPhoto)
            }
            binding.root.setOnClickListener {
                // TODO send ID when clicked
                onItemClickCallBack?.onItemClicked(item.id)
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(paintingId: String)
//        fun onItemClicked()
    }
}
