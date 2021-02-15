package com.example.menumakananminuman

class Menu {
    var id: Int? = null
    var nama: String? = null
    var harga: Double? = null
    var gambar: String? = null

    constructor(id: Int, nama: String, harga: Double, gambar: String) {
        this.id = id
        this.nama = nama
        this.harga = harga
        this.gambar = gambar
    }
}