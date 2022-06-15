package com.example.testing

class ListUtils(val list: List<String>) {

    fun filter(): List<String> {
        val filter = "filter"
        return list.filter { it.contains(filter) }
    }

}