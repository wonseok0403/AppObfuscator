package modifier

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import util.file.FileUtil
import java.io.File

abstract class AbstractModifier : BaseModifier {
    protected lateinit var targetAppName: String
    protected val mainFolder by lazy { FileUtil.getMainFolder(targetAppName) }
    protected val codeFolder by lazy { File("${mainFolder}/java/${FileUtil.getPackageName(mainFolder)?.replace(".","/")}") }
    protected val javaFiles: List<File> by lazy { FileUtil.getJavaFiles(mainFolder) }

    protected fun parse(file : File) : CompilationUnit{
        return StaticJavaParser.parse(file)
    }

    protected fun parse(code : String) : CompilationUnit{
        return StaticJavaParser.parse(code)
    }
}