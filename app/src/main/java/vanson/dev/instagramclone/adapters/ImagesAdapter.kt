package vanson.dev.instagramclone.adapters

import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import vanson.dev.instagramclone.R
import vanson.dev.instagramclone.controllers.common.loadImage
import vanson.dev.instagramclone.utilites.SimpleCallback

class ImagesAdapter : RecyclerView.Adapter<ImagesAdapter.ViewHolder>() {
    private var images = listOf<String>()

    class ViewHolder(val image: ImageView) : RecyclerView.ViewHolder(image)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val imageView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false) as ImageView
        return ViewHolder(imageView)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.image.loadImage(images[position])
    }

    fun updateImages(newImages: List<String>){
        val diffResult = DiffUtil.calculateDiff(SimpleCallback(images, newImages){it})
        this.images = newImages
        diffResult.dispatchUpdatesTo(this)
    }
//    private fun ImageView.loadImage(urlImage: String){
//        GlideApp.with(this).load(urlImage).centerCrop().into(this)
//    }
}