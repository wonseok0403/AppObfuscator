import util.FileUtil.getJavaFiles
import util.FileUtil.getTestAppFiles
import util.CompilationUtil.saveCompilationUnit
import util.VariableUtil.getVariable
import util.VariableUtil.modifyVariable
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit

fun main() {
    val TEST_APP_NAME = "test_app"
    val files = getTestAppFiles(TEST_APP_NAME)
    val javaFiles = getJavaFiles(files)

    javaFiles[1].also { file ->
        val cu: CompilationUnit = StaticJavaParser.parse(file)
        getVariable(cu).forEach {
            println(it.toString())
            modifyVariable(cu, it.nameAsString, "11")
        }
        saveCompilationUnit(file, cu)
    }
}