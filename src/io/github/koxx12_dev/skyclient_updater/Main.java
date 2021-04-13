package io.github.koxx12_dev.skyclient_updater;




import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class Main{

    public static void main(String[] args) throws IOException {
        List<String> namesJson = new ArrayList<>();
        List<String> to_be_updated_fn = new ArrayList<>();
        List<String> to_be_updated_n = new ArrayList<>();
        HashMap<String,String> name_to_id = new HashMap<>();

        int errors = 0;

        PrintStream out = new PrintStream(new FileOutputStream("update.txt"));
        System.setOut(out);

        String is = request("https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/mods.json");

        String fldmds = System.getenv("APPDATA") + "\\.minecraft\\skyclient\\mods";

        String[] filelist = new File(fldmds).list();

        if (filelist == null) {
            System.exit(0);
        }

        JSONArray arry = new JSONArray(is);
        //.out.println(arry.length());
        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            assert false;
            namesJson.add(String.valueOf(obj.get("file")));
        }

        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            assert false;
            name_to_id.put(String.valueOf(obj.get("file")),String.valueOf(obj.get("id")));
        }

        for (int i = 0; i < filelist.length; i++) {
            //System.out.println(filelist.length);
            List<Integer> tstarray = new ArrayList<>();
            for (int x = 0; x < namesJson.size(); x++) {
                tstarray.add(LevenshteinDistanceDP.compute_Levenshtein_distanceDP(""+filelist[i],""+namesJson.get(x)));
            }
            for (int y = 0; y < tstarray.size(); y++) {
                int z = tstarray.stream().mapToInt(v -> v).min().orElse(Integer.MAX_VALUE);
                //System.out.println(vall+" "+tstarray.get(y)+","+namesJson.get(y)+" "+filelist[i]);
                //System.out.println(vall +" "+ tstarray.get(y));
                if (tstarray.get(y) == z) {
                    if (!(z < 4) && (z < 15)){
                        to_be_updated_fn.add(filelist[i]);
                        to_be_updated_n.add(namesJson.get(y));
                    }
                }
            }
            //System.out.println(tstarray+"\n"+tstarray.size());
        }

        for (int i = 0; i < to_be_updated_n.size(); i++) {
            for (int x = 0; x < namesJson.size(); x++) {
                try {
                    //System.out.println(namesJson.get(x)+" "+to_be_updated_n.get(i));
                    if (("" + namesJson.get(x)).equals("" + to_be_updated_n.get(i))) {
                        //System.out.println(namesJson.get(x) + " " + to_be_updated_n.get(i));

                        JSONObject obj = new JSONObject(arry.get(x).toString());
                        String url = (String) obj.get("url");
                        String dpname = (String) obj.get("display");

                        File todel = new File(fldmds+"\\"+to_be_updated_fn.get(i));

                        //System.out.println(url+","+fldmds+"\\"+namesJson.get(x)+","+to_be_updated_fn.get(i));

                        try {
                            if (!url.contains("sk1er")) {
                                FileUtils.copyURLToFile(new URL(url), new File(fldmds + "\\" + namesJson.get(x)));
                                todel.delete();
                                System.out.println("Updated: " + dpname);
                                System.out.println("From: " + to_be_updated_fn.get(i) + "\n" + "To: " + to_be_updated_n.get(i));
                            } else {
                                System.out.println("Ignoring: " + to_be_updated_fn.get(i) + "\n" + "Reason: Downloading sk1er's is broken for me (always 403)");
                            }
                        } catch (IOException e) {
                            e.getStackTrace();
                            System.out.println("Failed to download "+namesJson.get(x)+"\n Try again later");
                            errors = errors + 1;
                        }

                    }
                } catch (Exception e) {
                    System.out.println("Exception at: "+x);
                }
            }
        }


        if (errors == 1) {
            System.out.println("Done\n"+errors+" download failed");
        } else {
            System.out.println("Done\n"+errors+" downloads failed");
        }


        //assert false;
        //System.out.println(Arrays.toString(filelist));
    }

    public static String request(String URL) throws IOException {
        URL url = new URL(URL);
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}