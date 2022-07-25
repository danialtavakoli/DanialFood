package com.danialtavakoli.danialfood

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danialtavakoli.danialfood.databinding.ActivityMainBinding
import com.danialtavakoli.danialfood.databinding.DialogAddNewItemBinding
import com.danialtavakoli.danialfood.databinding.DialogDeletItemBinding
import com.danialtavakoli.danialfood.databinding.DialogUpdateItemBinding
import com.danialtavakoli.danialfood.room.Food
import com.danialtavakoli.danialfood.room.FoodDao
import com.danialtavakoli.danialfood.room.MyDatabase
import java.util.*

const val BASE_URL_IMAGE = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food"

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {

    private lateinit var binding: ActivityMainBinding
    private lateinit var foodDao: FoodDao
    private lateinit var myAdapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foodDao = MyDatabase.getDatabase(this).foodDao
        checkFirstRun()
        showAllData()
        binding.btnRemoveAllFoods.setOnClickListener { removeAllData() }
        binding.btnAddNewFood.setOnClickListener { addNewFood() }
        binding.edtSearch.addTextChangedListener { input -> searchOnFoods(input.toString()) }
    }

    private fun checkFirstRun() {
        val sharedPreferences =
            getSharedPreferences("DanialSharedPreferences", Context.MODE_PRIVATE)
        if (sharedPreferences.getBoolean("first_run", true)) {
            foodDao.insertAllFoods(setListFood())
            sharedPreferences.edit().putBoolean("first_run", false).apply()
        }
    }

    private fun setListFood(): List<Food> {
        return listOf(
            Food(
                txtSubject = "Hamburger",
                txtPrice = "15",
                txtDistance = "3",
                txtCity = "Isfahan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
                numOfRating = 20,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Grilled fish",
                txtPrice = "20",
                txtDistance = "2.1",
                txtCity = "Tehran, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
                numOfRating = 10,
                rating = 4f
            ),
            Food(
                txtSubject = "Lasania",
                txtPrice = "40",
                txtDistance = "1.4",
                txtCity = "Isfahan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
                numOfRating = 30,
                rating = 2f
            ),
            Food(
                txtSubject = "pizza",
                txtPrice = "10",
                txtDistance = "2.5",
                txtCity = "Zahedan, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
                numOfRating = 80,
                rating = 1.5f
            ),
            Food(
                txtSubject = "Sushi",
                txtPrice = "20",
                txtDistance = "3.2",
                txtCity = "Mashhad, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
                numOfRating = 200,
                rating = 3f
            ),
            Food(
                txtSubject = "Roasted Fish",
                txtPrice = "40",
                txtDistance = "3.7",
                txtCity = "Jolfa, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
                numOfRating = 50,
                rating = 3.5f
            ),
            Food(
                txtSubject = "Fried chicken",
                txtPrice = "70",
                txtDistance = "3.5",
                txtCity = "NewYork, USA",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
                numOfRating = 70,
                rating = 2.5f
            ),
            Food(
                txtSubject = "Vegetable salad",
                txtPrice = "12",
                txtDistance = "3.6",
                txtCity = "Berlin, Germany",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
                numOfRating = 40,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Grilled chicken",
                txtPrice = "10",
                txtDistance = "3.7",
                txtCity = "Beijing, China",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
                numOfRating = 15,
                rating = 5f
            ),
            Food(
                txtSubject = "Baryooni",
                txtPrice = "16",
                txtDistance = "10",
                txtCity = "Ilam, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
                numOfRating = 28,
                rating = 4.5f
            ),
            Food(
                txtSubject = "Ghorme Sabzi",
                txtPrice = "11.5",
                txtDistance = "7.5",
                txtCity = "Karaj, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
                numOfRating = 27,
                rating = 5f
            ),
            Food(
                txtSubject = "Rice",
                txtPrice = "12.5",
                txtDistance = "2.4",
                txtCity = "Shiraz, Iran",
                urlImage = "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
                numOfRating = 35,
                rating = 2.5f
            ),
        )
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
                    val newFood = Food(
                        txtSubject = txtName,
                        txtPrice = txtPrice,
                        txtDistance = txtDistance,
                        txtCity = txtCity,
                        urlImage = urlPic,
                        numOfRating = txtRatingNumber,
                        rating = ratingBarStar
                    )
                    myAdapter.addFood(newFood)
                    foodDao.insertOrUpdate(newFood)
                    dialog.dismiss()
                    binding.recyclerMain.scrollToPosition(0)
                } else Toast.makeText(
                    this@MainActivity,
                    "Please fill all the values",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun removeAllData() {
        foodDao.deleteAllFoods()
        showAllData()
    }

    private fun searchOnFoods(input: String) {
        if (input.isNotEmpty()) {
            val searchData = foodDao.searchFoods(input)
            myAdapter.setData(ArrayList(searchData))
        } else {
            val data = foodDao.getAllFood()
            myAdapter.setData(ArrayList(data))
        }
    }

    private fun showAllData() {
        val foodData = foodDao.getAllFood()
        myAdapter = FoodAdapter(ArrayList(foodData), this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
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
                    dialog.dismiss()
                    myAdapter.updateFood(newFood, position)
                    foodDao.insertOrUpdate(newFood)
                } else Toast.makeText(
                    this@MainActivity,
                    "Please fill all the values",
                    Toast.LENGTH_SHORT
                ).show()
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
            dialog.dismiss()
            myAdapter.removeFood(food, position)
            foodDao.deleteFood(food)
        }
    }

}