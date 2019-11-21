package com.osueat.hungry.model

class TruckUtility {
    fun GetActiveTrucksFromList(truckList: ArrayList<Truck>) : ArrayList<Truck> {
        val activeTruckList = ArrayList<Truck>()

        for(i in 0..truckList.size - 1) {
            if (truckList[i].isActive)
                activeTruckList.add(truckList[i])
        }

        return activeTruckList
    }
}