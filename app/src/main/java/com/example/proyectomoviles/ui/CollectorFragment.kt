package com.example.proyectomoviles.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.proyectomoviles.adapters.CollectorSectionsPagerAdapter
import com.example.proyectomoviles.databinding.CollectorFragmentBinding
import com.example.proyectomoviles.models.Collector
import com.example.proyectomoviles.viewmodels.CollectorViewModel
import com.google.android.material.tabs.TabLayoutMediator

class CollectorFragment : Fragment() {
    private var _binding: CollectorFragmentBinding? = null
    private val binding get() = _binding!!
    private val args: CollectorFragmentArgs by navArgs()

    private lateinit var sectionsCollectionAdapter: CollectorSectionsPagerAdapter
    private lateinit var viewPager: ViewPager2

    private lateinit var viewModel: CollectorViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CollectorFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sectionsCollectionAdapter = CollectorSectionsPagerAdapter(this, args.collectorId)
        viewPager = binding.pager
        viewPager.adapter = sectionsCollectionAdapter

        val tabNames = arrayListOf<String>("Álbumes", "Artistas favoritos", "Comentarios")
        val tabLayout = binding.tabLayout
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabNames[position]
        }.attach()

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        viewModel = ViewModelProvider(this, CollectorViewModel.Factory(activity.application,
            args.collectorId))[CollectorViewModel::class.java]

        viewModel.collector.observe(viewLifecycleOwner, Observer<Collector> {

            it.apply {
                binding.collector = this
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer<Boolean> { isNetworkError ->
            if (isNetworkError) onNetworkError()
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun onNetworkError() {
        if(!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }
}
