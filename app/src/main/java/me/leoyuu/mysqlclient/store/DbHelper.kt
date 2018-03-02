package me.leoyuu.mysqlclient.store

import android.database.sqlite.SQLiteDatabase
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import me.leoyuu.mysqlclient.App
import me.leoyuu.mysqlclient.store.entity.RecorderModel

/**
 * date 2018/3/2
 * email july.oloy@qq.com
 *
 * @author leoyuu
 */

object DbHelper {
    private const val DB_NAME = "MYSQL_CLIENT_DB"
    private val db = SQLiteDatabase.openOrCreateDatabase(App.app.getDatabasePath(DB_NAME), null)
    private val handlerThread = HandlerThread(DbHelper::class.java.simpleName)
    private val uiHandler = Handler(Looper.getMainLooper())
    private var dbHandler: Handler

    init {
        handlerThread.start()
        dbHandler = Handler(handlerThread.looper)
        dbHandler.post {
            db.execSQL("create table if not exists record(id INTEGER PRIMARY KEY  AUTOINCREMENT, time bigint, cmd text, result text)")
        }
    }

    fun saveRecorder(model: RecorderModel) {
        db.execSQL("insert into record(time, cmd, result) values(?, ?, ?)", arrayOf(model.time, model.cmd, model.result))
    }

    fun getRecorderAsync(callback: OnQueryCallback<RecorderModel>) {
        dbHandler.post {
            val cursor = db.rawQuery("select id, time, cmd, result from record order by id desc", null)
            val res = mutableListOf<RecorderModel>()
            while (cursor.moveToNext()) {
                res.add(RecorderModel(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getString(3)))
            }
            cursor.close()
            uiHandler.post { callback.onResult(res) }
        }
    }

    interface OnQueryCallback<T> {
        fun onResult(result: List<T>)
    }
}
