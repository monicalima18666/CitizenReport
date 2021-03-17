package ipvc.estg.citizenreport.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import ipvc.estg.citizenreport.dao.NotaDao
import ipvc.estg.citizenreport.entities.Nota
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Database(entities = [Nota::class], version = 1)

abstract class NotaDB : RoomDatabase() {

   /* abstract fun NotaDao(): NotaDao

    companion object {
        @Volatile
        private var INSTANCE: NotaDB? = null

        fun getDatabase(
                context: Context,
                scope: CoroutineScope
        ): NotaDB {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        NotaDB::class.java,
                        "nota_database"
                )
                        // Wipes and rebuilds instead of migrating if no Migration object.
                        // Migration is not part of this codelab.
                        .fallbackToDestructiveMigration()
                        .addCallback(NotaDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class NotaDatabaseCallback(
                private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {
            *//**
             * Override the onCreate method to populate the database.
             *//*
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.NotaDao())
                    }
                }
            }
        }

        *//**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         *//*
        suspend fun populateDatabase(notaDao: NotaDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
           notaDao.deleteAll()

            var nota = Nota (1, "TRANSITO", "RUA 25 DE ABRIL")
            notaDao.insert(nota)
            nota = Nota(2, "Obras", "Perto de casa")
            notaDao.insert(nota)
            nota = Nota(3, "Tampa", "Desviada")
            notaDao.insert(nota)
        }
    }*/
}