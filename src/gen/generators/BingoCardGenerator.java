package gen.generators;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import gen.objects.BingoCard;

public class BingoCardGenerator extends PdfGenerator {

	@Override
	public void generate(int num) throws DocumentException, IOException {
		generatePdf("bingo_cards.pdf", num);
		openPdf("bingo_cards.pdf");
	}

	@Override
	public void generatePdf(String fileName, int numItems) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream(fileName));

		document.open();
		Paragraph title = new Paragraph("Bingo Card");
		title.setAlignment(Element.ALIGN_CENTER);
		document.add(title);
		

		for (int i = 0; i < numItems; i++) {
			BingoCard card = new BingoCard();
			PdfPTable table = createBingoTable(card.getNumbers());

			table.setWidthPercentage(80);
			table.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.getDefaultCell().setBorderColor(BaseColor.BLACK);
			table.getDefaultCell().setPadding(10f);
			table.setSpacingBefore(20f);
			table.setSpacingAfter(20f);

			document.add(table);
			document.newPage();
		}
		document.close();
	}

}