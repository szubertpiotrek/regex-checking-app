package content;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;


import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;


public class DocReader {


	public static void readDocxFile(String fileName) {

		try {
			File file = new File(fileName);
			FileInputStream fis = new FileInputStream("/media/piotrsz/--------/Regular_Expressions/Wyrazenia_01.docx");

			XWPFDocument document = new XWPFDocument(fis);

			List<XWPFParagraph> paragraphs = document.getParagraphs();
								
			
			System.out.println("Total no of paragraph "+paragraphs.size());
			for (XWPFParagraph para : paragraphs) {
				System.out.println(para.getText());
			}
			fis.close();
			document.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

		
		System.out.println("Start programu");
		readDocxFile("Wyrazenia_01.docx");


	}
}