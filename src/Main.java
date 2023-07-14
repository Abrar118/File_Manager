import com.aspose.pdf.Document;
import com.aspose.pdf.SaveFormat;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.fit.pdfdom.PDFDomTree;

import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main
{
    static Scanner cin;

    static String format(String s)
    {
        String ss=s;
        StringBuffer sb=new StringBuffer(ss);
        int k=0;

        for(int i=0;i<s.length();++i)
        {
            if(s.charAt(i)=='\\') sb.insert(i+(k++),'\\');
        }
        ss=sb.toString();
        return ss;
    }

    static void pdf_to_html() throws IOException, ParserConfigurationException
    {
        cin=new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String s=cin.nextLine();
        s=format(s);

        PDDocument pdf=PDDocument.load(new File(s));
        Writer html = new PrintWriter("Convert.html", StandardCharsets.UTF_8);
        new PDFDomTree().writeText(pdf,html);
        html.close();
    }

    static void pdf_to_image() throws IOException
    {
        cin=new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String s=cin.nextLine();
        s=format(s);

        PDDocument document = PDDocument.load(new File(s));
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        for (int page = 0; page < document.getNumberOfPages(); ++page)
        {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);
            ImageIOUtil.writeImage(bim, String.format("Pdf-%d.%s", page + 1, ".png"), 300);
        }
        document.close();

    }

    static void pdf_to_text() throws IOException
    {
        cin=new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String s=cin.nextLine();
        s=format(s);

        File load_pdf=new File(s);
        PDFParser parser=new PDFParser(new RandomAccessFile(load_pdf, "r"));
        parser.parse();

        COSDocument cosdoc=parser.getDocument();
        PDFTextStripper stripper=new PDFTextStripper();
        PDDocument pddoc=new PDDocument(cosdoc);
        String parsedtext=stripper.getText(pddoc);

        PrintWriter writer=new PrintWriter("PDF.txt");
        writer.print(parsedtext);

        writer.close();
        cosdoc.close();
        pddoc.close();
    }

    static void pdf_to_docx()
    {
        cin=new Scanner(System.in);
        System.out.println("Enter the file path: ");
        String s=cin.nextLine();
        s=format(s);

        Document doc = new Document(s);
        doc.save("PDF.docx", SaveFormat.DocX);
    }

    static void download_from_url() throws IOException
    {
        cin=new Scanner(System.in);
        System.out.println("Enter Download Link (Your link should end with .pdf): ");
        String link=cin.nextLine();

        URL url = new URL(link);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        double filesize = (double)http.getContentLengthLong();

        BufferedInputStream input = new BufferedInputStream(http.getInputStream());

        FileOutputStream ouput = new FileOutputStream("C:\\Users\\Abrar Mahir Esam\\Downloads\\Documents\\url.pdf");
        BufferedOutputStream bufferOut = new BufferedOutputStream(ouput, 1024);

        byte[] buffer = new byte[1024];
        double TotalDownload = 0.00;
        int readbyte = 0;
        double percentOfDownload = 0.00;

        while((readbyte=input.read(buffer,0,1024))>=0)
        {
            bufferOut.write(buffer,0,readbyte);
            TotalDownload += readbyte;
            percentOfDownload = (TotalDownload*100)/filesize;
            String percent = String.format("%.2f", percentOfDownload);
            System.out.println("Downloaded "+ percent + "%");
        }

        System.out.println("Download is complete.");
        bufferOut.close();
        input.close();
    }

    public static void main(String[] args) throws IOException
    {
        //pdf_to_html();
        //pdf_to_image();
        pdf_to_text();
        //pdf_to_docx();
        //download_from_url();
    }
}