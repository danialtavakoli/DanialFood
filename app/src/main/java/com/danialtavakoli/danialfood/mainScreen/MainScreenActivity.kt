package com.danialtavakoli.danialfood.mainScreen

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danialtavakoli.danialfood.databinding.ActivityMainBinding
import com.danialtavakoli.danialfood.databinding.DialogAddNewItemBinding
import com.danialtavakoli.danialfood.databinding.DialogDeletItemBinding
import com.danialtavakoli.danialfood.databinding.DialogUpdateItemBinding
import com.danialtavakoli.danialfood.model.Food
import com.danialtavakoli.danialfood.model.MyDatabase
import com.danialtavakoli.danialfood.utils.BASE_URL_IMAGE
import com.danialtavakoli.danialfood.utils.showToast
import java.util.*

@Suppress("UNCHECKED_CAST")
class MainScreenActivity : AppCompatActivity(), FoodAdapter.FoodEvents, MainScreenContract.View {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainAdapter: FoodAdapter
    private lateinit var presenter: MainScreenContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val foodDao = MyDatabase.getDatabase(this).foodDao
        presenter = MainScreenPresenter(foodDao)
        checkFirstRun()
        presenter.onAttach(this)
        binding.btnRemoveAllFoods.setOnClickListener { presenter.onDeleteAllClicked() }
        binding.btnAddNewFood.setOnClickListener { addNewFood() }
        binding.edtSearch.addTextChangedListener { input -> presenter.onSearchFood(input.toString()) }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDetach()
    }

    private fun checkFirstRun() {
        val sharedPreferences =
            getSharedPreferences("DanialSharedPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            presenter.firstRun()
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }
    }

    private fun addNewFood() {
        val dialog = AlertDialog.Builder(this).create()
        val dialogBinding = DialogAddNewItemBinding.inflate(layoutInflater)
        dialog.setView(dialogBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        with(dialogBinding) {
            dialogBtnDone.setOnClickListener {
                if (dialogEdtNameFood.length() > 0 &&
                    dialogEdtFoodPrice.length() > 0 &&
                    dialogEdtFoodDistance.length() > 0 &&
                    dialogEdtFoodCity.length() > 0
                ) {
                    val newFood = createNewFood(dialogBinding)
                    presenter.onAddNewFoodClicked(newFood)
                    dialog.dismiss()
                } else showToast("Please fill all the values")
            }
        }
    }

    private fun createNewFood(dialogBinding: DialogAddNewItemBinding): Food {
        with(dialogBinding) {
            val txtName = dialogEdtNameFood.text.toString()
            val txtPrice = dialogEdtFoodPrice.text.toString()
            val txtDistance = dialogEdtFoodDistance.text.toString()
            val txtCity = dialogEdtFoodCity.text.toString()
            val txtRatingNumber = (1..150).random()
            val min = 0f
            val max = 5f
            val ratingBarStar = min + Random().nextFloat() * (max - min)
            val randomForUrl = (1 until 12).random()
            val urlPic = "$BASE_URL_IMAGE$randomForUrl.jpg"
            return Food(
                txtSubject = txtName,
                txtPrice = txtPrice,
                txtDistance = txtDistance,
                txtCity = txtCity,
                urlImage = urlPic,
                numOfRating = txtRatingNumber,
                rating = ratingBarStar
            )
        }
    }

    override fun onFoodClicked(food: Food, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogUpdateBinding = DialogUpdateItemBinding.inflate(layoutInflater)
        dialog.setView(dialogUpdateBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        with(dialogUpdateBinding) {
            dialogEdtNameFood.setText(food.txtSubject)
            dialogEdtFoodCity.setText(food.txtCity)
            dialogEdtFoodPrice.setText(food.txtPrice)
            dialogEdtFoodDistance.setText(food.txtDistance)
            dialogUpdateBtnCancel.setOnClickListener { dialog.dismiss() }
            dialogUpdateBtnDone.setOnClickListener {
                if (dialogEdtNameFood.length() > 0 &&
                    dialogEdtFoodPrice.length() > 0 &&
                    dialogEdtFoodDistance.length() > 0 &&
                    dialogEdtFoodCity.length() > 0
                ) {
                    val txtName = dialogEdtNameFood.text.toString()
                    val txtPrice = dialogEdtFoodPrice.text.toString()
                    val txtDistance = dialogEdtFoodDistance.text.toString()
                    val txtCity = dialogEdtFoodCity.text.toString()
                    val newFood = Food(
                        id = food.id,
                        txtSubject = txtName,
                        txtPrice = txtPrice,
                        txtDistance = txtDistance,
                        txtCity = txtCity,
                        urlImage = food.urlImage,
                        numOfRating = food.numOfRating,
                        rating = food.rating
                    )
                    presenter.onUpdateFood(newFood, position)
                    dialog.dismiss()
                } else showToast("Please fill all the values")
            }
        }
    }

    override fun onFoodLongClicked(food: Food, position: Int) {
        val dialog = AlertDialog.Builder(this).create()
        val dialogDeleteBinding = DialogDeletItemBinding.inflate(layoutInflater)
        dialog.setView(dialogDeleteBinding.root)
        dialog.setCancelable(true)
        dialog.show()
        dialogDeleteBinding.dialogBtnDeleteCancel.setOnClickListener { dialog.dismiss() }
        dialogDeleteBinding.dialogBtnDeleteYes.setOnClickListener {
            presenter.onDeleteFood(food, position)
            dialog.dismiss()
        }
    }

    override fun showFoods(data: List<Food>) {
        mainAdapter = FoodAdapter(ArrayList(data), this)
        binding.recyclerMain.adapter = mainAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun refreshFoods(data: List<Food>) {
        mainAdapter.setData(ArrayList(data))
    }

    override fun addNewFood(newFood: Food) {
        mainAdapter.addFood(newFood)
    }

    override fun deleteFood(oldFood: Food, position: Int) {
        mainAdapter.removeFood(oldFood, position)
    }

    override fun updateFood(editingFood: Food, position: Int) {
        mainAdapter.updateFood(editingFood, position)
    }
}