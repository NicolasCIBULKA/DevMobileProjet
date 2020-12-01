package com.example.devmobileprojet.treatment;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import androidx.annotation.Nullable;

import com.example.devmobileprojet.dataclass.Music;
import com.example.devmobileprojet.dataclass.MusicList;
import com.example.devmobileprojet.dataclass.Playlist;

import java.io.File;
import java.nio.file.Files;

/*
    Class to manage the Database
    creating tables
    register new playlist or music

 */
public class DataBaseHandler extends SQLiteOpenHelper {

    // Attributs
    private static final String FILE_PATH = "/storage/sdcard/music";
    // Database version
    private static final int DATABASE_VERSION = 1;

    // Database name
    private static final String DATABASE_NAME = "MusicApp";

    // Tables names
    private static final String TABLE_MUSIC = "Music";
    private static final String TABLE_PLAYLIST = "Playlist";
    private static final String TABLE_HAVE = "Have";

    // Tables Columns Names
    private static final String KEY_IDMUSIC = "idMusic";
    private static final String KEY_POSITION = "position";
    private static final String KEY_SIZE = "size";
    private static final String KEY_LENGTH = "length";
    private static final String KEY_NAME = "name";
    private static final String KEY_ARTIST = "artist";

    private static final String KEY_IDPLAYLIST = "idPlaylist";
    private static final String KEY_PLAYLISTNAME = "idPlaylist";
    private static final String KEY_DESC = "description";


    // ---------------------
    // Methods
    // ---------------------

    // Constructor
    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create DataBase
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQLite Request of table creation

        String CREATE_TABLE_MUSIC = "CREATE TABLE " + TABLE_MUSIC + "("
                + KEY_IDMUSIC + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_POSITION + " TEXT,"
                + KEY_SIZE + " INTEGER," + KEY_LENGTH + " INTEGER," + KEY_NAME + " TEXT UNIQUE," + KEY_ARTIST + " TEXT" + ")";

        String CREATE_TABLE_PLAYLIST = "CREATE TABLE " + TABLE_PLAYLIST + "("+ KEY_IDPLAYLIST + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + KEY_PLAYLISTNAME +" TEXT, " + KEY_DESC + " TEXT)";

        String CREATE_TABLE_HAVE = "CREATE TABLE " + TABLE_HAVE + "(" + KEY_IDPLAYLIST +  " INTEGER, "
                + KEY_IDMUSIC + " INTEGER, FOREIGN KEY  (" + KEY_IDMUSIC + ") REFERENCES  "+ TABLE_MUSIC +"("+ KEY_IDMUSIC +"), " +
                " FOREIGN KEY ("+ KEY_IDPLAYLIST+ ") REFERENCES "+TABLE_PLAYLIST+"("+ KEY_IDPLAYLIST +")," +
                "PRIMARY KEY ("+ KEY_IDPLAYLIST +","+ KEY_IDMUSIC +"))";

