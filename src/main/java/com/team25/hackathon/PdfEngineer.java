package com.team25.hackathon;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.AccessPermission;
import org.apache.pdfbox.pdmodel.encryption.StandardProtectionPolicy;
import org.apache.pdfbox.preflight.Format;
import org.apache.pdfbox.preflight.PreflightDocument;
import org.apache.pdfbox.preflight.ValidationResult;
import org.apache.pdfbox.preflight.ValidationResult.ValidationError;
import org.apache.pdfbox.preflight.parser.PreflightParser;
import org.apache.pdfbox.text.PDFTextStripper;

import com.spire.pdf.conversion.PdfStandardsConverter;

public class PdfEngineer {

	private PDDocument pdfDoc1;
	private File pdfFile;
	private File TEMP_DOWNLOAD_FILE; // Temporary file generated when a PDF is opened using URL.

	// Default constructor
	public PdfEngineer() {

	}

	// Parameterized constructor for single PDF file
	public PdfEngineer(File pdfFile) {

		this.pdfFile = pdfFile;
		try {
			// Load PDF file
			pdfDoc1 = PDDocument.load(this.pdfFile);
		} catch (Exception e) {
			System.out.println("ERROR: File could not be loaded " + e);
		}

	}

	// Parameterized constructor to load PDF from give URL
	// The Download PDF is stored in the temp directory
	public PdfEngineer(URL pdfURL) {

		String userDirectory = System.getProperty("user.dir");
		// Random file name for downloaded file is generated and stored in temp
		// directory
		TEMP_DOWNLOAD_FILE = new File(userDirectory + "/temp/" + System.currentTimeMillis() + ".pdf");
		try {
			FileUtils.copyURLToFile(pdfURL, TEMP_DOWNLOAD_FILE, 10000, 1000000);
			pdfFile = TEMP_DOWNLOAD_FILE;
			pdfDoc1 = PDDocument.load(TEMP_DOWNLOAD_FILE);

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
	public boolean simpleCompare(PdfEngineer pdf2) {
		if (extractText().equals(pdf2.extractText()))
			return true;
		return false;

	}

	// Validate PDF according to provided format and return the validation errors
	public List<ValidationError> validatePDF(Format documentFormat) throws Exception {
		ValidationResult result = null;
		PreflightParser parser = new PreflightParser(pdfFile);

		parser.parse(documentFormat);
		PreflightDocument documentTemp = parser.getPreflightDocument();
		documentTemp.validate();
		documentTemp.close();
		result = documentTemp.getResult();

		return result.getErrorsList();

	}

	// Convert PDF to given Format
	public void convertToFormat(Format documentFormat, String outputFile) throws Exception {
		InputStream isr = new DataInputStream(new FileInputStream(pdfFile));
		PdfStandardsConverter converter = new PdfStandardsConverter(isr);
		switch (documentFormat) {
		case PDF_A1A:
			converter.toPdfA1A(outputFile);
			break;
		case PDF_A1B:
			converter.toPdfA1B(outputFile);
			break;
		default:
			converter.toPdfA1B(outputFile);
		}
	}

	// Search for word of phrase returns true if found
	public boolean findInPdf(String phrase) {
		if (this.extractText().contains(phrase)) {
			return true;
		}
		return false;
	}

	// Encrypt PDF with a given password with a given filename
	public void encryptPdf(String password, File fileName) {
		AccessPermission accessPermission = new AccessPermission();
		StandardProtectionPolicy spp = new StandardProtectionPolicy(password, password, accessPermission);
		spp.setEncryptionKeyLength(128);
		spp.setPermissions(accessPermission);
		try {
			pdfDoc1.protect(spp);
			pdfDoc1.save(fileName);
			pdfDoc1.close();
		} catch (Exception e) {
			System.out.println("ERROR: File could not be encrypted " + e);
		}
	}

	// Merge PDF. Provide two PdfEngineer objects and merge them into single output
	// PDF file
	public void mergePdf(PdfEngineer pdf2, File output) {
		PDFMergerUtility PDFmerger = new PDFMergerUtility();
		try {
			PDFmerger.setDestinationFileName(output.toString());
			PDFmerger.addSource(this.getFile());
			PDFmerger.addSource(pdf2.getFile());

			PDFmerger.mergeDocuments(null);
		} catch (Exception e) {
			System.out.println("ERROR: Files could not be merged");
		}
	}

	public PDDocument getPdfDoc() {
		return this.pdfDoc1;
	}

	public File getFile() {
		return this.pdfFile;
	}
}