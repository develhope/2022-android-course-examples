package com.example.testing

import assertk.assertAll
import assertk.assertThat
import assertk.assertions.containsOnly
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.size
import org.junit.Test

class ListUtilsTest {

    @Test
    fun `filtering an empty array returns an empty array`() {
        val sut = ListUtils(emptyList())

        val result = sut.filter()

        assertThat(result).isEmpty()
    }

    @Test
    fun `filtering without matches returns an empty array`() {
        val noMatchesList = listOf("something", "")
        val sut = ListUtils(noMatchesList)

        val result = sut.filter()

        assertThat(result).isEmpty()
    }

    @Test
    fun `filtering with matches returns matches`() {
        val matchesList = listOf("something", "filter", "filter1")
        val sut = ListUtils(matchesList)

        val result = sut.filter()

        assertAll {
            assertThat(result).containsOnly("filter", "filter1")
            assertThat(result.size).isEqualTo(2)
        }
    }
}