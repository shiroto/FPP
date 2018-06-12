package FPP.LinearOptimization.View.Gui.BranchAndBound;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import java.util.Map.Entry;

import javax.swing.JPanel;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten;
import FPP.LinearOptimization.View.Gui.BandBFrame;
import FPP.LinearOptimization.View.Gui.InputScreenBB;;

/**
 * A JComponent displaying a tree of BranchAndBoundKnoten, given by a
 * {@link TreeLayout}.
 * 
 */
public class BranchAndBoundTreePane extends JPanel {
	private final TreeLayout<BranchAndBoundKnoten> treeLayout;
	private final BranchAndBoundKnoten aktuellerKnoten;

	private TreeForTreeLayout<BranchAndBoundKnoten> getTree() {
		return treeLayout.getTree();
	}

	private Iterable<BranchAndBoundKnoten> getChildren(BranchAndBoundKnoten parent) {
		return getTree().getChildren(parent);
	}

	private Rectangle2D.Double getBoundsOfNode(BranchAndBoundKnoten node) {
		return treeLayout.getNodeBounds().get(node);
	}

	/**
	 * Specifies the tree to be displayed by passing in a {@link TreeLayout} for
	 * that tree.
	 * 
	 * @param treeLayout
	 *            the {@link TreeLayout} to be displayed
	 */
	public BranchAndBoundTreePane(TreeLayout<BranchAndBoundKnoten> treeLayout, BranchAndBoundKnoten aktuellerKnoten) {
		this.treeLayout = treeLayout;
		this.aktuellerKnoten = aktuellerKnoten; // aktueller zu bearbeitender Knoten, in GUI hellblau dargestellt
		this.setBackground(Color.WHITE);
		Dimension size = treeLayout.getBounds().getBounds().getSize();
		setPreferredSize(size);
		this.addMouseListener(new MouseListener() {
			@Override
			public void mouseReleased(MouseEvent e) {
				// nichts tun
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// nichts tun
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// nichts tun
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// nichts tun
			}

			/**
			 * 
			 */
			@Override
			public void mouseClicked(MouseEvent e) {
				for (Entry<BranchAndBoundKnoten, Rectangle2D.Double> knotenEntry : treeLayout.getNodeBounds()
						.entrySet()) {
					if (knotenEntry.getValue().contains(e.getPoint())) {
						BandBFrame frame = new BandBFrame(knotenEntry.getKey()); // Ã¶ffnet Fenster mit Details zu Knoten
						frame.init();
					}
				}
			}
		});
	}

	// -------------------------------------------------------------------
	// painting

	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.orange;
	private final static Color BOX_COLOR_SUCCESS = new Color(202, 255, 112); // Bester Knoten
	private final static Color BOX_COLOR_AKTUELL = new Color(152, 245, 255); // Aktueller Knoten
	private final static Color BOX_COLOR_BEARBEITET = new Color(255, 255, 102); // Abgearbeiteter Knoten
	private final static Color TEXT_COLOR = Color.black;

	private void paintEdges(Graphics g, BranchAndBoundKnoten parent) {
		if (!getTree().isLeaf(parent)) {
			Rectangle2D.Double b1 = getBoundsOfNode(parent);
			double x1 = b1.getCenterX();
			double y1 = b1.getCenterY();
			for (BranchAndBoundKnoten child : getChildren(parent)) {
				Rectangle2D.Double b2 = getBoundsOfNode(child);
				g.drawLine((int) x1, (int) y1, (int) b2.getCenterX(), (int) b2.getCenterY());

				paintEdges(g, child);

			}
		}
	}

	private void paintBox(Graphics g, BranchAndBoundKnoten bAndBKnoten) {

		DecimalFormat df = InputScreenBB.getDecimalFormat();

		// draw the box in the background

		if (bAndBKnoten == aktuellerKnoten) {
			g.setColor(BOX_COLOR_AKTUELL);
		} else if (bAndBKnoten.isBesteLoesung()) {
			g.setColor(BOX_COLOR_SUCCESS);
		} else if (bAndBKnoten.isAusgelotet() || bAndBKnoten.isVerzweigt()) {
			g.setColor(BOX_COLOR_BEARBEITET);
		} else {
			g.setColor(BOX_COLOR);
		}

		Rectangle2D.Double box = getBoundsOfNode(bAndBKnoten);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1, (int) box.height - 1, ARC_SIZE, ARC_SIZE);
		if (bAndBKnoten == aktuellerKnoten) {
			g.setColor(BOX_COLOR_AKTUELL);
		} else if (!bAndBKnoten.isBesteLoesung()) {
			g.setColor(BOX_COLOR);
		} else {
			g.setColor(BOX_COLOR_SUCCESS);
		}
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1, (int) box.height - 1, ARC_SIZE, ARC_SIZE);

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String ausgabe = "P" + Integer.toString(bAndBKnoten.getNummer()) + "\n";

		if (bAndBKnoten.getBranchAndBound().isMax()) {
			ausgabe = ausgabe + "Zielfunktionswert: " + df.format(bAndBKnoten.getProblem().getZFWert()) + "\n";
		} else {

			ausgabe = ausgabe + "Zielfunktionswert: " + df.format(bAndBKnoten.getProblem().getZFWert()) + "\n";
		}

		if (bAndBKnoten.getProblem().isLoesbar()) {
			String temp = "";
			if (bAndBKnoten.getProblem().getErgebnisTableau().getQ() > 6) {
				temp += "Lösungsvektor: \n";
				temp += "(";
				for (int i = 0; i < bAndBKnoten.getProblem().getErgebnisTableau().getQ(); i++) {
					String loesungsString = df.format(bAndBKnoten.getProblem().getLoesung()[i]);
					if (i != bAndBKnoten.getProblem().getErgebnisTableau().getQ() - 1) {
						temp = temp + loesungsString + "/ ";
					} else {
						temp = temp + loesungsString + ")";
					}
				}

			} else {

				for (int i = 0; i < bAndBKnoten.getProblem().getErgebnisTableau().getQ(); i++) {
					temp += "x" + (i + 1) + ": " + df.format(bAndBKnoten.getProblem().getLoesung()[i]);
					temp += "\n";
				}

			}
			temp = temp + "\n";
			ausgabe = ausgabe + temp;
		} else {
			String temp = "Nicht lösbar \n";
			ausgabe = ausgabe + temp;

		}

		String[] lines = ausgabe.split("\n");
		FontMetrics m = getFontMetrics(getFont());
		int x = (int) box.x + ARC_SIZE / 2;
		int y = (int) box.y + m.getAscent() + m.getLeading() + 1;
		for (int i = 0; i < lines.length; i++) {
			g.drawString(lines[i], x, y);
			y += m.getHeight();
		}

		// Verzweigung
		int y2 = (int) box.y + m.getAscent() + m.getLeading() + 120;
		if (bAndBKnoten.isVerzweigt()) {
			String neu = "x" + (bAndBKnoten.getAuslotungsIndex() + 1) + "<=" + bAndBKnoten.getAuslotungAbgerundet()
					+ "     " + "x" + (bAndBKnoten.getAuslotungsIndex() + 1) + ">="
					+ bAndBKnoten.getAuslotungAufgerundet();
			g.drawString(neu, x, y2);
		}

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		paintEdges(g, getTree().getRoot());

		// paint the boxes
		for (BranchAndBoundKnoten textInBox : treeLayout.getNodeBounds().keySet()) {
			paintBox(g, textInBox);
		}
	}
}