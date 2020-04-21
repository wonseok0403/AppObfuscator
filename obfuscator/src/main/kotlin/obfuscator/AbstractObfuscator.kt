package obfuscator

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import util.file.FileUtil
import java.io.File

abstract class AbstractObfuscator : BaseObfuscator{
    protected lateinit var targetAppName: String
    protected val folder by lazy { FileUtil.getAppFiles(targetAppName) }
    protected val javaFiles: List<File> by lazy { FileUtil.getJavaFiles(folder) }

    protected fun parse(file : File) : CompilationUnit{
        return StaticJavaParser.parse(file)
    }
}