package me.leoyuu.mysqlclient

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

import kotlinx.android.synthetic.main.activity_test.*
import me.leoyuu.mysqlclient.sql.*

/**
 * A login screen that offers login via email/password.
 */
class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        test_table_view.bindData(getQueryTitle(), getQueryRows())
    }

    private fun getQueryTitle():QueryTitle{
        return QueryTitle((0 until COL).map { SqlCol("col$it") })
    }

    private fun getQueryRows():List<SqlRow>{
        return (0 until  ROW).map { row ->
            SqlRow((0 until COL).map { col -> "row$row col$col" })
        }
    }

    companion object {
        private const val COL = 6
        private const val ROW = 1000
    }
}
