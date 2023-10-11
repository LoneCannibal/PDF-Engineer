package com.team25.hackathon;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.pdfbox.preflight.Format;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.testng.Reporter;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

public class PdfEngineerTestCases {

	PdfEngineer p1, p2, p3;
	String userDirectory;

	@BeforeMethod
	public void initialization() {
		File f1 = new File(userDirectory + "/resources/sample1.pdf");
		p1 = new PdfEngineer(f1);
	}

	@BeforeSuite
	public void setup() {
		userDirectory = System.getProperty("user.dir");
	}

	// Open PDF using PdfEngineer with filename/path
	@Test
	public void openFile() {
		Reporter.log("PDF loaded from path");
	}

	// Open PDF from a provided URL
	@Test
	public void openURL() throws MalformedURLException {
		URL url = new URL("https://download.gigabyte.com/FileList/Manual/mb_manual_z490m_e.pdf");
		p1 = new PdfEngineer(url);
		Reporter.log("PDF loaded from given URL");
	}

	// Read text from a PDF
	@Test
	public void readText() {
		System.out.println(p1.extractText());
		Reporter.log("PDF text extracted");
	}

	// Comparison of two PDF files
	@Test
	public void comparison() {
		// File f1 = new File(userDirectory + "/resources/sample1.pdf");
		File f2 = new File(userDirectory + "/resources/sample2.pdf");
		// p1 = new PdfEngineer(f1);
		p2 = new PdfEngineer(f2);

		boolean result = p1.simpleCompare(p2);
		Reporter.log("Do files have same content? " + result);
	}

	// Check if the PDF file is valid according to standards
	@Test
	public void validation() throws Exception {
		List<ValidationError> validationErrors = p1.validatePDF(Format.PDF_A1B);
		for (ValidationResult.ValidationError ve : validationErrors) {
			System.out.println(ve.getErrorCode() + ": " + ve.getDetails() + ". Found at Page No." + ve.getPageNumber());
		}
		Reporter.log("PDF validation errors are successfully displayed");
	}

	// Convert PDF to standard format
	@Test
	public void convert() throws Exception {
		p1.convertToFormat(Format.PDF_A1A, userDirectory + "/output/validated.pdf");
		Reporter.log("PDF was converted to A1A format");
	}

	// Search for phrase in PDF
	@Test
	public void search() {
		String searchPhrase = "this mistaken idea of denouncing pleasure and praising pain";
		boolean isFound = p1.findInPdf(searchPhrase);
		Reporter.log("Phrase: " + searchPhrase + " is found? " + isFound);
	}

	// Encrypt PDF with a given password
	@Test
	public void encryptWithPassword() {
		String password = "secret123";
		File encrypt = new File(userDirectory + "/output/encrypted.pdf");
		p1.encryptPdf(password, encrypt);
		Reporter.log("PDF successfully encrypted");
	}

	// Merge two PDF files
	@Test
	public void mergeFiles() {
		File merged = new File(userDirectory + "/output/merged.pdf");
		File f3 = new File(userDirectory + "/resources/sample3.pdf");
		p3 = new PdfEngineer(f3);
		p1.mergePdf(p3, merged);
		Reporter.log("PDF files merged into single file: " + merged.toString());
	}

}
