package com.example.mankala.player


class AlphaBetaPlayer(
    playerOne: Boolean,
    private val maxDepth: Int,
    private val isFirstMoveRandom: Boolean
) :
    Player(playerOne) {

    override fun makeMove(bins: ArrayList<Int>): Int {
        lastTurnStart = System.currentTimeMillis()

        val binIndex = if (isFirstMoveRandom && moves.value!! == 0) {
            if (playerOne) (0..5).random() else (7..12).random()
        } else {
            val (bin, _) = alphaBeta(
                true,
                bins,
                0,
                Float.NEGATIVE_INFINITY,
                Float.POSITIVE_INFINITY
            )
            bin
        }
        val turnEnd = System.currentTimeMillis()
        lastTurnTime = turnEnd - lastTurnStart
        totalTime += lastTurnTime

        return binIndex
    }

    private fun alphaBeta(
        myTurn: Boolean,
        bins: ArrayList<Int>,
        currDepth: Int,
        alpha: Float,
        beta: Float
    ): Pair<Int, Int> {

        if (currDepth == maxDepth)
            return Pair(-1, (if (playerOne) bins[6] - bins[13] else bins[13] - bins[6]))

        val possibleMoves = ArrayList<Int>()

        var i = if (playerOne)
            if (myTurn)
                0
            else 7
        else
            if (myTurn)
                7
            else 0

        var j = 0

        while (j < 6) {
            if (bins[i + j] > 0)
                possibleMoves.add(i + j)
            j++
        }

        if (possibleMoves.isEmpty()) {
            i = if (playerOne)
                if (myTurn)
                    0
                else 7
            else
                if (myTurn)
                    7
                else 0

            j = 0
            while (j < 6) {
                bins[i + 6] += bins[i + j]
                j++
            }
            return Pair(-1, if (playerOne) bins[6] - bins[13] else bins[13] - bins[6])
        }


        var bestBinIndex = -1
        var bestValue = if (myTurn) Float.NEGATIVE_INFINITY else Float.POSITIVE_INFINITY

        var alphaVar = alpha
        var betaVar = beta

        for (binIndex in possibleMoves) {

            val (newBins, lastRecipient) = distributeStones(
                bins.toMutableList() as ArrayList<Int>,
                binIndex,
                myTurn
            )
            var myTurnAgain = false
            if ((playerOne && lastRecipient == 6) || (!playerOne && lastRecipient == 13))
                myTurnAgain = true

            val (_, value) = alphaBeta(
                myTurnAgain,
                newBins,
                currDepth + 1,
                alphaVar,
                betaVar
            )

            if (myTurn) {
                if (value > bestValue) {
                    bestValue = value.toFloat()
                    bestBinIndex = binIndex
                }
                if (value >= betaVar) {
                    return Pair(bestBinIndex, bestValue.toInt())
                }
                if (value > alphaVar) {
                    alphaVar = value.toFloat()
                }
            } else {
                if (value < bestValue) {
                    bestValue = value.toFloat()
                    bestBinIndex = binIndex
                }
                if (value <= alphaVar) {
                    return Pair(bestBinIndex, bestValue.toInt())
                }
                if (value < betaVar) {
                    betaVar = value.toFloat()
                }
            }
//            if (bestBinIndex == -1) {
//                bestBinIndex = binIndex
//                bestValue = value
//            } else if (myTurn) {
//                if (value > bestValue) {
//                    bestValue = value
//                    bestBinIndex = binIndex
//                }
//            } else {
//                if (value < bestValue) {
//                    bestValue = value
//                    bestBinIndex = binIndex
//                }
//            }
        }
        return Pair(bestBinIndex, bestValue.toInt())
    }


    private fun distributeStones(
        bins: ArrayList<Int>,
        binIndex: Int,
        myTurn: Boolean
    ): Pair<ArrayList<Int>, Int> {
        var giveawayPile = bins[binIndex]
        bins[binIndex] = 0
        var recipient = binIndex + 1
        var lastRecipient = -1
        while (giveawayPile > 0) {
            if (playerOne && recipient == 13)
                recipient = 0
            else if (!playerOne && recipient == 6)
                recipient = 7

            bins[recipient] = bins[recipient].plus(1)

            giveawayPile -= 1

            if (giveawayPile == 0)
                lastRecipient = recipient
            else {
                recipient += 1
                if (recipient > 13)
                    recipient = 0
            }
        }
        if ((myTurn && playerOne) || (!myTurn && !playerOne)) {
            if (bins[lastRecipient] == 1 && lastRecipient < 6 && bins[12 - lastRecipient] > 0) {
                bins[6] += bins[lastRecipient] + bins[12 - lastRecipient]
                bins[lastRecipient] = 0
                bins[12 - lastRecipient] = 0
            }
        } else if ((!myTurn && playerOne) || (myTurn && !playerOne)) {
            if (bins[lastRecipient] == 1 && 6 < lastRecipient && lastRecipient < 13 && bins[12 - lastRecipient] > 0) {
                bins[13] += bins[lastRecipient] + bins[12 - lastRecipient]
                bins[lastRecipient] = 0
                bins[12 - lastRecipient] = 0
            }
        }
        return Pair(bins, lastRecipient)

    }
}