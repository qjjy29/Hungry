package com.osueat.hungry.util

import android.os.Parcel
import android.os.Parcelable

class MyParcelable() : Parcelable {
    private var mData = 0

    constructor(parcel: Parcel) : this() {
        mData = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(mData)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MyParcelable> {
        override fun createFromParcel(parcel: Parcel): MyParcelable {
            return MyParcelable(parcel)
        }

        override fun newArray(size: Int): Array<MyParcelable?> {
            return arrayOfNulls(size)
        }
    }

}

