package gen.generators;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.BaseColor;

import gen.objects.LotteryTicket;

public class LotteryTicketGenerator extends PdfGenerator {

	private double[] possibleWinSums;

	public void setPossibleWinSums(double[] possibleWinSums) {
		this.possibleWinSums = possibleWinSums;
	}

	@Override
	public void generate(int num) throws DocumentException, IOException {
		this.generatePdf("lottery_tickets.pdf", num);
		this.openPdf("lottery_tickets.pdf");
	}

	@Override
	public void generatePdf(String fileName, int numItems) throws DocumentException, IOException {
		Document document = new Document(PageSize.A4);
		PdfWriter.getInstance(document, new FileOutputStream(fileName));

		document.open();

		Paragraph title = new Paragraph("Lottery Ticket");
		title.setAlignment(Element.ALIGN_CENTER);
		title.setSpacingAfter(20f);
		document.add(title);

		for (int i = 0; i < numItems; i++) {
			LotteryTicket ticket = new LotteryTicket(this.possibleWinSums);
			PdfPTable bingoTable = createBingoTable(ticket.getBingoCard().getNumbers());
			PdfPTable playerNumbersTable = createPlayerNumbersTable(ticket.getPlayerNumbers());

			PdfPCell bingoHeader = new PdfPCell(new Paragraph("Bingo Numbers"));
			bingoHeader.setColspan(5);
			bingoTable.setHeaderRows(1);
			bingoTable.addCell(bingoHeader);

			PdfPCell playerNumbersHeader = new PdfPCell(new Paragraph("Player Numbers"));
			playerNumbersHeader.setColspan(5);
			playerNumbersTable.setHeaderRows(1);
			playerNumbersTable.addCell(playerNumbersHeader);

			PdfPTable mainTable = new PdfPTable(2);
			mainTable.setWidthPercentage(100);
			mainTable.addCell(bingoTable);
			mainTable.addCell(playerNumbersTable);

			mainTable.setHorizontalAlignment(Element.ALIGN_CENTER);
			document.add(mainTable);

			double winningAmount = ticket.getWinningAmount();
			if (winningAmount > 0) {
				Paragraph winningParagraph = new Paragraph("Your Win Amount : " + String.format("%.2f", winningAmount) + " BGN ");
				winningParagraph.setAlignment(Element.ALIGN_CENTER);
				winningParagraph.setFont(new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.RED));
				document.add(winningParagraph);
			}

			document.newPage();
		}

		document.close();
	}

	private PdfPTable createPlayerNumbersTable(int[][] playerNumbers) {
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(50);
		for (int row = 0; row < 5; row++) {
			for (int col = 0; col < 5; col++) {
				String cellValue = (playerNumbers[row][col] == 0) ? "X" : String.valueOf(playerNumbers[row][col]);
				PdfPCell cell = new PdfPCell(new Paragraph(cellValue));
				cell.setMinimumHeight(30);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}
		}
		return table;
	}
}