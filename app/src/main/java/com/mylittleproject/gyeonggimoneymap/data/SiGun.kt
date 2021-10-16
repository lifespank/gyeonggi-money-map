package com.mylittleproject.gyeonggimoneymap.data

import com.naver.maps.geometry.LatLng

enum class SiGun(val siGunName: String, val latLng: LatLng) {
    GAPYEONG("가평군", LatLng(37.831275785867255, 127.5095740386528)),
    GOYANG("고양시", LatLng(37.658241770701316, 126.83201242804257)),
    GWACHEON("과천시", LatLng(37.42921381913445, 126.98761940042974)),
    GWANGMYEONG("광명시", LatLng(37.478530787858965, 126.864695029032)),
    GWANJU("광주시", LatLng(37.429433218665494, 127.2551635581527)),
    GURI("구리시", LatLng(37.59433536541618, 127.12975313004483)),
    GIMPO("김포시", LatLng(37.61535725817597, 126.71545099993476)),
    NAMYANGJU("남양주시", LatLng(37.635999239613874, 127.21650927977556)),
    DONGDUCHEON("동두천시", LatLng(37.90359519017107, 127.06041775791347)),
    BUCHEON("부천시", LatLng(37.50346747037045, 126.76604888014023)),
    SEONGNAM("성남시", LatLng(37.42001813081023, 127.12665377835219)),
    SUWON("수원시", LatLng(37.263346227653095, 127.02854937671871)),
    SIGHEUNG("시흥시", LatLng(37.38009290924344, 126.80285924146753)),
    ANSAN("안산시", LatLng(37.321887495477164, 126.8308443183044)),
    ANSEONG("안성시", LatLng(37.008021627347325, 127.27976179992487)),
    ANYANG("안양시", LatLng(37.39423226955192, 126.95682255810888)),
    YANGJU("양주시", LatLng(37.78527011175076, 127.04577971972166)),
    YANGPYEONG("양평군", LatLng(37.49174014158687, 127.48758719840215)),
    YEOJU("여주시", LatLng(37.298205646305156, 127.6373145539441)),
    YEONCHEON("연천군", LatLng(38.09640866689137, 127.07503959121685)),
    OSAN("오산시", LatLng(37.1498535147121, 127.07744984269557)),
    YONGIN("용인시", LatLng(37.24103621892301, 127.17791004170876)),
    UIWANG("의왕시", LatLng(37.34468350080975, 126.96830999918456)),
    UIJEONGBU("의정부시", LatLng(37.7381059286283, 127.03373300018076)),
    ICHEON("이천시", LatLng(37.27221994634131, 127.43506595665774)),
    PAJU("파주시", LatLng(37.759850054300415, 126.77986916758047)),
    PYEONGTAEK("평택시", LatLng(36.99229982938375, 127.11269959255154)),
    POCHEON("포천시", LatLng(37.89491190108089, 127.2003458742244)),
    HANAM("하남시", LatLng(37.53928268030923, 127.21495393145926)),
    HWASEONG("화성시", LatLng(37.19957888392491, 126.83134105160612));

    companion object {
        fun fromName(name: String) =
            values().first { it.siGunName == name }

        fun toNameList() = values().map { it.siGunName }
    }
}