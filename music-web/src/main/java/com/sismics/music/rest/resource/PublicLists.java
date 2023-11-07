package com.sismics.music.rest.resource;

import com.sismics.music.core.model.dbi.Playlist;
import com.sismics.music.core.model.dbi.PlaylistDecorator;

import javax.json.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PublicLists{
    private static ArrayList<PlaylistDecorator> publicLists = new ArrayList<PlaylistDecorator>();

    public PublicLists() {
        System.out.println("hello from publiclists class \n");
        PublicLists.readFromFile();
    }

    public static void add(PlaylistDecorator playlistDecorator) {

        publicLists.add(playlistDecorator);
        PublicLists.writeToFile();
    }

    public static ArrayList<PlaylistDecorator> getPublicLists() {
        return publicLists;
    }

    public static boolean contains(String id) {
        boolean result;
        for (PlaylistDecorator playlist : publicLists) {
            if (playlist.getId().equals(id))
                return true;
        }
        return false;
    }

    public static String getUserId(String id) {
        String result = "";
        for (PlaylistDecorator playlist : publicLists) {
            if (playlist.getId().equals(id))
                return playlist.getUserId();
        }
        return result;
    }

    public static void remove(String id)
    {
        for (int i = 0; i < publicLists.size(); i++) {
            PlaylistDecorator obj = publicLists.get(i);
            if (obj.getId().equals(id)) {
                publicLists.remove(i);
                break;
            }
        }
        PublicLists.writeToFile();
    }

    public static void writeToFile()  {
        try {
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            for (PlaylistDecorator playlist : publicLists) {
                arrayBuilder.add(Json.createObjectBuilder()
                        .add("id", playlist.getId())
                        .add("name", playlist.getName())
                        .add("userId", playlist.getUserId())
                        .add("type",playlist.getType()));
            }
            JsonArray jsonArray = arrayBuilder.build();
            String jsonString = jsonArray.toString();

            // Write the string to a file
            FileWriter fileWriter = new FileWriter("publiclists.json");
            fileWriter.write(jsonString);
            fileWriter.close();
            System.out.println("Inside write to file\n");
        } catch(Exception e) {
            System.out.println(e);
        }
    }
    public static void readFromFile() {
        ArrayList<PlaylistDecorator> pLists=new ArrayList<PlaylistDecorator>();
        try{
            String jsonString = new String(Files.readAllBytes(Paths.get("publiclists.json")));
            JsonReader reader = Json.createReader(new StringReader(jsonString));
            JsonArray jsonArray = reader.readArray();
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject jsonObject = jsonArray.getJsonObject(i);
                Playlist obj=new Playlist(jsonObject.getString("id"),jsonObject.getString("userId"),jsonObject.getString("name"));
                PlaylistDecorator playlistDecorator=new PlaylistDecorator(obj,true);
                pLists.add(playlistDecorator);
            }
        }catch (IOException e) {
            //e.printStackTrace();
            System.out.println("inside readfile "+e);
        }catch (Exception e) {
            //e.printStackTrace();
            System.out.println("inside readfile Exception"+e);
        }
        publicLists=pLists;
    }
}
