package algoritmiGenetski;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import java.awt.ScrollPane;
import javax.swing.ScrollPaneConstants;

import javafx.concurrent.Task;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;

public class UIGenetski extends JFrame {
	private JTextField txtPopulationSize;
	private JTextField txtMutationRate;
	private JTextField txtCrossoverRate;
	private JTextField txtTournamentSize;
	private JTextArea textArea;
	private JTextField txtGeneration;
	private static JProgressBar progressbar;
	private final int MAXGENRATIONS = 5000;
	public boolean samePopulationCheck = false;
	private int populationSize;

	City cities[];
	private JTextField txtBestDistance;

	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new UIGenetski();
				frame.setVisible(true);
			}
		});

	}

	public static boolean setProgress(int progress) {
		int procenat = progressbar.getValue();
		if (procenat < progress) {
			progressbar.setValue(progress);
			return true;
		} else
			return false;
	}

	public void updateProgress(int value) {
		try {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					progressbar.setValue(value);
				}
			});
			java.lang.Thread.sleep(100);
		} catch (InterruptedException e) {
			;
		}
	}

	// public void setText (String txt) {
	// textArea.setText(txt);
	// }
	public UIGenetski() {
		setResizable(false);
		setTitle("Trgovacki Putnik");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(450, 150, 700, 587);

		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);

		JLabel lblPopulationSize = new JLabel("Velcina Populacije:");
		lblPopulationSize.setBounds(28, 126, 91, 14);
		lblPopulationSize.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		txtPopulationSize = new JTextField();
		txtPopulationSize.setBounds(143, 123, 96, 20);
		txtPopulationSize.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtPopulationSize.setColumns(10);

		JLabel lblMutationRate = new JLabel("Stopa Mutacije:");
		lblMutationRate.setBounds(28, 157, 77, 14);
		lblMutationRate.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		txtMutationRate = new JTextField();
		txtMutationRate.setBounds(143, 154, 96, 20);
		txtMutationRate.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtMutationRate.setColumns(10);

		JLabel lblStopaUkrstanja = new JLabel("Stopa Ukrstanja:");
		lblStopaUkrstanja.setBounds(28, 192, 81, 14);
		lblStopaUkrstanja.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		txtCrossoverRate = new JTextField();
		txtCrossoverRate.setBounds(143, 189, 96, 20);
		txtCrossoverRate.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtCrossoverRate.setText("");
		txtCrossoverRate.setColumns(10);

		progressbar = new JProgressBar();
		progressbar.setBounds(10, 306, 452, 17);
		progressbar.setValue(0);
		progressbar.setStringPainted(true);

		JCheckBox samePopulation = new JCheckBox("Koristi istu populaciju");
		samePopulation.setBounds(295, 122, 135, 23);
		samePopulation.setEnabled(samePopulationCheck);
		samePopulation.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		samePopulation.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JCheckBox cb = (JCheckBox) e.getSource();
				if (cb.isSelected()) {
					// do something if check box is selected
					txtPopulationSize.setEditable(false);
				} else {
					// check box is unselected, do something else
					txtPopulationSize.setEditable(true);
				}
			}
		});

		JButton btnStart = new JButton("START");
		btnStart.setBounds(120, 272, 96, 23);
		btnStart.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (txtPopulationSize.getText().isEmpty() || txtMutationRate.getText().isEmpty()
						|| txtCrossoverRate.getText().isEmpty() || txtTournamentSize.getText().isEmpty()
						|| txtGeneration.getText().isEmpty()) {
					JOptionPane.showMessageDialog(UIGenetski.this, "Fill all fields");
				} else if (Integer.parseInt(txtGeneration.getText()) <= 0
						|| Integer.parseInt(txtGeneration.getText()) > MAXGENRATIONS) {
					JOptionPane.showMessageDialog(UIGenetski.this, "Unesi broj generacija (izmedju 0, 5000)");
				} else {

					samePopulationCheck = true;
					samePopulation.setEnabled(samePopulationCheck);
					populationSize = Integer.parseInt(txtPopulationSize.getText());
					double mutationRate = Double.parseDouble(txtMutationRate.getText());
					double crossoverRate = Double.parseDouble(txtCrossoverRate.getText());
					int tournamentSize = Integer.parseInt(txtTournamentSize.getText());
					int generations = Integer.parseInt(txtGeneration.getText());
					
					new UpdatingFields (textArea,progressbar,mutationRate,crossoverRate,tournamentSize, generations,populationSize,txtBestDistance,samePopulation).execute();

				}
			}
		});

		JLabel lblVelicinaTurnira = new JLabel("Velicina Turnira:");
		lblVelicinaTurnira.setBounds(28, 229, 80, 14);
		lblVelicinaTurnira.setFont(new Font("Times New Roman", Font.PLAIN, 12));

		txtTournamentSize = new JTextField();
		txtTournamentSize.setBounds(143, 226, 96, 20);
		txtTournamentSize.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		txtTournamentSize.setColumns(10);
		Image img = new ImageIcon(this.getClass().getResource("/TSP2.png")).getImage();
		panel.setLayout(null);

		txtGeneration = new JTextField();
		txtGeneration.setBounds(143, 92, 96, 20);
		panel.add(txtGeneration);
		txtGeneration.setColumns(10);

		JLabel lblGeneration = new JLabel("Broj Generacija:");
		lblGeneration.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblGeneration.setBounds(28, 95, 91, 14);
		panel.add(lblGeneration);

		txtBestDistance = new JTextField();
		txtBestDistance.setBounds(401, 154, 135, 20);
		panel.add(txtBestDistance);
		txtBestDistance.setColumns(30);
		panel.add(btnStart);
		panel.add(lblPopulationSize);
		panel.add(lblMutationRate);
		panel.add(lblStopaUkrstanja);
		panel.add(lblVelicinaTurnira);
		panel.add(txtTournamentSize);
		panel.add(txtCrossoverRate);
		panel.add(txtMutationRate);
		panel.add(txtPopulationSize);
		panel.add(samePopulation);
		panel.add(progressbar);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 336, 452, 171);
		panel.add(scrollPane);
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		DefaultCaret caret = (DefaultCaret) textArea.getCaret(); 
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);  

		JLabel lblBestDistance = new JLabel("Najkraca distanca: ");
		lblBestDistance.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		lblBestDistance.setBounds(295, 150, 96, 28);
		panel.add(lblBestDistance);

		JLabel lblImage = new JLabel("");
		lblImage.setBounds(0, 0, 694, 558);
		lblImage.setIcon(new ImageIcon(img));
		panel.add(lblImage);

	}
}
