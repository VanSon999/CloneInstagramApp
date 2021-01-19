package vanson.dev.instagramclone.utilites

import androidx.recyclerview.widget.DiffUtil

class SimpleCallback<T>(
    private val oldItems: List<T>,
    private val newItems: List<T>,
    private val getIdItem: (T) -> Any
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldItems.size
    override fun getNewListSize(): Int = newItems.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        getIdItem(oldItems[oldItemPosition]) == getIdItem(newItems[newItemPosition])

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldItems[oldItemPosition] == newItems[newItemPosition]
}