package util.syntax

import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.NodeList
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration
import com.github.javaparser.ast.body.MethodDeclaration
import com.github.javaparser.ast.expr.MethodCallExpr
import com.github.javaparser.ast.stmt.BlockStmt
import com.github.javaparser.ast.type.ClassOrInterfaceType
import java.lang.Exception

class ClassUtil(override var cu: CompilationUnit) :
    BaseUtil<ClassOrInterfaceDeclaration>(ClassOrInterfaceDeclaration::class.java) {

    fun setName(name: String?, value: String) {
        looper(name) { it.setName(value) }
    }

    fun addMethod(name: String?, code: String) {
        val cu = StaticJavaParser.parse(code)
        val method = cu.findAll(MethodDeclaration::class.java)[0]
        val blockStmt = BlockStmt()
        looper(name) { it.addMember(method) }
    }

    fun getExtends(name: String): List<String>? {
        return try{
            cu.findAll(ClassOrInterfaceDeclaration::class.java)
                .filter {isNameSame(it,name)}[0].extendedTypes.map{it.name.toString()}
        }catch(except : Exception) {
            null
        }
    }


    override fun isNameSame(node: ClassOrInterfaceDeclaration, comp: String): Boolean {
        return node.name.toString() == comp
    }
}