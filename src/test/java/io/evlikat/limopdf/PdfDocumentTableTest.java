package io.evlikat.limopdf;

import io.evlikat.limopdf.paragraph.PdfParagraph;
import io.evlikat.limopdf.paragraph.PdfParagraphProperties;
import io.evlikat.limopdf.table.PdfCell;
import io.evlikat.limopdf.table.PdfRow;
import io.evlikat.limopdf.table.PdfTable;
import io.evlikat.limopdf.util.Box;
import org.junit.Test;

public class PdfDocumentTableTest extends AbstractPdfDocumentTest {

    @Test(timeout = 3000L)
    public void shouldAddTable() {
        PdfTable table = new PdfTable(300f, 180f);

        PdfRow row1 = new PdfRow();
        PdfCell cell11 = new PdfCell();
        cell11.addDrawable(new PdfParagraph("1.1"));
        PdfCell cell12 = new PdfCell();
        cell12.addDrawable(new PdfParagraph("1.2"));
        row1.addCell(cell11);
        row1.addCell(cell12);

        PdfRow row2 = new PdfRow();
        PdfCell cell21 = new PdfCell();
        cell21.addDrawable(new PdfParagraph("2.1"));
        PdfCell cell22 = new PdfCell();
        cell22.addDrawable(new PdfParagraph("2.2"));
        row2.addCell(cell21);
        row2.addCell(cell22);

        table.addRow(row1);
        table.addRow(row2);

        doc.add(new PdfParagraph("Table I", PdfParagraphProperties.builder().margin(Box.of(15f)).build()));
        doc.add(table);
    }
}