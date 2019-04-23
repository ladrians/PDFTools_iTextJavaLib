package pdftools;

import java.io.FileOutputStream;

import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class TesterClass {

	public static void main(String[] args) {
		//concatSample();
		//sign();
		//setFields2();
		//setTiff();
		//getFields();
		//addText();
		html2pdf();
	}
	
	public static void concatSample()
	{
		String targetPath = "C:\\temp\\DocOut.pdf";
		String res = "";
		
		java.util.ArrayList<java.lang.String> AV8files = new java.util.ArrayList<java.lang.String>();
	    AV8files.add("C:\\temp\\uno.pdf");
		AV8files.add("C:\\temp\\dos.pdf");

	    //AV8files.add("C:\\tmp\\client_test_1.pdf");
	    //AV8files.add("C:\\tmp\\client_test_2.pdf");
	    //AV8files.add("C:\\tmp\\client_attached_test_1.pdf");
	    //AV8files.add("C:\\tmp\\client_attached_test_2.pdf");
	    
		iTextSharpUtil ut = new iTextSharpUtil();
		res = ut.ConcatFiles(AV8files, targetPath);
		
		System.out.println( "Result:" + res);
		//System.in.read();
	}
	
	public static void modifPermissions()
	{
	}

	public static void sign()
	{
		//iTextSharpUtil ut = new iTextSharpUtil();
	    //String res = ut.AddSignature("C:\\temp\\DocOut.pdf", "C:\\temp\\DocSigned.pdf", "c:\\mycert.pfx", "technical!1", true);
	    
		String sourcePath = "C:\\temp\\a.pdf";
		String targetPath = "C:\\temp\\b.pdf";
		String cert = "C:\\temp\\EOs\\pdftools\\dobleFirmaDigital\\cert.pfx";
		String pwd = "denarius1";
		int lx = 1; // 100
		int ly = 1; // 100
		int ux = 50; // 250
		int uy = 300; // 150
		int page = 1;
		Boolean isVisible = true;
		String res = "";

		iTextSharpUtil ut = new iTextSharpUtil();
		res = ut.AddSignature(sourcePath, targetPath, cert, pwd, lx, ly, ux, uy, page, isVisible);

		System.out.println( "Result:" + res);	    
	}

	public static void setFields()
	{
		iTextSharpUtil ut = new iTextSharpUtil();

		com.genexus.util.GXProperties AV10properties = new com.genexus.util.GXProperties();
		
        AV10properties.set("topmostSubform[0].Page1[0].top2[0]", "uno");
        AV10properties.set("topmostSubform[0].Page1[0].siccode[0]", "dos");
        AV10properties.set("topmostSubform[0].Page1[0].merchant_number[0]", "tres");
        AV10properties.set("Text1", "cuatro");
        AV10properties.set("topmostSubform[0].Page2[0].topmostSubform_0_\\.Page2_0_\\.topmostSubform_0__\\.Page9_0__\\.saannual_0__0_[0]", "Yes");
        AV10properties.set("topmostSubform[0].Page3[0].cdsc_third_party_process_yes[0]", "Yes");
        AV10properties.set("topmostSubform[0].Page2[0].topmostSubform_0_\\.Page2_0_\\.topmostSubform_0__\\.Page9_0__\\.customerorderinet_0__0_[0]", "Yes");

		String res = ut.SetFields("C:\\temp\\ApplicationFristData.pdf", "C:\\temp\\ApplicationFristData3_java.pdf", AV10properties);
	    System.out.println( "Result:" + res);
	}
	
	public static void setFields2()
	{
	    try
	    {
            PdfReader reader = new PdfReader("C:\\temp\\TestPDFAngeloNardonePDF.pdf");
	    	PdfStamper stamp = new PdfStamper(reader,new FileOutputStream("C:\\temp\\TestPDFAngeloNardonePDF_2.pdf"));
	    	int i = reader.getNumberOfPages();
            PdfContentByte content = stamp.getUnderContent(i);

            Image image = Image.getInstance("C:\\temp\\ladrians.jpeg");

            image.setAbsolutePosition(30,30);
            
            //content.addImage(image);
            content.addImage(image, image.getWidth(), 10, 10, image.getHeight(), 10, 10);
            
            stamp.setFormFlattening(true);

            stamp.close();
            reader.close();
            
            //output.toByteArray();
	    }
	    catch (Exception exception)
	    {
	    	System.out.println(exception.getMessage());
	    }				
	}

	public static void setTiff()
	{
		String targetPath = "C:\\temp\\DocOut.pdf";
		String res = "";
		
		java.util.ArrayList<java.lang.String> AV8files = new java.util.ArrayList<java.lang.String>();
	    AV8files.add("C:\\temp\\tiff01.TIF");
		AV8files.add("C:\\temp\\tiff01.TIF");
		
		iTextSharpUtil t2d = new iTextSharpUtil();
		res =  t2d.TiffAsPDF(AV8files, targetPath);
		
		System.out.println( "Result:" + res);
	}
	
	public static void getFields()
	{
	    try
	    {
			String targetPath = "C:\\temp\\ApplicationFristData.pdf";
			String res = "";

			com.genexus.util.GXProperties AV10properties = new com.genexus.util.GXProperties();

			iTextSharpUtil t2d = new iTextSharpUtil();
			AV10properties =  t2d.GetFields(targetPath);

			System.out.println( "Result:" + res + " count:" + AV10properties.count());
	    }
	    catch (Exception exception)
	    {
	    	System.out.println(exception.getMessage());
	    }				
	}
	
	public static void addText()
	{
		String sourcePath = "C:\\temp\\a.pdf";
		String targetPath = "C:\\temp\\a_text.pdf";
		String res = "";
		int x = 250;
		int y = 700;
		int selectedPage = 1;
		String text = "Hello World";
		int fontSize = 20;
		java.math.BigDecimal opacity = new java.math.BigDecimal(0.5);
			    
		iTextSharpUtil ut = new iTextSharpUtil();
		res = ut.addText(sourcePath, targetPath, x, y, selectedPage, text, 45, 255, 0, 0, fontSize, opacity);
		//res = ut.addText(sourcePath, targetPath, x, y, selectedPage, text, fontSize);
		
		System.out.println( "Result:" + res);
		//System.in.read();
	}	
		
    public static void html2pdf()
    {
        String sourceString = "" + 
        		"<html><body>" +
        		"<p>使用iText做PDF文件輸出</p>" +    
        		"<p>使用iText做PDF文件輸出</p>" +    
        		"<p>使用iText做PDF文件輸出</p>" +  
        		"<p>使用iText做PDF文件輸出</p>" + 
        		"<p>使用iText做PDF文件輸出</p>" +
        		"</body></html>";
        
        String targetPath = "C:\\temp\\html01.pdf";
        String res = "";

        iTextSharpUtil ut = new iTextSharpUtil();
        res = ut.Html2PDF(sourceString, targetPath, "");
        System.out.println("Result:" + res);
	}
}