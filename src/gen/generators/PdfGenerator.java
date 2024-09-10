package gen.generators;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import javax.swing.JOptionPane;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;

public abstract class PdfGenerator {

	protected abstract void generatePdf(String fileName, int numItems) throws DocumentException, IOException;

	public abstract void generate(int num) throws DocumentException, IOException;

	protected void openPdf(String fileName) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.open(new File(fileName));
			} catch (IOException ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(null, "Възникна грешка при отварянето на PDF файла.");
			}
		} else {
			JOptionPane.showMessageDialog(null, "Отварянето на PDF файлове не се поддържа на тази система.");
		}
	}

	protected PdfPTable createBingoTable(int[][] bingoNumbers) {
	    PdfPTable table = new PdfPTable(5);
	    table.setWidthPercentage(50);
	    for (int row = 0; row < 5; row++) {
	        for (int col = 0; col < 5; col++) {
	            String cellValue = (bingoNumbers[row][col] == 0) ? "X" : String.valueOf(bingoNumbers[row][col]);
	            PdfPCell cell = new PdfPCell(new Paragraph(cellValue));
	            cell.setMinimumHeight(30);
	            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
	            table.addCell(cell);
	        }
	    }
	    return table;
	}


}