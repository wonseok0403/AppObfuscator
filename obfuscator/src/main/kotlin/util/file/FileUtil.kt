package util.file

import java.io.File
import java.nio.file.Files
import java.nio.file.Path

object FileUtil {
    private val filterFiles = listOf(
        "ExampleInstrumentedTest.java",
        "ExampleUnitTest.java",
        "BuildConfig.java",
        "R.java"
    )

    fun getMainFolder(name: String): File {
        return File(getAppPath(name))
    }

    fun getPackageName(folder: File) : String?{
        return XMLUtil.getPackageName(getManifestXML(folder))
    }

    fun getAppPath(name: String): String {
        return joinDir(getRoot(), "$name/app/src/main")
    }

    fun getRoot(): String {
        return getForwardDir(getCurrentDir())
    }

    fun getJavaFiles(folder: File): List<File> {
        return getFilesByCondition(folder) { it.name.endsWith(".java") && !filterFiles.contains(it.name) }
    }

    fun getManifestXML(folder: File): File{
        return getFilesByCondition(folder) { it.name == "AndroidManifest.xml" }[0]
    }

    private fun getFilesByCondition(folder: File, condition: ((File) -> Boolean)): List<File> {
        val files = mutableListOf<File>()
        for (fileEntry in folder.listFiles()) {
            if (fileEntry.isDirectory) {
                files += getFilesByCondition(fileEntry, condition)
            } else {
                if (condition(fileEntry)) {
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
//        return dir.split('/').dropLast(1).joinToString("/")

        return dir.split('\\').dropLast(1).joinToString("\\")
    }

    fun joinDir(main: String, sub: String): String {
//        return if (main.last() == '/' && sub.first() == '/') main.dropLast(1) + sub
//        else if (main.last() == '/' || sub.first() == '/') main + sub

        // Windows
        return if (main.last() == '\\' && sub.first() == '\\') main.dropLast(1) + sub
        else if (main.last() == '\\' || sub.first() == '\\') main + sub
        else "$main/$sub"
    }

    fun changeFileName(target : File, change : String){
        changeFileName(target.toPath(), change)
    }

    fun changeFileName(target : Path, change : String){
        Files.move(target,target.resolveSibling("$change.java"))
    }
}