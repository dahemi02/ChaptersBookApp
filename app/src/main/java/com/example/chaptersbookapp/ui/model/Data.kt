package com.example.chaptersbookapp.ui.model

import com.example.chaptersbookapp.R
import com.example.chaptersbookapp.ui.data.Author
import com.example.chaptersbookapp.ui.data.Book

object Data {
    val books = listOf(
        Book(1, R.string.beachRead, R.string.emilyHenry, R.string.beachread_des, R.string.romance, R.drawable.beachread),
        Book(2, R.string.betterThanTheMovies, R.string.lynnPainter, R.string.betterTTM_des, R.string.romance, R.drawable.betterthanthemovies),
        Book(3, R.string.twistedGames, R.string.anaHuang, R.string.twistedGames_des, R.string.romance, R.drawable.twistedgames),
        Book(4, R.string.silentPatient, R.string.alexMichaelides, R.string.silentPatient_des, R.string.mystery, R.drawable.thesilentpatient),
        Book(5, R.string.theHousemaid, R.string.freidaMcFadden, R.string.theHousemaid_des, R.string.mystery, R.drawable.thehousemaid),
        Book(6, R.string.aGGTM, R.string.hollyJackson, R.string.aGGTM_des, R.string.mystery, R.drawable.agggtm),
        Book(7, R.string.dune, R.string.frankHerbert, R.string.dune_des, R.string.scienceFiction, R.drawable.dune),
        Book(8, R.string.theThreeBodyProblem, R.string.cixinLiu, R.string.theThreeBodyProblem_des, R.string.scienceFiction, R.drawable.thethreebodyproblem),
        Book(9, R.string.theDaVinciCode, R.string.danBrown, R.string.theDaVinciCode_des, R.string.scienceFiction, R.drawable.thedavincicode),
        Book(10, R.string.theCruelPrince, R.string.hollyBlack, R.string.theCruelPrince_des, R.string.fantasy, R.drawable.thecruelprince),
        Book(11, R.string.tILOAL, R.string.vESchwab, R.string.tILOAL_des, R.string.fantasy, R.drawable.tiloal),
        Book(12, R.string.sixOfCrows, R.string.leighBardugo, R.string.sixOfCrows_des, R.string.fantasy, R.drawable.sixofcrows)
    )

    val authors = listOf(
        Author(1, R.string.emilyHenry, R.string.emilyHenry_des, R.drawable.emilyhenry),
        Author(2, R.string.aliHazelwood, R.string.aliHazelwood_des, R.drawable.alihazelwood),
        Author(3, R.string.danBrown, R.string.danBrown_des, R.drawable.danbrown),
        Author(4, R.string.hollyBlack, R.string.hollyBlack_des,R.drawable.hollyblack),
        Author(5, R.string.vESchwab, R.string.vESchwab_des, R.drawable.veschwab),
        Author(6, R.string.adamSilvera, R.string.adamSilvera_des, R.drawable.adamsilvera),
        Author(7, R.string.heatherMorris, R.string.heatherMorris_des, R.drawable.heathermorris),
        Author(8, R.string.alexMichaelides, R.string.alexMichaelides_des, R.drawable.alexmichaelides)
    )

}