package com.plcoding.landmarkrecognitiontensorflow.presentation

import android.graphics.Bitmap
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.core.graphics.scale
import com.plcoding.landmarkrecognitiontensorflow.domain.RoadSingsFinder

class RoadSignsAnalyzer(
    private val roadSingsFinder: RoadSingsFinder,
    private val onResults: (String) -> Unit
): ImageAnalysis.Analyzer {
    private var frameSkipCounter = 0

    override fun analyze(image: ImageProxy) {
        if (frameSkipCounter % 60 == 0) {
            val rotationDegrees = image.imageInfo.rotationDegrees
            val bitmap = image.toBitmap()
            val scaled = Bitmap.createScaledBitmap(bitmap, 2134, 1600, true)
                .centerCrop(1600, 1600)
            val results = roadSingsFinder.find(scaled, rotationDegrees)
            onResults(results)
        }

        frameSkipCounter++
        image.close()
    }
}
