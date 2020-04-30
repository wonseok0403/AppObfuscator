import modifier.encryptor.Encryptor
import modifier.obfuscator.Obfuscator

fun main() {
    val TEST_APP_NAME = "test_app_java"

    /**
     * #순서
     * #암호화
     *
     * #난독화
     * 변수 사용, 파라미터 사용
     * 변수 선언, 파라미터 선언, 클래스 선언
     **/
    Encryptor.build(TEST_APP_NAME).run()
    Obfuscator.build(TEST_APP_NAME).run()
}
