package util.syntax

import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.VariableDeclarator
import com.github.javaparser.ast.expr.AssignExpr

class VariableUtil(private var cu: CompilationUnit) {
    fun getVariable(): List<VariableDeclarator> {
        val list = mutableListOf<VariableDeclarator>()
        cu.findAll(VariableDeclarator::class.java){
            list.add(it)
        }
        return list
    }

    fun modifyValue(varName: String, changeValue: String) {
        looper(varName) { it.setInitializer(changeValue) }
    }

    fun modifyName(varName: String, changeValue: String) {
        looper(varName) { it.setName(changeValue) }
    }

    private fun looper(varName: String, block: ((VariableDeclarator) -> Unit)) {
        cu.findAll(VariableDeclarator::class.java).filter {
            if (varName.isNullOrEmpty()) {
                true
            } else {
                it.name.toString() == varName
            }
        }.forEach {
            block(it)
        }
    }
}