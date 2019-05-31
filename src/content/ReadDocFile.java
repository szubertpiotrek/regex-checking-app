package content;

import java.io.*;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;

public class ReadDocFile
{
    public static void main(String[] args)
    {
        File file = null;
        WordExtractor extractor = null;
        try
        {

            file = new File("Wyrazenia_01.docx");
            FileInputStream fis = new FileInputStream("/media/piotrsz/--------/Regular_Expressions/Wyrazenia_01.docx");
            HWPFDocument document = new HWPFDocument(fis);
            extractor = new WordExtractor(document);
            String[] fileData = extractor.getParagraphText();
            for (int i = 0; i < fileData.length; i++)
            {
                if (fileData[i] != null)
                    System.out.println(fileData[i]);
            }
        }
        catch (Exception exep)
        {
            exep.printStackTrace();
        }
    }
}