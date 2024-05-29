package com.msoft.gallerydemoapp.domain.use_case

import android.annotation.SuppressLint
import com.msoft.gallerydemoapp.utils.HOURS_24_FORMAT_LONG
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@SuppressLint("SimpleDateFormat")
class FormatDateUseCase @Inject constructor() {
    operator fun invoke(date: Date, dateFormat: String = HOURS_24_FORMAT_LONG): String {
        return SimpleDateFormat(dateFormat, Locale.getDefault()).format(date)
    }
}
