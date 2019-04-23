package pdftools;

import java.io.*;
import java.security.PrivateKey;
import java.util.*;
import java.util.List;
import java.security.KeyStore;
import java.security.cert.*;

import com.genexus.util.GXProperty;
import com.lowagie.text.*;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.*;
import com.lowagie.text.pdf.codec.TiffImage;

import java.security.Security;
import java.security.cert.X509Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class iTextSharpUtil {

	
	public String AddSignature(String PathSource, String PathTarget, String CertPath, String CertPass, Boolean Visible)
	{
		return AddSignature(PathSource, PathTarget, CertPath, CertPass,100,100,250,150,1,true);		
	}
	
	public String AddSignature(String PathSource, String PathTarget, String CertPath, String CertPass, int lx, int ly, int ux, int uy, int page, Boolean Visible)
	{
		lx = lx < 1? 100:lx;
		ly = ly < 1? 100:ly;
		ux = ux < 1? 250:ux;
		uy = uy < 1? 150:uy;
		
	    try
	    {
	        //AsymmetricKeyParameter privKey = null;
	        X509Certificate[] certChain = null;

	    	String alias = null;
	    	String str2 = null;
	    	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	    	KeyStore store = KeyStore.getInstance("PKCS12", new BouncyCastleProvider());
	    	store.load(new FileInputStream(CertPath), CertPass.toCharArray());
	    	Enumeration<String> aliases  = store.aliases();
	    	while (aliases.hasMoreElements())
	        {
	    		str2 = aliases.nextElement();
	            alias = str2;
	            if (store.isKeyEntry(alias))
	            {
	                break;
	            }
	        }
	    	PrivateKey privKey = (PrivateKey)store.getKey(alias, CertPass.toCharArray());
	    	Certificate[] certificateChain = store.getCertificateChain(alias);
	    	
	    	int l = certificateChain.length;
	    	certChain = new X509Certificate[l];
	        for (int i = 0; i < l; i++)
	        {
	            certChain[i] = (X509Certificate)certificateChain[i];
	        }
	        PdfReader reader = new PdfReader(PathSource);
	        PdfStamper stamper = PdfStamper.createSignature(reader, new FileOutputStream(PathTarget), '\0', null, true);
	        PdfSignatureAppearance signatureAppearance = stamper.getSignatureAppearance();
	        signatureAppearance.setCrypto(privKey, certificateChain, null, PdfSignatureAppearance.WINCER_SIGNED);
	        if (Visible)
	        {
	        	page = (page < 1 || page > reader.getNumberOfPages()) ? 1 : page;
	            signatureAppearance.setVisibleSignature(new Rectangle(lx, ly, ux, uy), page, null);
	        }
	        stamper.close();
	        return "";
	    }
	    catch (Exception exception)
	    {
	        return exception.getMessage();
	    }
	}

	public String ConcatFiles(ArrayList<String> files, String targetPath)
	{
		try
		{
			if (files.size() > 0)
			{
				String filename = files.get(0);
				PdfReader reader = new PdfReader(filename);
				int numberOfPages = reader.getNumberOfPages();
				Document document = new Document(reader.getPageSizeWithRotation(1));
				PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(targetPath)); //FileMode.Create
				document.open();
				PdfContentByte directContent = instance.getDirectContent();
				for(int i = 0; i < files.size(); i++)
				{
					int index = 0;
					String str2 = files.get(i);
					reader = new PdfReader(str2);
					numberOfPages = reader.getNumberOfPages();
					while (index < numberOfPages)
					{
						index++;
						document.setPageSize(reader.getPageSizeWithRotation(index));
						document.newPage();
						PdfImportedPage importedPage = instance.getImportedPage(reader, index);
						int pageRotation = reader.getPageRotation(index);
						
						// Rotation taken from http://itext-general.2136553.n4.nabble.com/Document-rotates-while-merging-td2224779.html
						
						if ((pageRotation == 90))
						{
							directContent.addTemplate(importedPage, 0f, -1f, 1f, 0f, 0f, reader.getPageSizeWithRotation(index).getHeight());
						}
						else if ((pageRotation == 270))
						{
							directContent.addTemplate(importedPage, 0f, 1f, -1f, 0f, reader.getPageSizeWithRotation(index).getWidth(),0f);
						}
						else
						{
							directContent.addTemplate(importedPage, 1f, 0f, 0f, 1f, 0f,0f);
						}
                    }
                }
                document.close();
                return "";
            }
			return "No files to process, use AddFile method";
        }
        catch (Exception exception)
        {
        	return exception.getMessage();
    	}
        finally
        {
        	// Do nothing;
        }
	}

    public String addText(String pathSource, String pathTarget, int x, int y, int selectedPage, String text, int fontSize)
    {
        return addText(pathSource, pathTarget, x, y, selectedPage, text, 0, 0, 0, 0, fontSize, new java.math.BigDecimal(1.0));	
	}
	
    public String addText(String pathSource, String pathTarget, int x, int y, int selectedPage, String text, int angle, int red, int green, int blue, int fontSize, java.math.BigDecimal opacity)
    {
        try
        {
        	PdfReader reader = new PdfReader(pathSource);
        	int n = reader.getNumberOfPages();
        	
        	if (!(selectedPage > 0 && selectedPage <= n))
            {
                return String.format("Invalid Page %s, the PDF has %s pages", selectedPage, n);
            }
        	
        	Document document = new Document(reader.getPageSizeWithRotation(1));
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(pathTarget));           

            document.open();
            PdfContentByte cb = writer.getDirectContent();
            PdfImportedPage page;
            int rotation;
            int i = 0;
            while (i < n)
            {
            	i++;
                document.newPage();
                page = writer.getImportedPage(reader, i);
                rotation = reader.getPageRotation(i);
                if (rotation == 90 || rotation == 270)
                {
                    cb.addTemplate(page, 0, -1f, 1f, 0, 0, reader.getPageSizeWithRotation(i).getHeight());
                }
                else
                {
                    cb.addTemplate(page, 1f, 0, 0, 1f, 0, 0);
                }
                
                if (i == selectedPage) {
                	insertText(cb, fontSize, x, y, text, angle, red, green, blue, opacity);
                }
            }
            document.close();
            return "";
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
    
    private void insertText(PdfContentByte cb, int fontSize, int x, int y, String text, int angle, int red, int green, int blue, java.math.BigDecimal opacity) throws DocumentException, IOException
    {
        // Idea from https://forums.asp.net/t/1360567.aspx?iText+Sharp+and+text+position

        BaseFont bf = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
        cb.beginText();
        cb.setFontAndSize(bf, fontSize);
        cb.setRGBColorFill(red, green, blue);
        PdfGState gstate = new PdfGState();
        gstate.setFillOpacity(opacity.floatValue());
        cb.setGState(gstate);
        cb.showTextAligned(PdfContentByte.ALIGN_LEFT, text, x, y, angle);
        cb.endText();
    }    

	public String ModifyPermissions(String PathSource, String PathTarget, String UserPassword, ArrayList<Integer> Permissons)
	{
	    try
	    {
	        int permissions = 0;
	        int num2 = 0;
	        for(int i = 0; i < Permissons.size(); i++)
	        {
	        	num2 = Permissons.get(i);
	        	permissions |= num2;
	        }
	        PdfReader reader = new PdfReader(PathSource);
	        int numberOfPages = reader.getNumberOfPages();
	        Document document = new Document(reader.getPageSizeWithRotation(1));
			PdfWriter instance = PdfWriter.getInstance(document, new FileOutputStream(PathTarget)); //FileMode.Create
	        //instance.setEncryption(true, UserPassword, null, permissions); // http://api.itextpdf.com/deprecated-list.html
	        // http://www.jarvana.com/jarvana/view/com/lowagie/itext/2.1.7/itext-2.1.7-javadoc.jar!/com/lowagie/text/pdf/interfaces/PdfEncryptionSettings.html#setEncryption%28byte[],%20byte[],%20int,%20int%29
	        instance.setEncryption(UserPassword.getBytes(), null,permissions,PdfWriter.ENCRYPTION_AES_128);
	        
	        document.open();
	        PdfContentByte directContent = instance.getDirectContent();
	        int index = 0;
	        while (index < numberOfPages)
	        {
	            index++;
	            document.setPageSize(reader.getPageSizeWithRotation(index));
	            document.newPage();
	            PdfImportedPage importedPage = instance.getImportedPage(reader, index);
	            int pageRotation = reader.getPageRotation(index);
	            if ((pageRotation == 90) || (pageRotation == 270))
	            {
	                directContent.addTemplate(importedPage, 0f, -1f, 1f, 0f, 0f, reader.getPageSizeWithRotation(index).getHeight());
	            }
	            else
	            {
	                directContent.addTemplate(importedPage, 1f, 0f, 0f, 1f, 0f, 0f);
	            }
	        }
	        document.close();
	        return "";
	    }
	    catch (Exception exception)
	    {
	        return exception.getMessage();
	    }		
	}

    @SuppressWarnings("unchecked")
	public com.genexus.util.GXProperties GetFields(String PathSource)
    {
    	com.genexus.util.GXProperties allFields = null;
        try
        {
        	PdfReader pdfR = new PdfReader(PathSource);

        	allFields = new com.genexus.util.GXProperties();
            AcroFields form = pdfR.getAcroFields();
            Set<String> fields = form.getFields().keySet();
            for (String key : fields) {
            	
            	allFields.add(key,key);
            }            
        }
        catch (Exception e)
        {
        	// do nothing
        }
        return allFields;
    }
	
	public String SetFields(String PathSource, String PathTarget, Object myFields)
	{
	    try
	    {
	    	com.genexus.util.GXProperties Fields = (com.genexus.util.GXProperties)myFields;
            // create a new PDF reader based on the PDF template document
	    	PdfReader reader = new PdfReader(PathSource);

	    	PdfStamper pdfStamper = new PdfStamper(reader,new FileOutputStream(PathTarget));

            GXProperty item = Fields.first();
            while (item != null)
            {
                pdfStamper.getAcroFields().setField(item.getKey(),item.getValue());
                item = Fields.next();
            }

            // flatten the form to remove editting options, set it to false to leave the form open to subsequent manual edits
            pdfStamper.setFormFlattening(false);

            // close the pdf
            pdfStamper.close();
            reader.close();

	        return "";
	    }
	    catch (Exception exception)
	    {
	        return exception.getMessage();
	    }		
	}
	
	public int NumberOfPages(String PathSource)
	{
		int res = 0;
	    try
	    {
	        PdfReader reader = new PdfReader(PathSource);
	        res = reader.getNumberOfPages();
	    }
	    catch (Exception exception)
	    {
	        res = -1; // error
	    }
	    return res;
	}
	
	public String TiffAsPDF(ArrayList<String> files, String targetPath)
	{
		// ideas from 
		// http://kjvarga.blogspot.com/2008/12/using-itext-to-convert-tiff-to-pdf-and.html
		// http://stackoverflow.com/questions/7721447/creating-pdf-from-tiff-image-using-itext
		
		try
		{
			if (files.size() > 0)
			{					
		        ByteArrayOutputStream outfile = new ByteArrayOutputStream();  
		        
		        Document document = null;
		        PdfWriter writer = null;
		        
				for(int iFile = 0; iFile < files.size(); iFile++) // iterate over files
				{
					String str2 = files.get(iFile);
					
					Image image = null;
					RandomAccessFileOrArray ra = new RandomAccessFileOrArray(str2);
					int pages = TiffImage.getNumberOfPages(ra);

			        for (int iPage = 1; iPage <= pages; iPage++) // iterate over tiff pages
			        {
			        	
			        	image = TiffImage.getTiffImage(ra, iPage);

				        Rectangle pageSize = new Rectangle(image.getPlainWidth(),image.getPlainHeight());
				        if (document == null)
				        {
				        	document = new Document(pageSize); // initialize with a PageSize
					        document.addCreationDate();
					        
					        writer = PdfWriter.getInstance(document, new FileOutputStream(targetPath));
					        writer.setStrictImageSequence(true);
				        	document.open();
				        }
				        else
				        {
				        	document.setPageSize(pageSize);
				        }
				        document.add(image);					
				        document.newPage();			        	
			        }
                }
                document.close();
		        outfile.flush();
                return "";
            }
			return "No Tiff files to process";
        }
        catch (Exception exception)
        {
        	return exception.getMessage();
    	}
        finally
        {
        	// Do nothing;
        }		
	}
	
	public String SetSize(String sourceFile, String pSize, String targetPath)
	{
		String result = "";
		try
		{
			PdfReader reader = new PdfReader(sourceFile);
			int i = reader.getNumberOfPages();
			Rectangle pageSize = PageSize.getRectangle(pSize);
			Document document = new Document(pageSize);
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(targetPath)); //FileMode.Create
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			int j = 0;
			reader = new PdfReader(sourceFile);
			i = reader.getNumberOfPages();
			while (j < i)
			{
				j++;
				document.setPageSize(pageSize);
				document.newPage();
				PdfImportedPage page = writer.getImportedPage(reader, j);
				int rotation = reader.getPageRotation(j);
				if (rotation == 90)
				{
					cb.addTemplate(page, 0f, -1f, 1f, 0f, 0f, reader.getPageSizeWithRotation(j).getHeight());
				}
				else
				{
					if (rotation == 270)
					{
						cb.addTemplate(page, 0f, 1f, -1f, 0f, reader.getPageSizeWithRotation(j).getWidth(), 0f);
					}
					else
					{
						cb.addTemplate(page, 1f, 0f, 0f, 1f, 0f, 0f);
					}
				}
			}
			document.close();
			result = "";
		}
		catch (Exception e)
		{
			result = e.getMessage();
		}
		return result;
	}
	
    @SuppressWarnings("unchecked")
	public String Html2PDF(String html, String pathTarget, String fontPath)
    {
        try
        {
        	// Using sample from https://stackoverflow.com/questions/17825782/how-to-convert-html-to-pdf-using-itext
        	
        	if (fontPath.isEmpty())
        		fontPath = "c:/windows/fonts/ARIALUNI.TTF";
        	FontFactory.register(fontPath); 
        	StyleSheet style = new StyleSheet();
        	style.loadTagStyle("body", "face", "Arial Unicode MS");
        	style.loadTagStyle("body", "encoding", BaseFont.IDENTITY_H);
        	Document document = new Document();
            OutputStream file = new FileOutputStream(new File(pathTarget));        	
        	PdfWriter.getInstance(document, file);
        	document.open();
        	List<Element> elements = (List<Element>) HTMLWorker.parseToList(new StringReader(html), style);
    		for (Element el : elements) {
    			document.add(el);
    		}
            document.close();
            file.close();
            return "";            		
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
    }
}

