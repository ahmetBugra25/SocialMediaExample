package com.example.fotografpaylasma

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.fotografpaylasma.databinding.FragmentYuklemeBinding
import com.example.fotografpaylasma.databinding.RecyclerRowBinding
import com.squareup.picasso.Picasso

class PostAdapter(val postList :ArrayList<Post>) : RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PostViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.binding.recyclerEmailText.text=postList[position].email
        holder.binding.recyclerCommentText.text =postList[position].commet
        Picasso.get().load(postList[position].downloadUrl).into(holder.binding.recyclerImageView)

    }
}