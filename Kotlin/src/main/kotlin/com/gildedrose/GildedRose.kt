package com.gildedrose

class GildedRose(var items: List<Item>) {

    companion object {
        val BRIE = "Aged Brie"
        val SULFURAS = "Sulfuras, Hand of Ragnaros"
        val BACKSTAGE = "Backstage passes to a TAFKAL80ETC concert"
        val CONJURED = "Conjured Mana Cake"
    }

    fun updateQuality() {
        items = items.map {
            when (it.name) {
                SULFURAS -> it.copy(quality = 80)
                else -> it.copy(sellIn = it.sellIn - 1, quality = determineQuality(it))
            }
        }
    }

    private fun determineQuality(item: Item): Int {
        var modifier = if (item.sellIn > 0) -1 else -2
        when (item.name) {
            BRIE -> modifier *= -1
            CONJURED -> modifier *= 2
            BACKSTAGE ->
                modifier = when {
                    item.sellIn > 10 -> 1
                    item.sellIn > 5 -> 2
                    item.sellIn > 0 -> 3
                    else -> -item.quality
                }
        }

        return (item.quality + modifier).coerceIn(0, 50)
    }
}
