package alex.ethier.dogear;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoreWriter {
	
	public PrintWriter writer;

	public StoreWriter(String url) throws IOException {
		//Use url to determine where to write to.
		writer = new PrintWriter(new BufferedWriter(new FileWriter(url, true)));		
	}

	public void addUserData() throws IOException, JSONException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		String tagString = "";
		System.out.print("Enter Tags:");

		tagString = reader.readLine();//Read in tags

		String[] tags = tagString.split(" ");//Convert to array

		String resource = "";
		System.out.print("Enter Resource:");
		resource = reader.readLine();//Read in resource
		reader.close();

		JSONObject root = new JSONObject();
		root.put("resource", resource);
		root.put("tags", new JSONArray(tags));
		
		writer.println(root.toString());
		writer.close();
	}
}