        // Execution of the SQLite requests
        db.execSQL(CREATE_TABLE_MUSIC);
        db.execSQL(CREATE_TABLE_PLAYLIST);
        db.execSQL(CREATE_TABLE_HAVE);
    }

    // Upgrade the database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HAVE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLAYLIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MUSIC);

        // Create tables again
        onCreate(db);
    }

    // Verify the DataBase - if a music isn't in folder, delete it
    public void verifyDataBase(SQLiteDatabase db){
        String FULL_DATABASE = "SELECT * FROM " + TABLE_MUSIC ;
        Cursor cursor = db.rawQuery(FULL_DATABASE, null);
        do {
            // verifier l'existence
            File f = new File(cursor.getString(1));
            // si non existence
            if(!f.exists()){
                // On supprime de la bd
                String DEL_MUSIC = "DELETE FROM " + TABLE_MUSIC + "WHERE "+ KEY_IDMUSIC + " = " + cursor.getString(0);
                db.execSQL(DEL_MUSIC);
            }

        }while (cursor.moveToNext());
        cursor.close();
        // verifier que les musiques du dossier sont présentes dans la BD
        String FilesPosition = FILE_PATH;
        File folder = new File(FilesPosition);
        File[] listOfMusic = folder.listFiles();
        for (int i = 0; i < listOfMusic.length ; i++) {
            // on fait une requete SQL pour verifier si la musique existe déja
            String TEST = "SELECT * FROM " + TABLE_MUSIC + " WHERE " + KEY_POSITION + " = " + listOfMusic[i].getPath();
            cursor = db.rawQuery(TEST, null);
            if (cursor.getCount() == 0) {
                // si non, on ajoute cette musique à la BD
                String position = listOfMusic[i].getPath();
                long size = listOfMusic[i].length();
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(position);
                String length = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                int duration = Integer.parseInt(length);
                String name = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
                String artist = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
                Music music = new Music(0, position, size, duration, name, artist);
                registerMusic(music);
            }
        }
        db.close();
    }

    // Delete a Music - depending of the ID of the music
    public void deleteMusic(Music music){
        // test if the music exists
        String TEST_EXISTS_MUSIC = "SELECT "+ KEY_IDMUSIC +" FROM " + TABLE_MUSIC + " WHERE " + KEY_NAME + " = " + music.getName();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(TEST_EXISTS_MUSIC, null);

        if(!cursor.moveToNext()){
            cursor.moveToFirst();
            int idToDel = cursor.getColumnIndex(KEY_IDMUSIC);
            String DEL_MUSIC = "DELETE FROM " + TABLE_MUSIC + "WHERE "+ KEY_IDMUSIC + " = " + idToDel;
            db.execSQL(DEL_MUSIC);
        }
        cursor.close();
        db.close();
    }
    
    // Add a Music
    public void registerMusic(Music music){
        SQLiteDatabase bd = this.getWritableDatabase();
        String MUSIC_ADD = "INSERT INTO " + TABLE_MUSIC + "("+ KEY_POSITION +","+ KEY_SIZE +", "+ KEY_LENGTH +", "+ KEY_NAME +","+ KEY_ARTIST +" ) " +
                "VALUES ("+ music.getPosition() +","+ music.getSize() +","+ music.getLength() +","+music.getName()+","+ music.getArtist() +")";
        bd.execSQL(MUSIC_ADD);
        bd.close();
    }

    // Add a Playlist
    public void registerPlaylist(Playlist playlist){
        SQLiteDatabase bd = this.getWritableDatabase();
        String PLAYLIST_ADD = "INSERT INTO "+ TABLE_PLAYLIST +" (playlistname, description) VALUES ("+playlist.getPlaylistname()+
                ","+playlist.getDescription()+") ";
        bd.execSQL(PLAYLIST_ADD);
    }

    // Add a Music in a Playlist
    public void addMusicInPlaylist(Music music, Playlist playlist){
        SQLiteDatabase bd = this.getWritableDatabase();
        String VERIFY = "SELECT * FROM " + TABLE_HAVE + "WHERE " + KEY_IDMUSIC + " = " + music.getIdMusic() + " AND " +
                KEY_IDPLAYLIST + " = " + playlist.getIdPlaylist();
        Cursor cursor = bd.rawQuery(VERIFY, null);
        if (!cursor.moveToNext()) {
            String ADD = "INSERT INTO " + TABLE_HAVE + " ("+ KEY_IDPLAYLIST +", "+ KEY_IDMUSIC +") VALUES("+ music.getIdMusic() +", "+ playlist.getIdPlaylist() +")";
            bd.execSQL(ADD);
        }
        bd.close();
    }

    // TODO
    public MusicList getMusicList(){
        MusicList ml = new MusicList();
        SQLiteDatabase bd = this.getWritableDatabase();
        String GETMUSIC = "SELECT * FROM " + TABLE_MUSIC ;
        Cursor cursor = bd.rawQuery(GETMUSIC, null);
        do {
            // create Music with elements given by request
            int idmusic = cursor.getInt(0);
            String position = cursor.getString(1);
            long size = cursor.getLong(2);
            int length = cursor.getInt(3);
            String name = cursor.getString(4);
            String artist = cursor.getString(5);
            Music m = new Music(idmusic, position, size, length, name, artist);
            // add Music to MusicList
            ml.getMusiclist().add(m);
            // increment number of Music
            ml.setMusicnumber(ml.getMusicnumber()+1);
            // Increase length of playing time
            ml.setLength(ml.getLength() + m.getLength());
        }while (cursor.moveToNext());
        return ml;
    }

    public MusicList getPlaylist(int idplaylist){
        MusicList ml = new MusicList();
        SQLiteDatabase bd = this.getWritableDatabase();
        String GETMUSIC = "SELECT * FROM " + TABLE_MUSIC + " WHERE " + KEY_IDMUSIC + "= (SELECT "+KEY_IDMUSIC
                +"FROM "+TABLE_PLAYLIST+" WHERE "+KEY_IDPLAYLIST+" = "+idplaylist+")";
        Cursor cursor = bd.rawQuery(GETMUSIC, null);
        do {
            // create Music with elements given by request
            int idmusic = cursor.getInt(0);
            String position = cursor.getString(1);
            long size = cursor.getLong(2);
            int length = cursor.getInt(3);
            String name = cursor.getString(4);
            String artist = cursor.getString(5);
            Music m = new Music(idmusic, position, size, length, name, artist);
            // add Music to MusicList
            ml.getMusiclist().add(m);
            // increment number of Music
            ml.setMusicnumber(ml.getMusicnumber()+1);
            // Increase length of playing time
            ml.setLength(ml.getLength() + m.getLength());
        }while (cursor.moveToNext());
        return ml;
    }

    // Clear the table
    public void clear() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_HAVE);
        db.execSQL("DELETE FROM " + TABLE_PLAYLIST);
        db.execSQL("DELETE FROM " + TABLE_MUSIC);
        db.close();
    }
}
