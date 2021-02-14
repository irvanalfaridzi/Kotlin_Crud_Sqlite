package com.example.menumakananminuman

class Menu {
    var id: Int? = null
    var nama: String? = null
    var harga: Double? = null

    constructor(id: Int, nama: String, harga: Double) {
        this.id = id
        this.nama = nama
        this.harga = harga
    }
}