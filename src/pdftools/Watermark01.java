package pdftools;

public class Watermark01 {

	public static void main(String[] args) {
		String PathSource = "C:\\temp\\EOs\\pdftools\\issueconWaterMark\\Presupuesto.pdf";// "C:\\temp\\DocIn.pdf";
		String image = "C:\\temp\\EOs\\pdftools\\issueconWaterMark\\presupuesto_sindatos_100.jpg";//"c:\\sw\\br\\ladrians.jpeg";
		String PathTarget = "c:\\tmp\\Presupuesto_al_25.pdf";//"C:\\temp\\DocOut.pdf";
		int offsetX = 1 /*200*/, offsetY = 1 /*400*/;
		DoWatermark(PathSource,PathTarget,image,offsetX,offsetY);
	}
	
	public static void DoWatermark(String PathSource,String PathTarget,String image,int offsetX,int offsetY)
	{
		// http://www.java2s.com/Code/Java/PDF-RTF/AddWatermarkImagetoanExistingPDFFile.htm
		try {
			com.lowagie.text.pdf.PdfReader reader = new com.lowagie.text.pdf.PdfReader(PathSource);
			int n = reader.getNumberOfPages();
			com.lowagie.text.pdf.PdfStamper stamp = new com.lowagie.text.pdf.PdfStamper(reader, new java.io.FileOutputStream(PathTarget));
			int i = 0;
			com.lowagie.text.pdf.PdfContentByte under;
			com.lowagie.text.Image img = com.lowagie.text.Image.getInstance(image);
			img.setAbsolutePosition(offsetX, offsetY);
			img.scaleToFit(826, 1100);
			while (i < n)
			{
				i++;
				under = stamp.getUnderContent(i);
				under.addImage(img);
			}
			stamp.close();
			
		} catch (Exception e) {
			String ErrorMessage = e.toString();
			System.out.println(ErrorMessage);
		}
	}
}
