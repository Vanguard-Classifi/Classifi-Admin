package com.vanguard.classifiadmin.domain.helpers

import com.vanguard.classifiadmin.domain.extensions.splitWithSpace

val AvatarColorMap: Map<Char, Long> = mapOf(
    'a' to 0xffe31809,
    'b' to 0xff4d4d24,
    'c' to 0xffe3620b,
    'd' to 0xff1c1c0d,
    'e' to 0xff421d05,
    'f' to 0xff242423,
    'g' to 0xffb87b53,
    'h' to 0xff5c5c05,
    'i' to 0xff10210a,
    'j' to 0xff164205,
    'k' to 0xff0f7319,
    'l' to 0xff093322,
    'm' to 0xff044a2d,
    'n' to 0xff053b34,
    'o' to 0xff122640,
    'p' to 0xff124039,
    'q' to 0xff0c23a6,
    'r' to 0xff0c4ea6,
    's' to 0xff0a0342,
    't' to 0xff0c0b12,
    'u' to 0xff030c42,
    'v' to 0xff6b0d75,
    'w' to 0xff1f0d21,
    'x' to 0xff661635,
    'y' to 0xff260311,
    'z' to 0xffd90909
)


fun generateColorFromUserName(username: String): Long {
    val split = username.lowercase().splitWithSpace()
    val first = split.first()[0].toString()
    return AvatarColorMap[split.first()[0]] ?: 0xff000000
}