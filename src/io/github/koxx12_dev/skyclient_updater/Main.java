package io.github.koxx12_dev.skyclient_updater;




import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main{
    
    

    public static void main(String[] args) throws IOException {
        List<String> namesJson = new ArrayList<>();
        int val = LevenshteinDistanceDP.compute_Levenshtein_distanceDP("aaa","aaaa");
        System.out.println(val);

        String is = request("https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/mods.json");

        JSONArray arry = new JSONArray(is);
        System.out.println(arry.length());
        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            assert false;
            namesJson.add(String.valueOf(obj.get("file")));
        }

        System.out.println(arry.length());
        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            assert false;
            namesJson.add(String.valueOf(obj.get("file")));
        }

        assert false;
        System.out.println(namesJson);

    }

    public static String request(String URL) throws IOException {
        URL url = new URL(URL);
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}