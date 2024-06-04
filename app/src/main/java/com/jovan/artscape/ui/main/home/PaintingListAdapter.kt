package com.jovan.artscape.ui.main.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jovan.artscape.databinding.ItemGridArtBinding
import com.jovan.artscape.remote.response.user.UserResponse

class PaintingListAdapter: RecyclerView.Adapter<PaintingListAdapter.ViewHolder>()  {
    private val list = ArrayList<UserResponse>()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }
    fun setList(user: ArrayList<UserResponse>) {
        val diffResult = DiffUtil.calculateDiff(UserDiffCallback(list, user))
        list.clear()
        list.addAll(user)
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
        fun bind(user: UserResponse) {

            binding.apply {

            }
            binding.root.setOnClickListener {
                onItemClickCallBack?.onItemClicked()
            }
        }
    }

    private class UserDiffCallback(
        private val oldList: List<UserResponse>,
        private val newList: List<UserResponse>
    ) : DiffUtil.Callback() {
        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked()
    }
}