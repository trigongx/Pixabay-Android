package com.geeks.pixabay

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.geeks.pixabay.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var adapter = ImageAdapter(arrayListOf())
    var page = 1
    var isAddToList = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter
        initClickers()
    }

    private fun initClickers() {
        with(binding) {
            btnSearch.setOnClickListener {
                isAddToList = false
                page = 1
                getImage()
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                    // Получаем LayoutManager для RecyclerView
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager?

                    // Получаем количество элементов в адаптере
                    val itemCount = layoutManager!!.itemCount

                    // Получаем позицию последнего полностью видимого элемента
                    val lastVisibleItemPosition =
                        layoutManager.findLastCompletelyVisibleItemPosition()

                    // Проверяем, достиг ли пользователь конца списка
                    if (lastVisibleItemPosition == itemCount - 1) {
                        // Ваш код для обработки прокрутки до конца списка здесь
                        // Например, вы можете загрузить дополнительные данные или отобразить сообщение о достижении конца списка.

                        isAddToList = true
                        ++page
                        getImage()
                    }
                }
            })
        }
    }

    private fun ActivityMainBinding.getImage() {
        RetrofitService.api.getPictures(keyWord = etSearchWard.text.toString(), page = page)
            .enqueue(object : Callback<PixaBayModel> {
                override fun onResponse(
                    call: Call<PixaBayModel>,
                    response: Response<PixaBayModel>
                ) {
                    if (response.isSuccessful) {
                        if (!isAddToList) {
                            adapter.list.clear()
                        }
                        adapter.list.addAll(response.body()?.hits!!)
                        adapter.notifyDataSetChanged();
                    }
                }

                override fun onFailure(call: Call<PixaBayModel>, t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_SHORT).show()
                    Log.e("kiber", t.message.toString())
                }
            })
    }
}