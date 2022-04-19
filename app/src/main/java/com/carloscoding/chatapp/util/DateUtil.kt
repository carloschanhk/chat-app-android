package com.carloscoding.chatapp.util

import java.text.DateFormat
import java.util.*

fun Long.convertToDateString(): String {
    return DateFormat.getDateInstance(DateFormat.DEFAULT).format(Date(this))
}