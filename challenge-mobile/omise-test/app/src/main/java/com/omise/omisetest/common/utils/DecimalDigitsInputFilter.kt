package com.omise.omisetest.common.utils

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

class DecimalDigitsInputFilter(private val digitsBeforeZero: Int, private val digitsAfterZero: Int): InputFilter {
    private val mPattern: Pattern =
        Pattern.compile("[0-9]{0," + (digitsBeforeZero - 1) + "}+((\\.[0-9]{0," + (digitsAfterZero - 1) + "})?)||(\\.)?")

    override fun filter(
        p0: CharSequence?,
        p1: Int,
        p2: Int,
        p3: Spanned?,
        p4: Int,
        p5: Int
    ): CharSequence? {
        val matcher = mPattern.matcher(p3)
        if (!matcher.matches()) {
            return ""
        }
        return null
    }
}