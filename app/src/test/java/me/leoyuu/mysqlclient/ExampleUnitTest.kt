package me.leoyuu.mysqlclient

import me.leoyuu.mysqlclient.sql.MySql
import me.leoyuu.mysqlclient.sql.ResultCallback
import me.leoyuu.mysqlclient.sql.SqlResult
import org.junit.Test

import org.junit.Assert.*
import org.junit.BeforeClass

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    companion object {
        private const val HOST = "118.190.98.99"
        private const val PORT = 3306
        private const val USER = "leoyuu"
        private const val PASSWORD = ""

    }



    @Test
    fun addition_isCorrect() {
        assertTrue("leoyuu".contains('.'))
    }
}
