package com.steve.commonarch.data

open class ResultServerTime(
    val server_time: Long = 0
) {
    override fun toString(): String {
        return "ResultServerTime(server_time=$server_time)"
    }
}