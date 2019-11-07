package com.osueat.hungry.data.model

import com.osueat.hungry.model.Vendor

object CurrentVendor {
    private var currentVendor:Vendor? = null

    fun getCurrentVendor(): Vendor? {
        return currentVendor
    }

    fun setCurrentVendor(vendor: Vendor) {
        currentVendor = vendor
    }
}