package com.jovan.artscape.utils

import androidx.recyclerview.widget.DiffUtil

class GenericDiffCallback<T>(
    private val oldList: List<T>,
    private val newList: List<T>,
    private val itemIdProvider: (T) -> Any,
    private val itemContentProvider: (T) -> Any
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return itemIdProvider(oldList[oldItemPosition]) == itemIdProvider(newList[newItemPosition])
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return itemContentProvider(oldList[oldItemPosition]) == itemContentProvider(newList[newItemPosition])
    }
}