package com.example.cryptocurrencytrackingapprxjava


import io.reactivex.Observable
import retrofit2.http.GET

interface CryptoAPI {


    // @GET("prices?key=8112b594d595e1e9d5136ccbcc0e03c5fb7aabd6")
    @GET("currencies/ticker?key=8112b594d595e1e9d5136ccbcc0e03c5fb7aabd6")
    fun getData(): Observable<List<CryptoModel>>

}