package com.example.cryptocurrencytrackingapprxjava

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptocurrencytrackingapprxjava.databinding.ActivityMainBinding
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val BASE_URL = "https://api.nomics.com/v1/"
    private var models: ArrayList<CryptoModel>? = null

    private var compositeDisposable: CompositeDisposable? = null

    private lateinit var recyclerView: RecyclerView

    lateinit var adapter: RecyclerViewAdapter
    private var searchEnable: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        compositeDisposable = CompositeDisposable()

        recyclerView = findViewById(R.id.recycleView)

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager

        loadData()

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (searchEnable)
                    adapter.filter.filter(newText)
                return false
            }

        })
    }

    private fun loadData() {

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build().create(CryptoAPI::class.java)


        compositeDisposable?.add(
            retrofit.getData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )

    }

    private fun handleResponse(cryptoList: List<CryptoModel>) {
        models = ArrayList(cryptoList)

        models?.let {
            adapter = RecyclerViewAdapter(it)
            recyclerView.adapter = adapter
            searchEnable = true
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable?.clear()
    }
}