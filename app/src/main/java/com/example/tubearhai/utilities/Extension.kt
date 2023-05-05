package com.example.tubearhai.utilities

import com.example.tubearhai.model.Volume

fun Volume.volumeLitres() : String{
    return this.value.toString() +" "+this.unit
}

