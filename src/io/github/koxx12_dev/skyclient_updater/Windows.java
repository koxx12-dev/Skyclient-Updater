package io.github.koxx12_dev.skyclient_updater;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Windows {
    public static void main(String[] args) throws IOException {
        List<String> namesJson = new ArrayList<>();
        List<String> to_be_updated_fn = new ArrayList<>();
        List<String> to_be_updated_n = new ArrayList<>();
        List<String> to_be_updated_fn_p = new ArrayList<>();
        List<String> to_be_updated_n_p = new ArrayList<>();

        int errors = 0;


        String is = request("https://raw.githubusercontent.com/nacrt/SkyblockClient-REPO/main/files/mods.json");

        String fldmds = System.getenv("APPDATA") + "\\.minecraft\\skyclient\\mods";

        String[] filelist = new File(fldmds).list();

        System.out.println();

        if (filelist == null) {
            System.exit(0);
        }

        JSONArray arry = new JSONArray(is);
        for (int i = 0; i < arry.length(); i++) {
            JSONObject obj = new JSONObject(arry.get(i).toString());
            assert false;
            namesJson.add(String.valueOf(obj.get("file")));
        }

        for (int i = 0; i < filelist.length; i++) {
            List<Integer> tstarray = new ArrayList<>();
            for (int x = 0; x < namesJson.size(); x++) {
                tstarray.add(LevenshteinDistanceDP.compute_Levenshtein_distanceDP(""+filelist[i],""+namesJson.get(x)));
            }
            for (int y = 0; y < tstarray.size(); y++) {
                int z = tstarray.stream().mapToInt(v -> v).min().orElse(Integer.MAX_VALUE);

                if (tstarray.get(y) == z) {
                    if (!(z < 1) && (z < 15)){
                        to_be_updated_fn.add(filelist[i]);
                        to_be_updated_n.add(namesJson.get(y));
                    }
                }
            }

        }

        System.out.println("Found "+to_be_updated_n.size()+" mods that need to be updated");
        for (int i = 0; i < to_be_updated_n.size(); i++) {
            for (int x = 0; x < namesJson.size(); x++) {
                if (("" + namesJson.get(x)).equals("" + to_be_updated_n.get(i))) {
                    JSONObject obj = new JSONObject(arry.get(x).toString());
                    String dpname = (String) obj.get("display");
                    System.out.println("");
                    System.out.println("Do you want to update "+dpname+"\n"+"From: " + to_be_updated_fn.get(i) + "\n" + "To: " + to_be_updated_n.get(i));

                    Scanner sc= new Scanner(System.in); //System.in is a standard input stream
                    System.out.print("[y/n]: ");
                    String str= sc.nextLine();
                    if (str.equalsIgnoreCase("n") || str.equalsIgnoreCase("no")) {

                    } else {
                        to_be_updated_n_p.add(to_be_updated_n.get(i));
                        to_be_updated_fn_p.add(to_be_updated_fn.get(i));
                    }
                }
            }
        }

        for (int i = 0; i < to_be_updated_n_p.size(); i++) {
            for (int x = 0; x < namesJson.size(); x++) {
                try {
                    if (("" + namesJson.get(x)).equals("" + to_be_updated_n_p.get(i))) {
                        JSONObject obj = new JSONObject(arry.get(x).toString());
                        String dpname = (String) obj.get("display");
                        String url;
                        File todel = new File(fldmds+"\\"+to_be_updated_fn_p.get(i));
                        if(obj.get("id") == "rpm") {
                            url = "https://github.com/nacrt/SkyblockClient-REPO/blob/main/files/mods/"+to_be_updated_fn_p.get(i)+"?raw=true";
                        } else {
                            url = (String) obj.get("url");
                        }
                        try {
                            Download(""+url, fldmds + "\\" + namesJson.get(x));
                            todel.delete();
                            System.out.println("");
                            System.out.println("Updated: " + dpname);
                            System.out.println("From: " + to_be_updated_fn_p.get(i) + "\n" + "To: " + to_be_updated_n_p.get(i));
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

        System.out.println("");
        if (errors == 1) {
            System.out.println("Done\n"+errors+" download failed");
        } else {
            System.out.println("Done\n"+errors+" downloads failed");
        }

    }

    public static String request(String URL) throws IOException {
        java.net.URL url = new URL(URL);
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static void Download(String URL, String Loc) throws IOException {
        java.net.URL url = new URL(URL);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setRequestProperty("User-Agent", "NING/1.0");
        BufferedInputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
        FileOutputStream fileOS = new FileOutputStream(Loc);
        byte[] data = new byte[1024];
        int byteContent;
        while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
            fileOS.write(data, 0, byteContent);
        }
    }
}
