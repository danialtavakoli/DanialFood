package com.danialtavakoli.danialfood.mainScreen

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.danialtavakoli.danialfood.databinding.ItemFoodBinding
import com.danialtavakoli.danialfood.model.Food
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

class FoodAdapter(private val data: ArrayList<Food>, private val foodEvents: FoodEvents) :
    RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    inner class FoodViewHolder(private val binding: ItemFoodBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindData(position: Int) {
            with(binding) {
                itemTxtSubject.text = data[position].txtSubject
                itemTxtCity.text = data[position].txtCity
                itemTxtPrice.text = "$" + data[position].txtPrice + " vip"
                itemTxtDistance.text = data[position].txtDistance + " miles from you"
                itemRatingMain.rating = data[position].rating
                itemTxtRating.text = "(" + data[position].numOfRating.toString() + " Ratings)"
                Glide.with(binding.root.context).load(data[position].urlImage)
                    .transform(RoundedCornersTransformation(16, 4)).into(itemImgMain)
            }
            itemView.setOnClickListener {
                foodEvents.onFoodClicked(
                    data[adapterPosition],
                    adapterPosition
                )
            }
            itemView.setOnLongClickListener {
                foodEvents.onFoodLongClicked(data[adapterPosition], adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val binding = ItemFoodBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        holder.bindData(position)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    interface FoodEvents {
        fun onFoodClicked(food: Food, position: Int)
        fun onFoodLongClicked(food: Food, position: Int)
    }

    fun addFood(newFood: Food) {
        data.add(0, newFood)
        notifyItemInserted(0)
    }

    fun removeFood(oldFood: Food, oldPosition: Int) {
        data.remove(oldFood)
        notifyItemRemoved(oldPosition)
    }

    fun updateFood(newFood: Food, position: Int) {
        data[position] = newFood
        notifyItemChanged(position)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: ArrayList<Food>) {
        data.clear()
        data.addAll(newList)
        notifyDataSetChanged()
    }

}