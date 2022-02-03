package com.azimmermannrosenthal.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.azimmermannrosenthal.myapplication.R
import com.azimmermannrosenthal.myapplication.objects.Track
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.squareup.picasso.Picasso

class TrackFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(
            R.layout.fragment_track,
            container,
            false
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as AppCompatActivity
        activity.findViewById<TextView>(R.id.title).visibility = GONE
        activity.findViewById<BottomNavigationView>(R.id.home_nav).visibility = GONE

        val track: Track = TrackFragmentArgs.fromBundle(requireArguments()).track

        view.findViewById<TextView>(R.id.track_title).text = track.strTrack
        Picasso.get().load(track.strTrackThumb)
            .into(view.findViewById<ImageView>(R.id.track_background_image))
        Picasso.get().load(track.strTrackThumb).into(view.findViewById<ImageView>(R.id.track_image))

        //TODO paroles

        view.findViewById<View>(R.id.return_button).setOnClickListener {
            findNavController().navigate(
                TrackFragmentDirections.actionTrackFragmentToTabRankings()
            )
        }
    }
}