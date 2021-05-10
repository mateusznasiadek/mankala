package com.example.mankala

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import com.example.mankala.databinding.MenuFragmentBinding
import com.google.android.material.slider.Slider


class MenuFragment : Fragment() {

    private lateinit var viewModel: MenuViewModel
    private lateinit var binding: MenuFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.menu_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        viewModel = ViewModelProvider(this).get(MenuViewModel::class.java)
        binding.viewModel = viewModel

        binding.playBTN.setOnClickListener { goToGame() }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setMenus()
        setObservers()
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        setMenus()
    }

    private fun goToGame() {
        val action = MenuFragmentDirections.actionMenuFragmentToGameFragment(
            viewModel.playerOneType.value!!,
            viewModel.playerTwoType.value!!,
            viewModel.playerOneMinMaxValue.value!!,
            viewModel.playerTwoMinMaxValue.value!!
        )
        NavHostFragment.findNavController(this).navigate(action)
    }


    private fun setMenus() {
        val items =
            listOf(resources.getString(R.string.human), resources.getString(R.string.min_max_alg))
        val adapter = ArrayAdapter(requireContext(), R.layout.details_menu_item, items)

        (binding.player1Menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.player1AUTV.setOnItemClickListener { adapterView: AdapterView<*>, view1: View, i: Int, l: Long ->
            viewModel.playerOneType.value = binding.player1AUTV.text.toString()
        }

        (binding.player2Menu.editText as? AutoCompleteTextView)?.setAdapter(adapter)
        binding.player2AUTV.setOnItemClickListener { _: AdapterView<*>, _: View, _: Int, _: Long ->
            viewModel.playerTwoType.value = binding.player2AUTV.text.toString()
        }


        binding.player1MinMaxSlider.addOnChangeListener(
            Slider.OnChangeListener { _, value, _ ->
                viewModel.playerOneMinMaxValue.value = value.toInt()
            })

        binding.player2MinMaxSlider.addOnChangeListener(
            Slider.OnChangeListener { _, value, _ ->
                viewModel.playerTwoMinMaxValue.value = value.toInt()
            })
    }

    private fun setObservers() {
        viewModel.playerOneType.observe(viewLifecycleOwner, {
            if (it == resources.getString(R.string.human)) {
                binding.player1MinMaxSlider.visibility = View.GONE
                binding.player1MinMaxTV.visibility = View.GONE
            } else if (it == resources.getString(R.string.min_max_alg)) {
                binding.player1MinMaxSlider.visibility = View.VISIBLE
                binding.player1MinMaxTV.visibility = View.VISIBLE
            }
        })
        viewModel.playerTwoType.observe(viewLifecycleOwner, {
            if (it == resources.getString(R.string.human)) {
                binding.player2MinMaxSlider.visibility = View.GONE
                binding.player2MinMaxTV.visibility = View.GONE
            } else if (it == resources.getString(R.string.min_max_alg)) {
                binding.player2MinMaxSlider.visibility = View.VISIBLE
                binding.player2MinMaxTV.visibility = View.VISIBLE
            }
        })

        viewModel.playerOneMinMaxValue.observe(viewLifecycleOwner, {
                binding.player1MinMaxTV.text = viewModel.playerOneMinMaxValue.value.toString()
        })

        viewModel.playerTwoMinMaxValue.observe(viewLifecycleOwner, {
                binding.player2MinMaxTV.text = viewModel.playerTwoMinMaxValue.value.toString()
        })
    }


}