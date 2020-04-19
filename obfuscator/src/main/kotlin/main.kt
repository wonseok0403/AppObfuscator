import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.NameExpr
import com.github.javaparser.ast.expr.VariableDeclarationExpr
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.stmt.ExpressionStmt
import com.github.javaparser.ast.stmt.ReturnStmt
import com.github.javaparser.ast.type.ClassOrInterfaceType
import util.CompilationUtil.saveCompilationUnit
import util.FileUtil.getJavaFiles
import util.FileUtil.getAppFiles
import util.MethodUtil
import util.VariableUtil

fun main() {
    val TEST_APP_NAME = "test_app_java"
    val files = getAppFiles(TEST_APP_NAME)
    val javaFiles = getJavaFiles(files).also {
        it.forEach {
            println(it.name)
        }
    }

//    javaFiles[1].also { file ->
//        val cu: CompilationUnit = StaticJavaParser.parse(file)
//        getVariable(cu).forEach {
//            println(it.toString())
//            modifyVariable(cu, it.nameAsString, "11")
//        }
//        getMethod(cu).forEach {
//            modifyMethodType(cu, it.nameAsString, "int")
//
//            if(it.name.toString() == "t1"){
//                modifyMethodName(cu, "t1","test1")
//            }
//        }
//
//        saveCompilationUnit(file, cu)
//    }

    var block = BlockStmt()

    javaFiles[2].also { file ->
        val cu: CompilationUnit = StaticJavaParser.parse(file)

        VariableUtil(cu).also {
            it.getVariable().forEach { variable ->
                it.modifyValue(variable.name.toString(), "11")
            }
        }
//        saveCompilationUnit(file, cu)
    }

    javaFiles[1].also { file ->
        val cu: CompilationUnit = StaticJavaParser.parse(file)
        MethodUtil(cu).also {
            it.getMethod().forEach { method ->
                val name = "t1"
                if (method.name.toString() == name) {
                    it.setMethodBody(name, block)
                }
            }
        }
    }

//    val compilationUnit = CompilationUnit()
//    compilationUnit.addImport("org.springframework.boot.SpringApplication");
//    val classDeclaration = compilationUnit.addClass("AnyClassName").setPublic(true)
//    classDeclaration.addAnnotation("AnyAnnotation");
//    val methodDeclaration = classDeclaration.addMethod("anyMethodName").setPublic(true)
//    methodDeclaration.setType("AnyReturnType")
//    methodDeclaration.addAndGetParameter(String::class.java, "args").isVarArgs = true
//    methodDeclaration.addAndGetAnnotation("AnyAnnotation")
}
