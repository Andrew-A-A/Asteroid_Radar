package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {


    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel
        val adapter=AsteroidsAdapter(AsteroidsAdapter.OnClickListener{
            viewModel.displayAsteroidDetails(it)
        })
        binding.asteroidRecycler.adapter=adapter
    viewModel.filter.observe(viewLifecycleOwner) {
        if (it == Filter.TODAY) {
            viewModel.asteroidsToday.observe(viewLifecycleOwner) { asteroid ->
                asteroid.apply {
                    adapter.submitList(this)
                }
            }
        } else {
            viewModel.asteroids.observe(viewLifecycleOwner) { asteroid ->
                asteroid.apply {
                    adapter.submitList(this)
                }
            }
        }

    }


        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            if (null != it) {
                this.findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsCompleted()
            }
        }
        setHasOptionsMenu(true)
        return binding.root
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        viewModel.filter.value = when(item.title){
            "View week asteroids"-> Filter.ALL
            "View today asteroids"-> Filter.TODAY
            "View saved asteroids"-> Filter.SAVED
            else ->{
                Filter.ALL
            }
        }
        Toast.makeText(context, viewModel.filter.value!!.name,Toast.LENGTH_SHORT).show()
        return true
    }
}







