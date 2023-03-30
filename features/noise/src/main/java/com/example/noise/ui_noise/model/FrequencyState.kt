package com.example.noise.ui_noise.model

data class FrequencyState(
    val frequencies: DoubleArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FrequencyState

        if (!frequencies.contentEquals(other.frequencies)) return false

        return true
    }

    override fun hashCode(): Int {
        return frequencies.contentHashCode()
    }
}