package com.plcoding.landmarkrecognitiontensorflow.domain

import android.graphics.Bitmap

interface RoadSingsFinder {
    fun find(bitmap: Bitmap, rotation: Int): String
}
