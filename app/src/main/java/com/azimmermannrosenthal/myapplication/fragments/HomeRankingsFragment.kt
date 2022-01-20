package com.azimmermannrosenthal.myapplication.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.azimmermannrosenthal.myapplication.*
import kotlinx.android.synthetic.main.fragment_home_rankings.view.*

class HomeRankingsFragment: Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_home_rankings,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.tab_rankings)

        //val product = ProductDetailsFragmentArgs.fromBundle(requireParentFragment().requireParentFragment().requireArguments()).product
        val product = Product(
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
        )

        view.findViewById<TextView>(R.id.product_title).text = getString(R.string.product_title, product.name)
        view.findViewById<TextView>(R.id.product_mark).text = getString(R.string.product_mark, product.mark)

        view.findViewById<TextView>(R.id.product_barcode).setBoldValue(
            getString(R.string.product_barcode, product.barcode)
        )
        view.product_quantity.setBoldValue( //raccourci possible grâce à 'kotlin-android-extensions'(build.gradle)
            getString(R.string.product_quantity, product.quantity)
        )
        view.findViewById<TextView>(R.id.product_sold_in).setBoldValue(
            getString(R.string.product_sold_in, product.sold_in)
        )
        view.findViewById<TextView>(R.id.product_ingredients).setBoldValue(
            getString(R.string.product_ingredients, product.ingredients)
        )
        view.findViewById<TextView>(R.id.product_allergenic_substances).setBoldValue(
            getString(R.string.product_allergenic_substances, product.allegenics_substances)
        )
        view.findViewById<TextView>(R.id.product_additives).setBoldValue(
            getString(R.string.product_additives, product.additives)
        )
    }

    //TextView --> applique la méthode de façon globale au TextView
    fun TextView.setBoldValue(value: String) {

        //récupérer l'index du caractère ':'
        val index = value.indexOf(':')

        //passe une partie de la chaine en gras
        val spannable = SpannableString(value)
        spannable.setSpan(StyleSpan(Typeface.BOLD), 0, index, 0) //de 0 à index

        text = spannable
    }
}