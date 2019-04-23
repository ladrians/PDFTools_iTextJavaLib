package pdftools;

import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.Image;

public class CreateImage {
	public static void main(String[] args) {
		String PathSource = "C:\\temp\\DocIn.pdf";
		String image = "C:\\temp\\pdftools_image_10.jpg";
		CreateImageUsing(PathSource,image);
	}
	
	@SuppressWarnings("unused")
	public static void CreateImageUsing(String PathSource,String image)
	{
		// http://stackoverflow.com/questions/14868910/how-to-create-image-from-pdf-using-pdfbox-in-java
		try {
			com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(PathSource);
			int n = reader.getNumberOfPages();
			
			PdfDictionary d = reader.getPageN(0);
			Image img = Image.getInstance(image);

			img.scaleToFit(826, 1100);
			
		} catch (Exception e) {
			String ErrorMessage = e.toString();
			System.out.println(ErrorMessage);
		}
		
	}
}
