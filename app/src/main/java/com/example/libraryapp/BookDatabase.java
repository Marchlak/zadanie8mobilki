package com.example.libraryapp;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Book.class}, version = 1, exportSchema = false)
public abstract class BookDatabase extends RoomDatabase {
    private static BookDatabase databaseInstance;
    static final ExecutorService databaseWriteExecutor = Executors.newSingleThreadExecutor();

    public abstract BookDAO bookDAO();

    static BookDatabase getDatabase(final Context context) {
        if(databaseInstance == null) {
            databaseInstance = Room.databaseBuilder(context.getApplicationContext(),
                            BookDatabase.class, "book_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }
        return databaseInstance;
    }

    private static final RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriteExecutor.execute(() -> {
                BookDAO dao = databaseInstance.bookDAO();
                Book book = new Book("Sto lat samotności", "Gabriela Garcíi Márqueza");
                dao.insert(book);
                book = new Book("Władca Pierścieni", "J.R.R. Tolkiena");
                dao.insert(book);
                book = new Book("Wielki Gatsby", " F. Scotta Fitzgeralda");
                dao.insert(book);
                book = new Book("Zabić drozda", " Harper Lee");
                dao.insert(book);
            });
        }
    };
}
