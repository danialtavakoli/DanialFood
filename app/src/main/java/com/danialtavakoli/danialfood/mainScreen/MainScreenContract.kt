package com.danialtavakoli.danialfood.mainScreen

import com.danialtavakoli.danialfood.model.Food
import com.danialtavakoli.danialfood.utils.BasePresenter
import com.danialtavakoli.danialfood.utils.BaseView

interface MainScreenContract {
    interface Presenter : BasePresenter<View> {
        fun firstRun()
        fun onSearchFood(filter: String)
        fun onAddNewFoodClicked(food: Food)
        fun onDeleteAllClicked()
        fun onUpdateFood(food: Food, position: Int)
        fun onDeleteFood(food: Food, position: Int)
    }

    interface View : BaseView {
        fun showFoods(data: List<Food>)
        fun refreshFoods(data: List<Food>)
        fun addNewFood(newFood: Food)
        fun deleteFood(oldFood: Food, position: Int)
        fun updateFood(editingFood: Food, position: Int)
    }
}