package alex.ethier.dogear;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.*;
//import java.util.ArrayList;

/**
 * Hello world!
 * 
 */
public class App {
	public static void main(String[] args) throws IOException, JSONException, InterruptedException {
		//!!!!!!! IMPORTANT TRY TO SEE HOW TO INCORPORATE LUCENE
		//!!!!!!! May want to package project as RPM - see Maven rpm plugin
		
		/*
		 * for (int i = 0; i < args.length; i++) {
		 * System.out.println(args[i].toString()); }
		 * System.out.println("Hello World2!");
		 */
		if(args.length == 0) {
			System.out.println("Remember to add args:");
			System.out.println("store");
			System.out.println("load");
			
			System.exit(0);
		}
		
		if (args[0].equals("store")) {
			StoreWriter storeWriter = new StoreWriter("/home/alex/playground/my_projects/deploy/executables/dogear/data/psd.txt");
			storeWriter.addUserData();
		} else if (args[0].equals("load")) {
			StoreReader storeReader = new StoreReader("/home/alex/playground/my_projects/deploy/executables/dogear/data/psd.txt");
			storeReader.runUserQuery();
		}
	}
}
