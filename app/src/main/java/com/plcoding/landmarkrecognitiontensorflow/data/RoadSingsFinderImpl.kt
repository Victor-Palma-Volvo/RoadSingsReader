package com.plcoding.landmarkrecognitiontensorflow.data

import android.content.Context
import android.graphics.Bitmap
import com.plcoding.landmarkrecognitiontensorflow.domain.RoadSingsFinder
import com.plcoding.landmarkrecognitiontensorflow.ml.Roadsings
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.nio.ByteBuffer

class RoadSingsFinderImpl(
    private val context: Context,
): RoadSingsFinder {
    private fun initTensorCode(bitmap: Bitmap) {
        println("victor - initTensorCode - bitmap :: ${bitmap.width}x${bitmap.height}")
        val model = Roadsings.newInstance(context)

        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 800, 800, 3), DataType.FLOAT32)
        val byteBuffer = bitmapToByteBuffer(bitmap)
        inputFeature0.loadBuffer(byteBuffer)

        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        println("victor - outputFeature0 :: $outputFeature0") // TODO :: how to extract data from here?

        model.close()
    }

    private fun bitmapToByteBuffer(bitmap: Bitmap): ByteBuffer {
        val width = bitmap.width
        val height = bitmap.height
        val size = width * height * 3 // 3 bytes per pixel for RGB
        val byteBuffer = ByteBuffer.allocateDirect(size)
        byteBuffer.order(java.nio.ByteOrder.nativeOrder())

        val intValues = IntArray(width * height)
        bitmap.getPixels(intValues, 0, width, 0, 0, width, height)

        var pixel = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val value = intValues[pixel++]

                // Extract RGB components (assuming ARGB_8888 format)
                byteBuffer.put((value shr 16 and 0xFF).toByte()) // Red
                byteBuffer.put((value shr 8 and 0xFF).toByte())  // Green
                byteBuffer.put((value and 0xFF).toByte())        // Blue
            }
        }

        byteBuffer.rewind()
        return byteBuffer
    }

    override fun find(bitmap: Bitmap, rotation: Int): String {
        try {
            initTensorCode(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}