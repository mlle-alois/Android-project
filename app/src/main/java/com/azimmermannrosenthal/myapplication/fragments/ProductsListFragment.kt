package com.azimmermannrosenthal.myapplication.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.azimmermannrosenthal.myapplication.*

class ProductsListFragment : Fragment() { // ne JAMAIS passer de paramètres au constructeur

    var products = listOf<Product>();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //TODO enlever les produits en commentaire pour voir la liste ou les remettre pour voir la page vide
        products = listOf<Product>(Product(
                "Petits pois et carottes",
                "Cassegrain",
                "3083680085304",
                Nutriscore.A,
                "static.openfoodfacts.org/images/products/308/368/008/5304/front_fr.7.400.jpg",
                "400g (280g net égoutté)",
                "France, Japon, Suisse",
                "Petits pois 66%, eau, garniture 2,8% (salade, oignon grelot), sucre, sel, arôme naturel",
                "Aucune",
                "Aucun",
                "234 kCal/part",
                NutritionFacts(
                    NutritionFactsItem("kj", 293.0, 0.0),
                    NutritionFactsItem("g", 0.8, 0.0),
                    NutritionFactsItem("g", 0.1, 0.0),
                    NutritionFactsItem("g", 8.4, 0.0),
                    NutritionFactsItem("g", 5.2, 0.0),
                    NutritionFactsItem("g", 5.2, 0.0),
                    NutritionFactsItem("g", 4.8, 0.0),
                    NutritionFactsItem("g", 0.75, 0.0),
                    NutritionFactsItem("g", 0.295, 0.0)
                )
            ),
            Product(
                "Salade au poulet", "Gerblé", "2562159856301",
                Nutriscore.A, "", "400g", "France, Belgique, Espagne",
                "Salade roquette, poulet mariné (20%), tomates confites, sauce vinaigrette (vinaigre, huile), sel, poivre",
                "Tomates", "Aucun", "215 kCal/part",
                NutritionFacts(
                    NutritionFactsItem("kj", 293.0, 0.0),
                    NutritionFactsItem("g", 0.8, 0.0),
                    NutritionFactsItem("g", 0.1, 0.0),
                    NutritionFactsItem("g", 8.4, 0.0),
                    NutritionFactsItem("g", 5.2, 0.0),
                    NutritionFactsItem("g", 5.2, 0.0),
                    NutritionFactsItem("g", 4.8, 0.0),
                    NutritionFactsItem("g", 1.1, 0.0),
                    NutritionFactsItem("g", 0.295, 0.0)
                )
            ),
            Product(
                "Hachi parmentier de boeuf", "Findus", "5489301524782",
                Nutriscore.C, "", "1000g", "France",
                "Pommes de terre, haché de boeuf (15% MG), huile, sel, poivre",
                "Aucune", "Aucun", "356",
                NutritionFacts(
                    NutritionFactsItem("kj", 293.0, 0.0),
                    NutritionFactsItem("g", 15.2, 0.0),
                    NutritionFactsItem("g", 0.1, 0.0),
                    NutritionFactsItem("g", 8.4, 0.0),
                    NutritionFactsItem("g", 5.2, 0.0),
                    NutritionFactsItem("g", 5.2, 0.0),
                    NutritionFactsItem("g", 4.8, 0.0),
                    NutritionFactsItem("g", 0.75, 0.0),
                    NutritionFactsItem("g", 0.295, 0.0)
                )
            )
        );

        if(products.isNotEmpty()) {
            return inflater.inflate(
                R.layout.products_list,
                container,
                false
            )
        }
        return inflater.inflate(
            R.layout.empty_list,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(products.isNotEmpty()) {
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
        }

        view.findViewById<Button>(R.id.products_start_scan).setOnClickListener {
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
    val products: List<Product>,
    val context: Context,
    val listener: ItemClickListener
) : RecyclerView.Adapter<ListProductsCell>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListProductsCell {
        return ListProductsCell(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.product, parent, false)
        )
    }

    override fun onBindViewHolder(listProductsCell: ListProductsCell, position: Int) {

        val product = products.get(position)

        listProductsCell.product_title.text =
            context.getString(R.string.product_title, product.name)
        listProductsCell.product_mark.text = context.getString(R.string.product_mark, product.mark)
        listProductsCell.product_nutriscore.text =
            context.getString(R.string.product_nutriscore, product.nutriscore)
        listProductsCell.product_calories.text =
            context.getString(R.string.product_calories, product.calories)

        listProductsCell.itemView.setOnClickListener {
            listener.onItemClicked(position)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

}

//attribution des valeurs
class ListProductsCell(v: View) : RecyclerView.ViewHolder(v) {

    val product_title = v.findViewById<TextView>(R.id.product_title)
    val product_mark = v.findViewById<TextView>(R.id.product_mark)
    val product_nutriscore = v.findViewById<TextView>(R.id.product_nutriscore)
    val product_calories = v.findViewById<TextView>(R.id.product_calories)

}

interface ItemClickListener {
    fun onItemClicked(position: Int);
}