import cached.FUNCTION_ENCRPTION
import com.github.javaparser.StaticJavaParser
import com.github.javaparser.ast.CompilationUnit
import com.github.javaparser.ast.body.FieldDeclaration
import com.github.javaparser.ast.stmt.BlockStmt
import obfuscator.Obfuscator
import util.file.FileUtil.getAppFiles
import util.file.FileUtil.getJavaFiles
import java.io.File

fun main() {
    val TEST_APP_NAME = "test_app_java"

    /**
     * #난독화 순서
     * 클래스 선언, 변수 선언, 파라미터 선언
     * 클래스 사용, 변수 사용, 파라미터 사용
     **/
    Obfuscator.build(TEST_APP_NAME).run()
    //TODO Encryptor 로직
}
