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

    fun GetInactiveTrucksFromList(truckList: ArrayList<Truck>) : ArrayList<Truck> {
        val inactiveTruckList = ArrayList<Truck>()

        for(i in 0..truckList.size - 1) {
            if (!truckList[i].isActive)
                inactiveTruckList.add(truckList[i])
        }

        return inactiveTruckList
    }

    fun DeactivateAllActiveTrucksInList(truckList: ArrayList<Truck>) : ArrayList<Truck> {
        val deactivatedTrucks = ArrayList<Truck>()

        for(i in 0..truckList.size - 1) {
            if (truckList[i].isActive) {
                val deactivatedTruck = Truck(truckList[i].id, truckList[i].name, truckList[i].address,
                    truckList[i].foodIdList, truckList[i].vendorId, false, truckList[i].latitude, truckList[i].longitude)

                deactivatedTrucks.add(deactivatedTruck)
            } else {
                deactivatedTrucks.add(truckList[i])
            }
        }

        return deactivatedTrucks
    }
}