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
import com.example.mankala.player.Player
import kotlin.collections.ArrayList


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
            args.playerTwoMinMaxVal,
            args.playerOneFirstMoveRandom,
            args.playerTwoFirstMoveRandom
        )

        viewModel = ViewModelProvider(this, viewModelFactory).get(GameViewModel::class.java)
        binding.lifecycleOwner = this.viewLifecycleOwner
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.playerOneTurn.observe(viewLifecycleOwner, {
            if (it) {

                binding.viewModel!!.playerOne.lastTurnStart = System.currentTimeMillis()
                binding.player1GameTV.setTextColor(resources.getColor(R.color.purple_500, null))
                binding.player1GameTV.setTypeface(null, Typeface.BOLD)


                binding.player2LastTimeTV.text =
                    resources.getString(
                        R.string.last_time,
                        (viewModel.playerTwo.lastTurnTime.toDouble() / 1000).toString()
                    )
                binding.player2TimeTV.text = resources.getString(
                    R.string.total_time,
                    (viewModel.playerTwo.totalTime.toDouble() / 1000).toString()
                )
                binding.player2GameTV.setTextColor(resources.getColor(R.color.black, null))
                binding.player2GameTV.setTypeface(null, Typeface.NORMAL)


                if (viewModel.playerOneType != resources.getString(R.string.human)) {
                    viewModel.chosenBin =
                        viewModel.playerOne.makeMove(viewModel.bins.value!!.toMutableList() as ArrayList<Int>)
                    binding.infoTV.text =
                        resources.getString(R.string.chosen_bin, viewModel.chosenBin)
                    viewModel.game()
                }

            } else {
                binding.player1LastTimeTV.text =
                    resources.getString(
                        R.string.last_time,
                        (viewModel.playerOne.lastTurnTime.toDouble() / 1000).toString()
                    )
                binding.player1TimeTV.text = resources.getString(
                    R.string.total_time,
                    (viewModel.playerOne.totalTime.toDouble() / 1000).toString()
                )

                binding.player1GameTV.setTextColor(resources.getColor(R.color.black, null))
                binding.player1GameTV.setTypeface(null, Typeface.NORMAL)

                binding.viewModel!!.playerTwo.lastTurnStart = System.currentTimeMillis()

                binding.player2GameTV.setTextColor(resources.getColor(R.color.purple_500, null))
                binding.player2GameTV.setTypeface(null, Typeface.BOLD)


                if (viewModel.playerTwoType != resources.getString(R.string.human)) {
                    viewModel.chosenBin =
                        viewModel.playerTwo.makeMove(viewModel.bins.value!!.toMutableList() as ArrayList<Int>)
                    binding.infoTV.text =
                        resources.getString(R.string.chosen_bin, viewModel.chosenBin)
                    viewModel.game()

                }
            }})

//        viewModel.playerTwo.myTurn.observe(viewLifecycleOwner, {
//            if (it) {
//
//                binding.viewModel!!.playerTwo.lastTurnStart = System.currentTimeMillis()
//
//                binding.player2GameTV.setTextColor(resources.getColor(R.color.purple_500, null))
//                binding.player2GameTV.setTypeface(null, Typeface.BOLD)
//
//
//                if (viewModel.playerTwoType != resources.getString(R.string.human)) {
//                    viewModel.chosenBin =
//                        viewModel.playerTwo.makeMove(viewModel.bins.value!!.toMutableList() as ArrayList<Int>)
//                    binding.infoTV.text =
//                        resources.getString(R.string.chosen_bin, viewModel.chosenBin)
//                    viewModel.game()
//                }
//            } else {
//                binding.player2LastTimeTV.text =
//                    resources.getString(
//                        R.string.last_time,
//                        (viewModel.playerTwo.lastTurnTime.toDouble() / 1000).toString()
//                    )
//                binding.player2TimeTV.text = resources.getString(
//                    R.string.total_time,
//                    (viewModel.playerTwo.totalTime.toDouble() / 1000).toString()
//                )
//                binding.player2GameTV.setTextColor(resources.getColor(R.color.black, null))
//                binding.player2GameTV.setTypeface(null, Typeface.NORMAL)
//            }
//        })

            if (viewModel.playerOneType == resources.getString(R.string.human)) {
                binding.player1Bin1.setOnClickListener { chooseBin(0, viewModel.playerOne) }
                binding.player1Bin2.setOnClickListener { chooseBin(1, viewModel.playerOne) }
                binding.player1Bin3.setOnClickListener { chooseBin(2, viewModel.playerOne) }
                binding.player1Bin4.setOnClickListener { chooseBin(3, viewModel.playerOne) }
                binding.player1Bin5.setOnClickListener { chooseBin(4, viewModel.playerOne) }
                binding.player1Bin6.setOnClickListener { chooseBin(5, viewModel.playerOne) }
            }

            if (viewModel.playerTwoType == resources.getString(R.string.human)) {
                binding.player2Bin1.setOnClickListener { chooseBin(7, viewModel.playerTwo) }
                binding.player2Bin2.setOnClickListener { chooseBin(8, viewModel.playerTwo) }
                binding.player2Bin3.setOnClickListener { chooseBin(9, viewModel.playerTwo) }
                binding.player2Bin4.setOnClickListener { chooseBin(10, viewModel.playerTwo) }
                binding.player2Bin5.setOnClickListener { chooseBin(11, viewModel.playerTwo) }
                binding.player2Bin6.setOnClickListener { chooseBin(12, viewModel.playerTwo) }
            }

            viewModel.gameOver.observe(viewLifecycleOwner, {
                if (it) {
                    binding.infoTV.text =
                        when {
                            viewModel.bins.value!![6] > viewModel.bins.value!![13] -> "Wygrał Gracz 1"
                            viewModel.bins.value!![6] < viewModel.bins.value!![13] -> "Wygral Gracz 2"
                            else -> "Remis"
                        }
                }
            })
        }


                private fun chooseBin(bin: Int, player: Player) {
            val chosenBin = player.chooseBin(bin, viewModel.bins.value!!)
            if (chosenBin >= 0) {
                viewModel.chosenBin = bin
                binding.infoTV.text =
                    resources.getString(R.string.chosen_bin, viewModel.chosenBin)
                viewModel.game()

            }
        }

    }