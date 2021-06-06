package io.github.koxx12_dev.skyclient_updater;

import org.apache.commons.lang3.SystemUtils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public abstract class OSBase {

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

    public static String request(String URL) throws IOException {
        java.net.URL url = new URL(URL);
        URLConnection conn = url.openConnection();
        InputStream inputStream = conn.getInputStream();
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public static List<ModAndID> getModsThatNeedUpdating(String[] localFiles, List<ModAndID> repoFiles, List<ModAndID> skyclientJsonFiles) {

        // so far nothing is known
        List<ModAndID> trackedMods = new ArrayList<>();
        List<String> untrackedMods = new ArrayList<>(Arrays.asList(localFiles));

        List<ModAndID> modsThatNeedUpdating = new ArrayList<>();
        // the mods are in SkyClient.json -> nothing changed therefore they are known
        for (ModAndID mod : skyclientJsonFiles) {
            if (untrackedMods.contains(mod.file)) {
                untrackedMods.remove(mod.file);
                trackedMods.add(mod);
            }
        }

        for (ModAndID item : repoFiles) {
            if (untrackedMods.size() == 0)
                break;
            if (untrackedMods.contains(item.file)) {
                boolean contained = false;
                for (ModAndID data : skyclientJsonFiles) {
                    if (data.id.equals(item.id)) {
                        data.file = item.file;
                        contained = true;
                        untrackedMods.remove(item.file);
                        trackedMods.add(data);
                        break;
                    }
                }
                if (!contained) {
                    untrackedMods.remove(item.file);
                    trackedMods.add(item);
                    skyclientJsonFiles.add(item);
                }
            }
        }


        // skyclient.json may not have everything, but surely the repo will know
        for (ModAndID item : repoFiles) {
            if (untrackedMods.size() == 0)
                break;
            if (untrackedMods.contains(item.file)) {
                boolean contained = false;
                for (ModAndID data : skyclientJsonFiles) {
                    if (data.id.equals(item.id)) {
                        data.file = item.file;
                        contained = true;
                        untrackedMods.remove(item.file);
                        trackedMods.add(data);
                        break;
                    }
                }
                if (!contained) {
                    untrackedMods.remove(item.file);
                    trackedMods.add(item);
                    skyclientJsonFiles.add(item);
                }
            }
        }

        // this is where the lookalike algo gets used
        if (untrackedMods.size() > 0) {
            List<Integer> removeAtIndexes = new ArrayList<>();
            for (ModAndID option : repoFiles) {
                int index = 0;
                for (String file : untrackedMods) {
                    // this is my implementation, it's a modified string distance algo that also measures what they have in common
                /*
                var calc = new LookalikeCalculator(option.File, file);
                var diff = calc.SimilaritiesAndDifferences.Total;
                if (diff <= Globals.Settings.similaritiesThresholdAdvanced)
                */

                    // this is what yours probably looks like !!!
                    if (LevenshteinDistanceDP.compute_Levenshtein_distanceDP(option.file, file) <= 7) {
                        System.out.println("Lookalike detected: " + option.file + " as " + file);
                        removeAtIndexes.add(index);
                        option.file = file;
                        trackedMods.add(option);
                    }
                    index++;
                }
            }

            removeAtIndexes.sort(Comparator.reverseOrder());

            try {
                for (int i = removeAtIndexes.size() - 1; i >= 0; i--) {
                    int removeAtIndex = removeAtIndexes.get(i);
                    untrackedMods.remove(removeAtIndex);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (ModAndID repoFile : repoFiles) {
                for (ModAndID knownFile : trackedMods) {
                    if (repoFile.id.equals(knownFile.id) && !repoFile.file.equals(knownFile.file)) {
                        modsThatNeedUpdating.add(knownFile);
                    }
                }
            }
        }
        return modsThatNeedUpdating;
    }

    public static String replaceWithOsPath(String path){
        String plc = "sus";
        if (SystemUtils.IS_OS_WINDOWS) {
            plc = "\\\\";
        } else if (SystemUtils.IS_OS_MAC || SystemUtils.IS_OS_LINUX) {
            plc = "/";
        } else {
            System.out.println("HOW TF DID YOU GOT HERE WITH "+System.getProperty("os.name")+"\nIT SHOULDN'T BE POSSIBLE");
            System.exit(-1);
        }

        return path.replaceAll("\\|\\|",plc);
    }

}
