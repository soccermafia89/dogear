package alex.ethier.dogear;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CopyOfStoreReader {
	Map<String, Set<String>> resources;

	public CopyOfStoreReader(String url) throws IOException, JSONException {
		resources = new HashMap<String, Set<String>>();

		BufferedReader bufferedReader = new BufferedReader(new FileReader(url));

		String line = bufferedReader.readLine();
		while (line != null) {
			JSONObject lineJSON = new JSONObject(line);

			String resource = lineJSON.getString("resource");

			JSONArray tagsJSON = lineJSON.getJSONArray("tags");
			Set<String> tags = new HashSet<String>();
			for (int i = 0; i < tagsJSON.length(); i++) {
				String tag = tagsJSON.getString(i);
				tags.add(tag);
			}

			resources.put(resource, tags);

			line = bufferedReader.readLine();
		}

		bufferedReader.close();
	}

	public void runUserQuery() throws IOException, InterruptedException {
		//Get user search parameters
		BufferedReader inputReader = new BufferedReader(new InputStreamReader(
				System.in));

		String tagString = "";
		System.out.print("Enter Tags:");

		tagString = inputReader.readLine();//Read in tags

		String[] tagsArray = tagString.split(" ");//Convert to array

		Set<String> tagsSet = new HashSet<String>();
		for (int i = 0; i < tagsArray.length; i++) {
			tagsSet.add(tagsArray[i]);
		}

		//Execute search
		ArrayList<String> foundResources = this.searchResources(tagsSet);

		//Report output
		System.out.println("Number of results: " + foundResources.size());
		System.out.println("");

		int cursor = 0;
		System.out.println(foundResources.get(cursor));

		while (true) {

			//Use raw console input mode, each character press will send data to the reader
			String[] raw = { "/bin/sh", "-c", "stty raw < /dev/tty" };
			Runtime.getRuntime().exec(raw).waitFor();

			//Turn echo off, user typing will not be displayed (and will not be sent to reader's buffer)
			String[] echoOff = { "/bin/sh", "-c", "stty -echo < /dev/tty" };
			Runtime.getRuntime().exec(echoOff).waitFor();

			//Load text from the buffer into a char[]
			char[] charInput = new char[1];
			inputReader.read(charInput);

			//Reset the tty
			String[] cooked = { "/bin/sh", "-c", "stty cooked < /dev/tty" };
			Runtime.getRuntime().exec(cooked).waitFor();

			String[] echoOn = { "/bin/sh", "-c", "stty echo < /dev/tty" };
			Runtime.getRuntime().exec(echoOn).waitFor();

			//Convert the char[] into a string

			String inputString = Character.toString(charInput[0]);

			if (inputString.equals("a")) {
				if (cursor > 0) {
					cursor--;
				} else {//Wrap the cursor
					cursor = foundResources.size() - 1;
				}
			} else if (inputString.equals("d")) {
				if (cursor == foundResources.size() - 1) {
					cursor = 0;
				} else {
					cursor++;
				}
			} else if (inputString.equals("q")) {
				break;
			} else {
				//System.out.println("Found: " + charInput[0]);
				System.out.println("");
				System.out.println("Enter a valid character!");
				System.out.println("");
				System.out.println("q: Quit");
				System.out.println("a: Move Left");
				System.out.println("d: Move Right");

				while (inputReader.ready()) {//Clear the input reader's buffer
					inputReader.read();
				}

				continue;//Do not print out a result.
			}

			System.out.println("");
			System.out.println(foundResources.get(cursor));
		}
	}

	//Returns an ordered list of the best matches
	public ArrayList<String> searchResources(Set<String> tagSet) {
		ArrayList<String> foundResources = new ArrayList<String>();

		TreeMap<String, Integer> foundResourcesTree = new TreeMap<String, Integer>();

		resources.keySet();

		for (String resource : resources.keySet()) {
			Set<String> resourceTags = resources.get(resource);
			Set<String> tags = new HashSet<String>(resourceTags);

			tags.retainAll(tagSet);//tags is now the intersection of resourceTags and tagSet
			if (tags.size() > 0) {
				foundResourcesTree.put(resource, tags.size());
			}
		}

		Set<String> orderedList = foundResourcesTree.descendingKeySet();
		for (String resource : orderedList) {
			foundResources.add(resource);
		}

		//If no matches add a single empty resource
		if (foundResources.size() == 0) {
			foundResources.add("No matches!");
		}

		return foundResources;
	}
}
