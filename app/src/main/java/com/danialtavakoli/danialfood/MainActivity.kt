package com.danialtavakoli.danialfood

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
import java.util.*

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), FoodAdapter.FoodEvents {
    private lateinit var binding: ActivityMainBinding
    private lateinit var myAdapter: FoodAdapter
    private lateinit var foodList: ArrayList<Food>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foodList = setFoodToArrayList()
        setAdapter()
        onAddNewFoodClicked()
        onSearchClicked()
    }

    private fun setFoodToArrayList(): ArrayList<Food> {
        return arrayListOf(
            Food(
                "Hamburger",
                "15",
                "3",
                "Isfahan, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food1.jpg",
                20,
                4.5f
            ),
            Food(
                "Grilled fish",
                "20",
                "2.1",
                "Tehran, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food2.jpg",
                10,
                4f
            ),
            Food(
                "Lasania",
                "40",
                "1.4",
                "Isfahan, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food3.jpg",
                30,
                2f
            ),
            Food(
                "pizza",
                "10",
                "2.5",
                "Zahedan, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food4.jpg",
                80,
                1.5f
            ),
            Food(
                "Sushi",
                "20",
                "3.2",
                "Mashhad, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food5.jpg",
                200,
                3f
            ),
            Food(
                "Roasted Fish",
                "40",
                "3.7",
                "Jolfa, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food6.jpg",
                50,
                3.5f
            ),
            Food(
                "Fried chicken",
                "70",
                "3.5",
                "NewYork, USA",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food7.jpg",
                70,
                2.5f
            ),
            Food(
                "Vegetable salad",
                "12",
                "3.6",
                "Berlin, Germany",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food8.jpg",
                40,
                4.5f
            ),
            Food(
                "Grilled chicken",
                "10",
                "3.7",
                "Beijing, China",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food9.jpg",
                15,
                5f
            ),
            Food(
                "Baryooni",
                "16",
                "10",
                "Ilam, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food10.jpg",
                28,
                4.5f
            ),
            Food(
                "Ghorme Sabzi",
                "11.5",
                "7.5",
                "Karaj, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food11.jpg",
                27,
                5f
            ),
            Food(
                "Rice",
                "12.5",
                "2.4",
                "Shiraz, Iran",
                "https://dunijet.ir/YaghootAndroidFiles/DuniFoodSimple/food12.jpg",
                35,
                2.5f
            )
        )
    }

    private fun setAdapter() {
        myAdapter = FoodAdapter(foodList.clone() as ArrayList<Food>, this)
        binding.recyclerMain.adapter = myAdapter
        binding.recyclerMain.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    private fun onAddNewFoodClicked() {
        binding.btnAddNewFood.setOnClickListener {
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
                        val randomForUrl = (0 until 12).random()
                        val urlPic = foodList[randomForUrl].urlImage
                        val newFood = Food(
                            txtName,
                            txtPrice,
                            txtDistance,
                            txtCity,
                            urlPic,
                            txtRatingNumber,
                            ratingBarStar
                        )
                        myAdapter.addFood(newFood)
                        binding.recyclerMain.scrollToPosition(0)
                        dialog.dismiss()
                    } else Toast.makeText(
                        this@MainActivity,
                        "Please fill all the values",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun onSearchClicked() {
        binding.edtSearch.addTextChangedListener { editTextInput ->
            if (editTextInput!!.isNotEmpty()) {
                val cloneList = foodList.clone() as ArrayList<Food>
                val filteredList =
                    cloneList.filter { foodItem -> foodItem.txtSubject.contains(editTextInput) }
                myAdapter.setData(ArrayList(filteredList))
            } else myAdapter.setData(foodList.clone() as ArrayList<Food>)
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
                        txtName,
                        txtPrice,
                        txtDistance,
                        txtCity,
                        food.urlImage,
                        food.numOfRating,
                        food.rating
                    )
                    dialog.dismiss()
                    myAdapter.updateFood(newFood, position)
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
        }
    }

}