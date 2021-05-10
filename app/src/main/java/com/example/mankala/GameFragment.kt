package com.example.mankala

import android.graphics.Typeface
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.mankala.databinding.GameFragmentBinding

class GameFragment : Fragment() {

    private lateinit var viewModel: GameViewModel
    private lateinit var viewModelFactory: GameViewModelFactory
    private lateinit var binding: GameFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.game_fragment, container, false)

        val args = GameFragmentArgs.fromBundle(requireArguments())
        viewModelFactory = GameViewModelFactory(
            args.playerOneType,
            args.playerTwoType,
            args.playerOneMinMaxVal,
            args.playerTwoMinMaxVal
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var begin = System.currentTimeMillis()
        viewModel.playerOneTurn.observe(viewLifecycleOwner, {
            if (it == true) {
                if (viewModel.playerOne.value!!.turns != 0) {
                    val end = System.currentTimeMillis()
                    val time = end - begin
                    viewModel.playerTwo.value!!.lastTurnTime = time
                    viewModel.playerTwo.value!!.totalTime += time

                    viewModel.playerTwo.value = viewModel.playerTwo.value

                    binding.player2LastTimeTV.text =
                        resources.getString(R.string.last_time, (time.toDouble() / 1000).toString())
                    binding.player2TimeTV.text = resources.getString(
                        R.string.last_time,
                        (viewModel.playerTwo.value!!.totalTime.toDouble() / 1000).toString()
                    )

                    begin = System.currentTimeMillis()
                }

                binding.player1GameTV.setTextColor(resources.getColor(R.color.purple_500, null))
                binding.player1GameTV.setTypeface(null, Typeface.BOLD)
                binding.player2GameTV.setTextColor(resources.getColor(R.color.black, null))
                binding.player2GameTV.setTypeface(null, Typeface.NORMAL)


            } else {
                val end = System.currentTimeMillis()
                val time = end - begin
                viewModel.playerOne.value!!.lastTurnTime = time
                viewModel.playerOne.value!!.totalTime += time
                viewModel.playerOne.value = viewModel.playerOne.value

                binding.player1LastTimeTV.text =
                    resources.getString(R.string.last_time, (time.toDouble() / 1000).toString())
                binding.player1TimeTV.text = resources.getString(
                    R.string.last_time,
                    (viewModel.playerOne.value!!.totalTime.toDouble() / 1000).toString()
                )

                begin = System.currentTimeMillis()

                binding.player2GameTV.setTextColor(resources.getColor(R.color.purple_500, null))
                binding.player2GameTV.setTypeface(null, Typeface.BOLD)
                binding.player1GameTV.setTextColor(resources.getColor(R.color.black, null))
                binding.player1GameTV.setTypeface(null, Typeface.NORMAL)
            }
        })

        if (viewModel.playerOne.value!!.type == resources.getString(R.string.human)) {
            binding.player1Bin1.setOnClickListener { chooseBin(0) }
            binding.player1Bin2.setOnClickListener { chooseBin(1) }
            binding.player1Bin3.setOnClickListener { chooseBin(2) }
            binding.player1Bin4.setOnClickListener { chooseBin(3) }
            binding.player1Bin5.setOnClickListener { chooseBin(4) }
            binding.player1Bin6.setOnClickListener { chooseBin(5) }
        }
        if (viewModel.playerTwo.value!!.type == resources.getString(R.string.human)) {
            binding.player2Bin1.setOnClickListener { chooseBin(7) }
            binding.player2Bin2.setOnClickListener { chooseBin(8) }
            binding.player2Bin3.setOnClickListener { chooseBin(9) }
            binding.player2Bin4.setOnClickListener { chooseBin(10) }
            binding.player2Bin5.setOnClickListener { chooseBin(11) }
            binding.player2Bin6.setOnClickListener { chooseBin(12) }
        }
    }


    private fun chooseBin(bin: Int) {
        if (viewModel.playerOneTurn.value!! && bin > 5)
            return
        else if (!viewModel.playerOneTurn.value!! && bin < 7)
            return
        if (viewModel.bins.value!![bin] > 0) {
            viewModel.chosenBin = bin
            viewModel.game()
        }
    }

}