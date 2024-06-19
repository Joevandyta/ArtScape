package com.jovan.artscape.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.jovan.artscape.databinding.ItemUserRowBinding
import com.jovan.artscape.remote.response.user.AllUserResponse
import com.jovan.artscape.utils.GenericDiffCallback

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
    private var list: List<AllUserResponse> = emptyList()
    private var onItemClickCallBack: OnItemClickCallBack? = null

    fun setOnItemClickCallBack(onItemClickCallBack: OnItemClickCallBack) {
        this.onItemClickCallBack = onItemClickCallBack
    }

    fun setUserList(user: List<AllUserResponse>) {
        val diffResult =
            DiffUtil.calculateDiff(
                GenericDiffCallback(
                    list,
                    user,
                    { it.id },
                    { it },
                ),
            )

        list = emptyList()
        list = user.reversed()
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val binding = ItemUserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
        private val binding: ItemUserRowBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: AllUserResponse) {
            binding.apply {
                tvUserName.text = user.name
                tvUserBio.text = user.description

                Glide
                    .with(itemView)
                    .load(user.picture)
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(ivUserPhoto)
            }
            binding.root.setOnClickListener {
                // TODO send ID when clicked
                onItemClickCallBack?.onItemClicked(user)
            }
        }
    }

    interface OnItemClickCallBack {
        fun onItemClicked(data: AllUserResponse)
    }
}
