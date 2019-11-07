package com.osueat.hungry.data.model

import com.osueat.hungry.model.Customer

object CurrentCustomer {
    private var currentCustomer: Customer? = null

    fun getCustomer(): Customer? {
        return currentCustomer
    }

    fun setCustomer(customer: Customer) {
        currentCustomer = customer
    }
}