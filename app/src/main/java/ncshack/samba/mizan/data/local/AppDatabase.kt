package ncshack.samba.mizan.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import ncshack.samba.mizan.data.local.dao.DocumentDao
import ncshack.samba.mizan.data.local.entity.DocumentEntity

@Database(
    entities = [DocumentEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun documentDao(): DocumentDao
}