package util

import com.github.javaparser.ast.CompilationUnit
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object CompilationUtil {
    fun saveCompilationUnit(path: Path, cu: CompilationUnit) {
        Files.write(path, cu.toString().toByteArray())
    }

    fun saveCompilationUnit(uri: String, cu: CompilationUnit) {
        saveCompilationUnit(Paths.get(uri), cu)
    }

    fun saveCompilationUnit(file: File, cu: CompilationUnit) {
        saveCompilationUnit(Paths.get(file.absolutePath), cu)
    }
}