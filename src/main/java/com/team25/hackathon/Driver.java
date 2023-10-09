package com.team25.hackathon;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.pdfbox.preflight.Format;

public class Driver {
	
	public static void main(String[] args) throws Exception{
		
		String FILE_PATH = "C:\\Users\\admin\\eclipse-workspace\\DA_Hackathon\\hackathon\\resources\\validated.pdf";
		
		//File f1 = new File(FILE_PATH);
		//pdfEngineer p1 = new pdfEngineer(f1);
		//System.out.println(p1.extractText());
		
		URL tempURL = new URL("https://ncertbooks.solutions/wp-content/uploads/2020/01/leac101.pdf");
		pdfEngineer p2 = new pdfEngineer(tempURL);
		System.out.println(p2.extractText());
		
		pdfEngineer p3 = new pdfEngineer(tempURL);
		//System.out.println(p3.simpleCompare(p2));
		//System.out.println(p3.simpleCompare(p3));
		
		File f1 = new File(FILE_PATH);
		pdfEngineer p4 = new pdfEngineer(f1);
		p4.validatePDF(Format.PDF_A1A);
		
		p3.convertToFormat(Format.PDF_A1A, "C:\\\\Users\\\\admin\\\\eclipse-workspace\\\\DA_Hackathon\\\\hackathon\\\\resources\\\\generated.pdf");
		
		
	}

}
