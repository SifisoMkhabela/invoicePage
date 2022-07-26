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
        response.addHeader("Content-Disposition", "attachment;filename=Invoices.pdf");

        //read data from controller
        List<Invoice> list = (List<Invoice>) model.get("list");

        //create element
        Font titleFont = new Font(Font.TIMES_ROMAN, 24, Font.BOLD, Color.blue);
        Paragraph customer = new Paragraph("Customer Details", titleFont);
        Paragraph title = new Paragraph("INVOICE ITEMS", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingBefore(20.0f);
        title.setSpacingAfter(25.0f);
        //add to document title
        document.add(title);

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



        //
    }
}