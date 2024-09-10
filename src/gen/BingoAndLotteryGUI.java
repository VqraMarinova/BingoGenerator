package gen;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.itextpdf.text.DocumentException;

import gen.generators.BingoCardGenerator;
import gen.generators.LotteryTicketGenerator;

public class BingoAndLotteryGUI extends JFrame {

	private static final long serialVersionUID = -4905602149201799875L;
	private JTextField numCardsField;
	private JButton generateBingoButton;
	private JButton generateLotteryButton;

	private BingoCardGenerator bingoGenerator = new BingoCardGenerator();
	private LotteryTicketGenerator lotteryGenerator = new LotteryTicketGenerator();
	private Font font;

	public BingoAndLotteryGUI() {
		font = new Font("Arial", Font.PLAIN, 14);
		setTitle("Bingo Generator");
		setSize(400, 250);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		JLabel label = new JLabel("Брой карти/билети:");
		label.setFont(font);
		label.setHorizontalAlignment(SwingConstants.CENTER);

		numCardsField = new JTextField(10);
		numCardsField.setFont(font);
		numCardsField.setHorizontalAlignment(SwingConstants.CENTER);

		generateBingoButton = new JButton("Генерирай бинго карти");
		generateBingoButton.setFont(font);

		generateLotteryButton = new JButton("Генерирай лотарийни билети");
		generateLotteryButton.setFont(font);

		generateBingoButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateBingoCards();
			}

		});

		generateLotteryButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				generateLotteryTickets();
			}

		});

		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
		panel.setBorder(new EmptyBorder(20, 20, 20, 20));

		panel.add(label);
		panel.add(numCardsField);
		panel.add(generateBingoButton);
		panel.add(generateLotteryButton);

		getContentPane().add(panel, BorderLayout.CENTER);

		setVisible(true);
	}

	private void generateBingoCards() {
		int num = getNumInput();
		try {
			bingoGenerator.generate(num);
			numCardsField.setText("");
		} catch (NumberFormatException ex) {
			this.showErrorMsg("Моля, въведете валиден брой карти.");
		} catch (DocumentException | IOException de) {
			de.printStackTrace();
			this.showErrorMsg("Възникна грешка при генерирането на картите.");
		}
	}

	private void generateLotteryTickets() {

		String message = "<html>Моля да въведете възможните печалби, разделени със запетая:<br>"
				+ "(Това поле не е задължително. Ако не се въведат суми, ще липсва печеливша сума в билета)</html>";

		JPanel panel = new JPanel();
		panel.add(new JLabel(message));

		JTextField inputField = new JTextField(20);
		inputField.setPreferredSize(new Dimension(600, 30));
		panel.add(inputField);

		JButton continueButton = new JButton("Продължи");

		JDialog dialog = new JDialog((Frame) null, "Възможни печалби", true);
		dialog.getContentPane().add(panel, BorderLayout.CENTER);
		dialog.getContentPane().add(continueButton, BorderLayout.SOUTH);
		dialog.setPreferredSize(new Dimension(700, 150));
		dialog.pack();

		final boolean[] isDialogClosed = { true };

		continueButton.addActionListener(e -> {
			isDialogClosed[0] = false;
			dialog.dispose();
		});

		dialog.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				isDialogClosed[0] = true; // Dialog was closed using "X"
			}
		});

		dialog.setLocationRelativeTo(null);
		dialog.setVisible(true);

		// If dialog was closed by "X", exit the method
		if (isDialogClosed[0]) {
			numCardsField.setText("");
			return;
		}

		String input = inputField.getText();

		double[] winnings;

		try {
			if (input == null || input.trim().isEmpty()) {
				winnings = new double[0];
			} else {
				String[] winningsStr = input.split(",");
				winnings = new double[winningsStr.length];
				for (int i = 0; i < winningsStr.length; i++) {
					winnings[i] = Double.parseDouble(winningsStr[i].trim());
					if (winnings[i] <= 0) {
						throw new NumberFormatException();
					}
				}
			}
		} catch (NumberFormatException ex) {
			this.showErrorMsg("Моля, въведете валидни положителни числа, разделени със запетая.");
			return;
		}

		int num = getNumInput();
		try {
			lotteryGenerator.setPossibleWinSums(winnings);
			lotteryGenerator.generate(num);
			numCardsField.setText("");
		} catch (NumberFormatException ex) {
			this.showErrorMsg("Моля, въведете валиден брой билети.");
		} catch (DocumentException | IOException de) {
			de.printStackTrace();
			this.showErrorMsg("Възникна грешка при генерирането на билетите.");
		}
	}

	private int getNumInput() {
		int num = 0;

		try {
			num = Integer.parseInt(numCardsField.getText().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (num <= 0) {
			this.showErrorMsg("Моля, въведете положително цяло число.");
			throw new NumberFormatException();
		}
		if (num > 1000) {
			this.showErrorMsg("Моля, въведете число по- малко или равно на 1000.");
			throw new NumberFormatException();
		}
		return num;
	}

	private void showErrorMsg(String msg) {
		if (SwingUtilities.isEventDispatchThread()) {
			JOptionPane optionPane = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE);
			JDialog dialog = optionPane.createDialog("Грешка");
			dialog.setVisible(true);
			dialog.requestFocus();
		} else {
			SwingUtilities.invokeLater(() -> {
				JOptionPane optionPane = new JOptionPane(msg, JOptionPane.ERROR_MESSAGE);
				JDialog dialog = optionPane.createDialog("Грешка");
				dialog.setVisible(true);
				dialog.requestFocus();
			});
		}
	}

}