package util.file

import java.io.File

object FileUtil {
    private val filterFiles = listOf(
        "ExampleInstrumentedTest.java",
        "ExampleUnitTest.java",
        "BuildConfig.java",
        "R.java"
    )

    fun getAppFiles(name: String): File {
        return File(getTestAppPath(name))
    }

    fun getTestAppPath(name: String): String {
        return joinDir(getRoot(), name)
    }

    fun getRoot(): String {
        return getForwardDir(getCurrentDir())
    }

    fun getJavaFiles(folder: File): List<File> {
        val files = mutableListOf<File>()
        for (fileEntry in folder.listFiles()) {
            if (fileEntry.isDirectory) {
                files += getJavaFiles(fileEntry)
            } else {
                if (fileEntry.name.endsWith(".java") && !filterFiles.contains(fileEntry.name)) {
                    files.add(fileEntry)
                }
            }
        }
        return files
    }

    fun getCurrentDir(): String {
        return System.getProperty("user.dir")
    }

    fun getForwardDir(dir: String): String {
        return dir.split('/').dropLast(1).joinToString("/")
    }

    fun joinDir(main: String, sub: String): String {
        return if (main.last() == '/' && sub.first() == '/') main.dropLast(1) + sub
        else if (main.last() == '/' || sub.first() == '/') main + sub
        else "$main/$sub"
    }
}