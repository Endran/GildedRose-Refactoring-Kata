package com.gildedrose

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class GildedRoseTest {

/*
    - Once the sell by date has passed, Quality degrades twice as fast
    - The Quality of an item is never negative
    - Min quality = 0
    - Max quality = 50
    - Alter quality by
        - Brie: +1
        - Sulfuras: 0
        - Backstage: by sellin:
            - <= 3 -> +5
            - <= 10 -> +2
            - <=0 -> quality to 0
            - > 10 -> +1
        - Conjured -2
        - Other -1

 */

    @Test
    fun `regular item degrades by 1 before sell date`() {
        val app = GildedRose(arrayOf(Item("NORMAL_ITEM", 10, 10)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item("NORMAL_ITEM", 9, 9))
    }

//    @Test
//    fun `item is never above 50`() {
//        val app = GildedRose(arrayOf(Item("NORMAL_ITEM", 10, 100)))
//
//        app.updateQuality()
//
//        assertThat(app.items).containsExactly(Item("NORM5AL_ITEM", 9, 510)
//        )
//    }

    @Test
    fun `regular item degrades by 2 after sell date`() {
        val app = GildedRose(arrayOf(Item("NORMAL_ITEM", -2, 10)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item("NORMAL_ITEM", -3, 8))
    }

    @Test
    fun `brie item increases by 1 before sell date`() {
        val app = GildedRose(arrayOf(Item(GildedRose.BRIE, 10, 10)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item(GildedRose.BRIE, 9, 11))
    }

    @Test
    fun `brie item increases by 2 after sell date`() {
        val app = GildedRose(arrayOf(Item(GildedRose.BRIE, -2, 10)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item(GildedRose.BRIE, -3, 12))
    }

    @Test
    fun `value of item is never negative`() {
        val app = GildedRose(arrayOf(Item("NORMAL_ITEM", 10, 0)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item("NORMAL_ITEM", 9, 0))
    }

    @Test
    fun `value of item is never increases 50`() {
        val app = GildedRose(arrayOf(Item(GildedRose.BRIE, 10, 50)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item(GildedRose.BRIE, 9, 50))
    }

    @Test
    fun `Sulfuras is an exception to all rules`() {
        val app = GildedRose(arrayOf(Item(GildedRose.SULFURAS, 10, 80)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item(GildedRose.SULFURAS, 10, 80))
    }

//    @Test
//    fun `Sulfuras is always quality 80`() {
//        val app = GildedRose(arrayOf(Item(GildedRose.SULFURAS, 10, 0)))
//
//        app.updateQuality()
//
//        assertThat(app.items).containsExactly(Item(GildedRose.SULFURAS, 10, 80))
//    }

    @Test
    fun `Backstage passes increase by 1 before 10 days to sell`() {
        val app = GildedRose(arrayOf(Item(GildedRose.BACKSTAGE, 11, 10)))

        app.updateQuality()

        assertThat(app.items).containsExactly(Item(GildedRose.BACKSTAGE, 10, 11))
    }

    @Test
    fun `Backstage passes increase by 2 between 6 and 9 days to sell`() {
        val app = GildedRose(arrayOf(
                Item(GildedRose.BACKSTAGE, 9, 10),
                Item(GildedRose.BACKSTAGE, 6, 10)
        ))

        app.updateQuality()

        assertThat(app.items).containsExactly(
                Item(GildedRose.BACKSTAGE, 8, 12),
                Item(GildedRose.BACKSTAGE, 5, 12),
        )
    }

    @Test
    fun `Backstage passes by 5 between 0 and 5 days to sell`() {
        val items = arrayOf(
                Item(GildedRose.BACKSTAGE, 5, 10),
                Item(GildedRose.BACKSTAGE, 1, 10)
        )
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items).containsExactly(
                Item(GildedRose.BACKSTAGE, 4, 13),
                Item(GildedRose.BACKSTAGE, 0, 13),
        )
    }

    @Test
    fun `Backstage drop to 0 past selling date`() {
        val items = arrayOf(
                Item(GildedRose.BACKSTAGE, 0, 10),
        )
        val app = GildedRose(items)
        app.updateQuality()
        assertThat(app.items).containsExactly(
                Item(GildedRose.BACKSTAGE, -1, 0),
        )
    }
}


