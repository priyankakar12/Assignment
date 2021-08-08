package com.demo.assignmentdemo.ImageCompresion

interface ImageCompressionListener {
    fun onStart()
    fun onCompressed(filePath: String?)
}