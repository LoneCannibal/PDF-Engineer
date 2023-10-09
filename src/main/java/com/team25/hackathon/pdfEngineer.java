package com.team25.hackathon;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.preflight.Format;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.exception.ValidationException;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.text.PDFTextStripper;

import com.spire.pdf.conversion.PdfStandardsConverter;

public class pdfEngineer {

	private PDDocument pdfDoc1;
	private File pdfFile;

	// Default constructor
	public pdfEngineer() {

	}

	// Parameterized constructor for single PDF file
	public pdfEngineer(File pdfFile) {

		this.pdfFile = pdfFile;
		try {
			// Load PDF file
			pdfDoc1 = PDDocument.load(this.pdfFile);
		} catch (Exception e) {
			System.out.println("ERROR: File could not be loaded " + e);
		}

	}

	// Parameterized constructor to load PDF from give URL
	public pdfEngineer(URL pdfURL) {

		try {
			InputStream is = pdfURL.openStream();
			byte[] temp = is.readAllBytes();
			pdfDoc1 = PDDocument.load(temp);
		} catch (Exception e) {
			System.out.println("ERROR: File could not be loaded from URL " + e);
			System.out.println("Please recheck the given URL: " + pdfURL);
		}

	}

	// Return all the text from PDF as String
	public String extractText() {
		String text = "";

		try {
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(pdfDoc1);
		} catch (Exception e) {
			System.out.println("ERROR: Text could not be extracted from file " + e);
		}
		return text;
	}

	// Compare PDF files with each other based only on text
	// Both files need to have same text to be equal
	public boolean simpleCompare(pdfEngineer pdf2) {
		if (extractText().equals(pdf2.extractText()))
			return true;
		return false;

	}

	// Validate PDF according to provided format
	public void validatePDF(Format documentFormat) throws Exception {
		ValidationResult result = null;
		PreflightParser parser = new PreflightParser(pdfFile);
		parser.parse(documentFormat);
		PreflightDocument documentTemp = parser.getPreflightDocument();
		documentTemp.validate();
		documentTemp.close();
		result = documentTemp.getResult();
		
		if(result.getErrorsList().size() > 0) {
			for(ValidationResult.ValidationError ve: result.getErrorsList()) {
				System.out.println(ve.getErrorCode()+": "+ve.getDetails()+": "+ve.getPageNumber());
				//System.out.println(ve.toString());
			}
		}
		System.out.println(result.toString());

	}
	
	//Convert PDF to given Format
	public void convertToFormat(Format documentFormat, String outputFile) throws Exception {
		FileInputStream isr = new FileInputStream(pdfFile);
		PdfStandardsConverter converter = new PdfStandardsConverter(isr);
		converter.toPdfA1A(outputFile);
	}
}
