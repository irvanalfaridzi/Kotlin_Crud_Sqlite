package com.example.menumakananminuman

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_menu.*

class MainActivity : AppCompatActivity() {
    private var listMenu = ArrayList<Menu>()
    lateinit var menusAdapterView: MenuAdapter
    var clicked: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadQueryAll()

        menusAdapterView = MenuAdapter(this, listMenu)
        lvMenu.adapter = menusAdapterView
        lvMenu.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
            Toast.makeText(this, "Click on " + listMenu[position].nama, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val searchView: SearchView = menu!!.findItem(R.id.searchMenu).actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                loadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        searchView.setOnCloseListener(object : SearchView.OnCloseListener {

            override fun onClose(): Boolean {
                loadQuery("%")
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addMenu -> {
                    var intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                }
                R.id.sorting -> {
                    if (clicked) {
                        Toast.makeText(applicationContext, "Coba", Toast.LENGTH_SHORT).show()
                        listMenu.sortByDescending { it.nama }
                        Log.d("Sorting", "onOptionsItemSelected: ${listMenu.joinToString()}")
                        menusAdapterView.notifyDataSetChanged();
                        clicked = false
                    } else {
                        Toast.makeText(applicationContext, "Coba", Toast.LENGTH_SHORT).show()
                        listMenu.sortBy { it.nama }
                        Log.d("Sorting", "onOptionsItemSelected: ${listMenu.joinToString()}")
                        menusAdapterView.notifyDataSetChanged();
                        clicked = true
                    }
                }
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        loadQueryAll()
    }

    fun loadQueryAll() {

        var dbManager = MenuDbManager(this)
        val cursor = dbManager.queryAll()

        listMenu.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val harga = cursor.getDouble(cursor.getColumnIndex("Harga"))
                val gambar = cursor.getString(cursor.getColumnIndex("Gambar"))

                listMenu.add(Menu(id, nama, harga, gambar))

            } while (cursor.moveToNext())
        }

        menusAdapterView = MenuAdapter(this, listMenu)
        lvMenu.adapter = menusAdapterView
    }

    fun loadQuery(nama: String) {

        var dbManager = MenuDbManager(this)
        val projections = arrayOf("Id", "Nama", "Harga", "Gambar")
        val selectionArgs = arrayOf(nama)
        val cursor = dbManager.query(projections, "Nama like ?", selectionArgs, "Nama")
        listMenu.clear()
        if (cursor.moveToFirst()) {

            do {
                val id = cursor.getInt(cursor.getColumnIndex("Id"))
                val nama = cursor.getString(cursor.getColumnIndex("Nama"))
                val harga = cursor.getDouble(cursor.getColumnIndex("Harga"))
                val gambar = cursor.getString(cursor.getColumnIndex("Gambar"))

                listMenu.add(Menu(id, nama, harga, gambar))

            } while (cursor.moveToNext())
        }

        var notesAdapter = MenuAdapter(this, listMenu)
        lvMenu.adapter = notesAdapter
    }

    inner class MenuAdapter : BaseAdapter {

        private var menuList = ArrayList<Menu>()
        private var context: Context? = null

        constructor(context: Context, notesList: ArrayList<Menu>) : super() {
            this.menuList = notesList
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {

            val view: View?
            val vh: ViewHolder

            if (convertView == null) {
                view = layoutInflater.inflate(R.layout.menu, parent, false)
                vh = ViewHolder(view)
                view.tag = vh
                Log.i("JSA", "set Tag for ViewHolder, position: " + position)
            } else {
                view = convertView
                vh = view.tag as ViewHolder
            }

            var mNote = menuList[position]

            vh.tvNama.text = mNote.nama
            vh.tvHarga.text = mNote.harga.toString()
            Glide
                .with(applicationContext)
                .load(mNote.gambar)
                .centerCrop()
                .placeholder(R.drawable.gambar)
                .into(vh.ivGambar);

            vh.ivEdit.setOnClickListener {
                updateMenu(mNote)
            }

            vh.ivDelete.setOnClickListener {
                var dbManager = MenuDbManager(this.context!!)
                val selectionArgs = arrayOf(mNote.id.toString())
                dbManager.delete("Id=?", selectionArgs)
                loadQueryAll()
            }

            return view
        }

        override fun getItem(position: Int): Any {
            return menuList[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return menuList.size
        }
    }

    private fun updateMenu(menu: Menu) {
        var intent = Intent(this, MenuActivity::class.java)
        intent.putExtra("MainActId", menu.id)
        intent.putExtra("MainActNama", menu.nama)
        intent.putExtra("MainActHarga", menu.harga)
        intent.putExtra("MainActGambar", menu.gambar)
        startActivity(intent)
    }

    private class ViewHolder(view: View?) {
        val tvNama: TextView
        val tvHarga: TextView
        val ivGambar: ImageView
        val ivEdit: ImageView
        val ivDelete: ImageView

        init {
            this.tvNama = view?.findViewById(R.id.tvNama) as TextView
            this.tvHarga = view?.findViewById(R.id.tvHarga) as TextView
            this.ivGambar = view?.findViewById(R.id.ivGambar) as ImageView
            this.ivEdit = view?.findViewById(R.id.ivEdit) as ImageView
            this.ivDelete = view?.findViewById(R.id.ivDelete) as ImageView
        }

        //  if you target API 26, you should change to:
//        init {
//            this.tvTitle = view?.findViewById<TextView>(R.id.tvTitle) as TextView
//            this.tvContent = view?.findViewById<TextView>(R.id.tvContent) as TextView
//        }
    }
}