package com.team25.hackathon;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.preflight.Format;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.exception.ValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.text.PDFTextStripper;

public class pdfEngineer {

	PDDocument pdfDoc1;
	File pdfFile;

	// Default constructor
	public pdfEngineer() {

	}

	// Parameterized constructor for single PDF file
	public pdfEngineer(File pdfFile) {

		this.pdfFile = pdfFile;
		try {
			// Load PDF file
			pdfDoc1 = Loader.loadPDF(this.pdfFile);
		} catch (Exception e) {
			System.out.println("ERROR: File could not be loaded " + e);
		}

	}

	// Parameterized constructor to load PDF from give URL
	public pdfEngineer(URL pdfURL) {

		try {
			InputStream is = pdfURL.openStream();
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] temp = bis.readAllBytes();
			pdfDoc1 = Loader.loadPDF(temp);
		} catch (Exception e) {
			System.out.println("ERROR: File could not be loaded from URL " + e);
			System.out.println("Please recheck the given URL: " + pdfURL);
		}

	}

	// Return all the text from PDF as String
	public String extractText() {
		PDFTextStripper stripper = new PDFTextStripper();
		String text = "";
		try {
			text = stripper.getText(pdfDoc1);
		} catch (Exception e) {
			System.out.println("ERROR: Text could not be extracted from file " + e);
		}
		return text;
	}

	// Validate PDF according to provided format
	public void validate(Format documentFormat) throws IOException {
		ValidationResult result = null;
		PreflightParser parser = new PreflightParser(pdfFile);
		parser.parse(documentFormat);
		PreflightDocument document = parser.getPDDocument();
		document.validate();
		result = document.getResult();
		System.out.println(result);
		

	}
}
