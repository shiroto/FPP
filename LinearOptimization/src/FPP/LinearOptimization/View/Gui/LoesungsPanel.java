package FPP.LinearOptimization.View.Gui;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.concurrent.*;

import javax.swing.*;

import FPP.LinearOptimization.Model.BranchAndBound.*;
import FPP.LinearOptimization.View.Gui.BranchAndBound.*;

public class LoesungsPanel extends JPanel implements BranchAndBoundSteuerung {

	MainFrame hauptFenster;
	private JPanel baumPanel = new JPanel();
	private JButton weiter = new JButton("Nächster Schritt");
	private JButton gesamtloesungZeigen = new JButton("Lösung anzeigen");
	private JPanel buttonPanel;
	private JLabel loesungsLabel = new JLabel("");

	private volatile boolean pause;

	private final CyclicBarrier naechsterSchrittBarrier = new CyclicBarrier(2);

	public LoesungsPanel(MainFrame mainFrame) {
		this.hauptFenster = mainFrame;
		this.setLayout(new GridBagLayout());
		baumPanel.setBackground(Color.WHITE);
		baumPanel.setLayout(new GridBagLayout());
		weiter.addActionListener(e -> {
			try {
				naechsterSchrittBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e1) {
				// ignorieren
			}
			weiter.setEnabled(false);
		});

		gesamtloesungZeigen.addActionListener(e -> {
			pause = false;
			gesamtloesungZeigen.setEnabled(false);
			// nicht 100% thread safe weil der Algorithmusthread
			// in die barrier laufen kann, nachdem die PrÃ¼fung hier
			// gemacht wird, im
			// schlimmsten Fall dann noch einmal auf "weiter" drÃ¼cken
			if (naechsterSchrittBarrier.getNumberWaiting() == 1) {
				try {
					naechsterSchrittBarrier.await();
				} catch (InterruptedException | BrokenBarrierException e1) {
					// ignore
				}
				weiter.setEnabled(false);
			}
		});
	}

	@Override
	public void algorithmusBeginnt() {
		try {
			SwingUtilities.invokeAndWait(() -> {
				pause = true;
				removeAll();
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 0;
				gbc.fill = GridBagConstraints.BOTH;
				gbc.weighty = 1;
				gbc.weightx = 1;
				baumPanel.removeAll();
				JScrollPane scrollPane = new JScrollPane(baumPanel);
				scrollPane.getVerticalScrollBar().setUnitIncrement(20);
				add(scrollPane, gbc);
				gbc.gridy++;
				gbc.weighty = 0;
				gbc.anchor = GridBagConstraints.NORTH;
				gbc.fill = GridBagConstraints.NONE;
				gbc.insets = new Insets(5, 5, 5, 5);
				buttonPanel = new JPanel();
				buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
				buttonPanel.add(weiter);
				buttonPanel.add(Box.createHorizontalStrut(5));
				buttonPanel.add(gesamtloesungZeigen);
				weiter.setEnabled(false);
				gesamtloesungZeigen.setEnabled(true);
				add(buttonPanel, gbc);
				//this.hauptFenster.getProblemPanel().getNeue().setEnabled(false);
				//his.hauptFenster.getProblemPanel().getGo().setEnabled(false);
				revalidate();
				repaint();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void naechsterSchritt(List<BranchAndBoundKnoten> knoten, BranchAndBoundKnoten aktuellerKnoten) {
		boolean pause = this.pause;
		try {
			SwingUtilities.invokeAndWait(() -> {
				refreshBaum(knoten, aktuellerKnoten);
				if (pause) {
					weiter.setEnabled(true);
				}
				revalidate();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RuntimeException(e);
		}
		if (pause) {
			try {
				naechsterSchrittBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				// ignore
			}
		}
	}

	private void refreshBaum(List<BranchAndBoundKnoten> knoten, BranchAndBoundKnoten aktuellerKnoten) {
		BaumInitialisierer sd = new BaumInitialisierer();
		sd.initialisiere(knoten, aktuellerKnoten);
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTH;
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.NONE;
		baumPanel.removeAll();
		baumPanel.add(sd.getPanel(), gbc); // fÃ¼gt Baumpanel hinzu
	}

	@Override
	public void setLoesung(List<double[]> loesung, List<BranchAndBoundKnoten> knoten) {
		try {
			SwingUtilities.invokeAndWait(() -> {
				refreshBaum(knoten, null);
				GridBagConstraints gbc = new GridBagConstraints();
				gbc.gridx = 0;
				gbc.gridy = 2;
				gbc.weightx = 1;
				gbc.anchor = GridBagConstraints.CENTER;
				gbc.insets = new Insets(5, 5, 5, 5);
				gbc.fill = GridBagConstraints.HORIZONTAL;
				loesungsLabel.setText(erstelleLoesung(loesung));
				loesungsLabel.setMinimumSize(new Dimension(200, 100));
				add(loesungsLabel, gbc);
				weiter.setEnabled(false);
				gesamtloesungZeigen.setEnabled(false);
				//this.hauptFenster.getProblemPanel().getNeue().setEnabled(true);
				//this.hauptFenster.getProblemPanel().getGo().setEnabled(true);
				revalidate();
			});
		} catch (InvocationTargetException | InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private String erstelleLoesung(List<double[]> loesung) {

		DecimalFormat df = InputScreenBB.getDecimalFormat();
		String ausgabe = "<html> <strong> Lösung </strong> <br>";
		for (int i = 0; i < loesung.size(); i++) {
			double[] temp = loesung.get(i);
			String zeileTemp = "";
			for (int e = 0; e < temp.length; e++) {
				if (e != temp.length - 1)
					zeileTemp = zeileTemp + "x" + "<sub>"+(e + 1)+ "</sub>" + ": " + df.format(temp[e]) + "&nbsp; &nbsp; &nbsp; ";
				else
					zeileTemp = zeileTemp + "Zielfunktion" + ": " + df.format(temp[e]) + " ";
			}
			ausgabe = ausgabe + zeileTemp + "<br>";
		}
		return ausgabe + "</html>";
	}

	public void zoomIn() {
		zoomInFont();
		zoomInSizes();

	}

	private void zoomInFont() {
		Font f = loesungsLabel.getFont();
		this.loesungsLabel.setFont(new Font(f.getName(), f.getStyle(), f.getSize() + 1));

	}

	private void zoomInSizes() {
		this.loesungsLabel.setSize(loesungsLabel.getWidth() + 10, loesungsLabel.getHeight() + 2);

	}

	public void zoomOut() {
		zoomOutFont();
		zoomOutSizes();
	}

	private void zoomOutSizes() {
		this.loesungsLabel.setSize(loesungsLabel.getWidth() - 10, loesungsLabel.getHeight() - 2);

	}

	private void zoomOutFont() {
		Font f = loesungsLabel.getFont();
		this.loesungsLabel.setFont(new Font(f.getName(), f.getStyle(), f.getSize() - 1));

	}

}
