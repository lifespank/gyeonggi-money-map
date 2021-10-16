package com.mylittleproject.gyeonggimoneymap.util

fun refineCU(cu: String): List<String> {
    var refinedCU = cu
    if (refinedCU.last() == '점') {
        refinedCU = refinedCU.dropLast(1)
    }
    return listOf(
        refinedCU.replace("CU ", "씨유"),
        refinedCU.replace("CU ", "씨유(CU)"),
        refinedCU.replace("CU", "씨유"),
        refinedCU.replace("CU", "씨유(CU)")
    )
}

fun refineGS25(gs25: String): List<String> {
    var refinedGS25 = gs25
    if (refinedGS25.last() == '점') {
        refinedGS25 = refinedGS25.dropLast(1)
    }
    return listOf(
        refinedGS25.replace("GS25 ", "GS25"),
        refinedGS25.replace("GS25 ", "(GS25)"),
        refinedGS25.replace("GS25", "(GS25)"),
        refinedGS25.replace("GS25 ", "gs25"),
        refinedGS25.replace("GS25", "gs25"),
        refinedGS25.replace("GS25", "(gs25)"),
        refinedGS25.replace("GS25", "지에스25"),
        refinedGS25.replace("GS25 ", "지에스25"),
        refinedGS25.replace("GS25", "지에스(GS25)25"),
        refinedGS25.replace("GS25 ", "지에스(GS)25"),
        refinedGS25.replace("GS25", "지에스(GS)25"),
    )
}