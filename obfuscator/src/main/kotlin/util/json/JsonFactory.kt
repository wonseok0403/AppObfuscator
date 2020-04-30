package util.json

import org.json.simple.JSONObject
import java.io.File
import java.io.FileWriter
import java.io.IOException

class JsonFactory private constructor() {
    companion object {
        private val jsonObject = JSONObject()
        private var count = 0
        private val assetPath by lazy { "$folder/assets/" }
        private val assetFile by lazy { File(assetPath) }
        private val jsonPath by lazy{ "${assetPath}/encryption.json"}
        private lateinit var folder: File

        fun build(f: File): JsonFactory {
            this.folder = f
            return JsonFactory()
        }
    }

    fun add(value: String) {
        jsonObject[count.toString()] = value
        count++
    }

    fun save() {
        if (!assetFile.exists()) {
            assetFile.mkdir()
        }
        println(jsonObject.toJSONString())

        try {
            FileWriter(jsonPath).use { file ->
                file.write(jsonObject.toJSONString())
                file.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}