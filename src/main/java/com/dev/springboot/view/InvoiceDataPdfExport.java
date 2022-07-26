package com.dev.springboot.view;

import java.awt.Color;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.view.document.AbstractPdfView;
import com.dev.springboot.model.Invoice;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.text.DecimalFormat;

public class InvoiceDataPdfExport extends AbstractPdfView {

    @Override
    protected void buildPdfMetadata(Map<String, Object> model, Document document, HttpServletRequest request)
    {
        Font headerFont = new Font(Font.TIMES_ROMAN, 20, Font.BOLD, Color.magenta);
        HeaderFooter header = new HeaderFooter(new Phrase("All Invoices PDF View", headerFont), false);
        header.setAlignment(Element.ALIGN_CENTER);
        document.setHeader(header);

        Font dateFont = new Font(Font.HELVETICA, 12, Font.NORMAL, Color.BLUE);

        HeaderFooter footer = new HeaderFooter(new Phrase("PDF Export Executed On : "+new Date(), dateFont), true);
        footer.setAlignment(Element.ALIGN_CENTER);
        document.setFooter(footer);
    }

    @Override
    protected void buildPdfDocument(
            Map<String, Object> model,
            Document document,
            PdfWriter writer,
            HttpServletRequest request,
            HttpServletResponse response)
            throws Exception
    {

        //total
        double totalAmount = 00;
        //download PDF with a given filename
        response.addHeader("Content-Disposition", "attachment;filename=Invoice.pdf");

        //adding image to pdf
        String imagePath = "images\\logo.png";
        //creating image data object
        ImageData data = ImageDataFactory.create(imagePath);
        //instatiating image class
        Image img = new Image(data);
        img.scaleToFit(200, 200); //imAGE RESCALLLING
        //adding image to document
        document.add(img);

        //read data from controller
        List<Invoice> list = (List<Invoice>) model.get("list");

        //Setting font
        Font cartFont = new Font(Font.TIMES_ROMAN, 24, Font.BOLD, Color.blue);

        //Company Profile
        Paragraph companyProfile = new Paragraph("Lorem Ipsum\nLorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat");
        document.add(companyProfile);

        //Adding Customer Details
        Paragraph customer = new Paragraph("Customer Details", cartFont);
        Paragraph customerDetails = new Paragraph("PepeBoii \nHouse 1220\nHappy Drive\nAncient City");
        document.add(customer);
        document.add(customerDetails);

        Paragraph cart = new Paragraph("INVOICE ITEMS", cartFont);
        cart.setAlignment(Element.ALIGN_CENTER);
        cart.setSpacingBefore(20.0f);
        cart.setSpacingAfter(25.0f);
        //add to document cart
        document.add(cart);

        //header for invoice Items
        Font tableHead = new Font(Font.TIMES_ROMAN, 12, Font.BOLD, Color.blue);
        PdfPTable Itemstable = new PdfPTable(4);// no.of columns
        Itemstable.addCell(new Phrase("Code",tableHead));
        Itemstable.addCell(new Phrase("Product",tableHead));
        Itemstable.addCell(new Phrase("Description",tableHead));
        Itemstable.addCell(new Phrase("Amount",tableHead));

        //invoice Items
        for(Invoice invoice : list ) {
            Itemstable.addCell(invoice.getId().toString());
            Itemstable.addCell(invoice.getName());
            Itemstable.addCell(invoice.getLocation());
            Itemstable.addCell(invoice.getAmount().toString());
            totalAmount = totalAmount + invoice.getAmount();

        }
        //add table data to document
        document.add(Itemstable);

        //total table
        PdfPTable totalTable = new PdfPTable((1));
        totalTable.addCell(new Phrase("Total Amount", tableHead));

        //rounding off rotal to 2 decimal places
        DecimalFormat f = new DecimalFormat("#.##");
        totalTable.addCell("R" + f.format(totalAmount));

        System.out.println("R" + f.format(totalAmount));



        //parse data to pdf document
        document.add(totalTable);

        //Banking Details
         Paragraph bankingDetails = new Paragraph(" Duis aute irure dolor\nin reprehenderit in\nvoluptate velit esse cillum dolore eu\nfugiat nulla pariatur.")
         document.add(bankingDetails);

        //Terms and Conditions
         Paragraph termsAndConditions = new Paragraph("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo. Nemo enim ipsam voluptatem quia voluptas sit aspernatur aut odit aut fugit, sed quia consequuntur magni dolores eos qui ratione voluptatem sequi nesciunt. Neque porro quisquam est, qui dolorem ipsum quia dolor sit amet, consectetur, adipisci velit, sed quia non numquam eius modi tempora incidunt ut labore et dolore magnam aliquam quaerat voluptatem. Ut enim ad minima veniam, quis nostrum exercitationem ullam corporis suscipit laboriosam, nisi ut aliquid ex ea commodi consequatur? Quis autem vel eum iure reprehenderit qui in ea voluptate velit esse quam nihil molestiae consequatur, vel illum qui dolorem eum fugiat quo voluptas nulla pariatur?");
         document.add(termsAndConditions);

        //
    }
}