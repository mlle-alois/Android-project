package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.*

class ProductsListFragment : Fragment() { // ne JAMAIS passer de paramètres au constructeur

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.empty_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*if(products.isNotEmpty()) {
            view.findViewById<RecyclerView>(R.id.products_list).run {
                adapter = ProductAdapter(products,
                    findViewById<RecyclerView>(R.id.products_list).context,
                    listener = object : ItemClickListener {
                        override fun onItemClicked(position: Int) {
                            Log.d("ITEM_CLICKED", "Position $position")
                            //ProductsListFragmentDirections généré automatiquement grâce au lien dans app-nav
                            findNavController().navigate(
                                ProductsListFragmentDirections.actionProductsListFragmentToProductDetailsFragment(
                                    products[position]
                                )
                            )
                        }
                    })
                //layoutManager = GridLayoutManager(this@MainActivity, 3)

                //requireContext() correspond à this
                layoutManager = LinearLayoutManager(requireContext())
            }
        }*/

        view.findViewById<Button>(R.id.tracks_button).setOnClickListener {
            startActivityForResult(Intent("com.google.zxing.client.android.SCAN"), 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && data != null) {
            //data.getStringExtra("SCAN_FORMATS") //EAN_13
            val barcode_type = data.getStringExtra("SCAN_RESULT_FORMAT")
            val barcode_value = data.getStringExtra("SCAN_RESULT")

            val toast = Toast.makeText(
                requireContext(),
                "$barcode_type : $barcode_value",
                Toast.LENGTH_LONG
            );
            toast.show()
        }

    }
}

//configuration de la liste de produits
class ProductAdapter(
    val context: Context,
    val listener: ItemClickListener
) : RecyclerView.Adapter<HomeRankingsFragment.ListItemCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeRankingsFragment.ListItemCell {
        return HomeRankingsFragment.ListItemCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product, parent, false)
        )
    }

    override fun onBindViewHolder(listItemCell: HomeRankingsFragment.ListItemCell, position: Int) {

        /*listTrackCell.product_title.text =
            context.getString(R.string.product_title, product.name)
        listTrackCell.product_mark.text = context.getString(R.string.product_mark, product.mark)
        listTrackCell.product_nutriscore.text =
            context.getString(R.string.product_nutriscore, product.nutriscore)
        listTrackCell.product_calories.text =
            context.getString(R.string.product_calories, product.calories)

        listTrackCell.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }*/
    }

    override fun getItemCount(): Int {
        return 0
    }

}

//attribution des valeurs
class ListProductsCell(v: View) : RecyclerView.ViewHolder(v) {

    val product_title = v.findViewById<TextView>(R.id.titles)
    val product_mark = v.findViewById<TextView>(R.id.product_mark)
    val product_nutriscore = v.findViewById<TextView>(R.id.product_nutriscore)
    val product_calories = v.findViewById<TextView>(R.id.product_calories)

}

interface ItemClickListener {
    fun onItemClicked(position: Int);
}