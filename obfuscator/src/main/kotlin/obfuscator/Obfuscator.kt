package obfuscator

import cipher.CipherHelper.SHA256
import com.github.javaparser.ast.CompilationUnit
import util.file.CompilationUtil.saveCompilationUnit
import util.file.FileUtil
import util.file.FileUtil.changeFileName
import util.file.XMLUtil
import util.syntax.ClassUtil
import java.io.File

class Obfuscator private constructor() : AbstractObfuscator() {
    companion object {
        fun build(targetAppName: String): Obfuscator {
            return Obfuscator().apply { this.targetAppName = targetAppName }
        }
    }

    private class DeclarationObfuscator(targetAppName: String) : AbstractObfuscator() {
        init {
            this.targetAppName = targetAppName
        }

        override fun run() {
            XMLUtil.obfuscateManifest(FileUtil.getManifestXML(folder))
            javaFiles.forEach{
                applyToClass(it)
                applyToVariable(it)
                applyToMethod(it)
            }
        }

        private fun applyToClass(file: File) {
            val cu = parse(file)
            val util = ClassUtil(cu)
            val classNameList = mutableListOf<String>()
            val obfuscatedClassNameList = mutableListOf<String>()

            util.get().forEach {
                classNameList.add(it.name.toString())
            }

            classNameList.forEach { default ->
                SHA256(default)?.let {
                    obfuscatedClassNameList.add(it)
                    util.setName(default, it)
                }
            }

            saveCompilationUnit(file, cu)

            changeFileName(file, obfuscatedClassNameList[0])
        }

        private fun applyToVariable(file: File) {
//            val util = VariableUtil(cu)
//            util.get().forEach{
//                val name = it.name.toString()
//                with(SHA256(name),{
//                    util.setName(name, this!!)
//                })
//            }
        }

        private fun applyToMethod(file: File) {

        }
    }

    private class CallingObfuscator(private val cu: CompilationUnit) : AbstractObfuscator() {
        override fun run() {
            applyToVariable()
            applyToMethod()
        }

        private fun applyToVariable() {
//            val util = AssignUtil(cu)
//            util.get().forEach{
//                val name = it.target.toString()
//                println(name)
//                with(SHA256(name),{
//                    //util.setName(name, this!!)
//                })
//            }
        }

        private fun applyToMethod() {

        }
    }

    override fun run() {
        DeclarationObfuscator(targetAppName).run()
    }
}

