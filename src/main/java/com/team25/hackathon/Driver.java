package com.team25.hackathon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Driver {
	
	public static void main(String[] args) throws MalformedURLException{
		
		String FILE_PATH = "C:\\Users\\aakas\\Downloads\\(www.entrance-exam.net)-BEL Placement Sample Paper 1.pdf";
		
		File f1 = new File(FILE_PATH);
		pdfEngineer p1 = new pdfEngineer(f1);
		//System.out.println(p1.extractText());
		
		URL tempURL = new URL("https://ncertbooks.solutions/wp-content/uploads/2020/01/leac101.pdf");
		pdfEngineer p2 = new pdfEngineer(tempURL);
		System.out.println(p2.extractText());
	}

}
