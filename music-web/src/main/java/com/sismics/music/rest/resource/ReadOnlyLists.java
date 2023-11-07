package com.sismics.music.rest.resource;

import com.sismics.music.core.model.dbi.Playlist;
import com.sismics.music.core.model.dbi.PlaylistDecorator;
import com.sismics.music.core.model.dbi.PlaylistReadOnlyDecorator;

import javax.json.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ReadOnlyLists {

    private static ArrayList<PlaylistReadOnlyDecorator> readOnlyLists = new ArrayList<PlaylistReadOnlyDecorator>();

    public ReadOnlyLists() {
        System.out.println("hello from publiclists class \n");
        ReadOnlyLists.readFromFile();
    }

    public static void add(PlaylistReadOnlyDecorator playlistReadOnlyDecorator) {

        readOnlyLists.add(playlistReadOnlyDecorator);
        ReadOnlyLists.writeToFile();
    }

    public static ArrayList<PlaylistReadOnlyDecorator> getReadOnlyLists() {
        return readOnlyLists;
    }

    public static boolean contains(String id) {
        boolean result;
        for (PlaylistReadOnlyDecorator playlist : readOnlyLists) {
            if (playlist.getId().equals(id))
                return true;
        }
        return false;
    }

    public static String getUserId(String id) {
        String result = "";
        for (PlaylistReadOnlyDecorator playlist : readOnlyLists) {
            if (playlist.getId().equals(id))
                return playlist.getUserId();
        }
        return result;
    }

    public static void remove(String id)
    {
        for (int i = 0; i < readOnlyLists.size(); i++) {
            PlaylistReadOnlyDecorator obj = readOnlyLists.get(i);
            if (obj.getId().equals(id)) {
                readOnlyLists.remove(i);
                break;
            }
        }
        ReadOnlyLists.writeToFile();
    }

    public static void writeToFile()  {
        try {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (PlaylistReadOnlyDecorator playlist : readOnlyLists) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", playlist.getId())
                        .add("name", playlist.getName())
                        .add("userId", playlist.getUserId())
                        .add("type",playlist.getType()));
            }
            JsonArray jsonArray = arrayBuilder.build();
            String jsonString = jsonArray.toString();

            // Write the string to a file
            FileWriter fileWriter = new FileWriter("readOnlyLists.json");
            fileWriter.write(jsonString);
            fileWriter.close();
            System.out.println("Inside write to file\n");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public static void readFromFile() {
        ArrayList<PlaylistReadOnlyDecorator> pLists=new ArrayList<PlaylistReadOnlyDecorator>();
        try{
            String jsonString = new String(Files.readAllBytes(Paths.get("readOnlyLists.json")));
            JsonReader reader = Json.createReader(new StringReader(jsonString));
            JsonArray jsonArray = reader.readArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.getJsonObject(i);
                Playlist obj=new Playlist(jsonObject.getString("id"),jsonObject.getString("userId"),jsonObject.getString("name"));
                PlaylistReadOnlyDecorator playlistReadOnlyDecorator=new PlaylistReadOnlyDecorator(obj);
                pLists.add(playlistReadOnlyDecorator);
            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("inside readfile "+e);
        }catch (Exception e) {
            //e.printStackTrace();
            System.out.println("inside readfile Exception"+e);
        }
        readOnlyLists=pLists;
    }
}
