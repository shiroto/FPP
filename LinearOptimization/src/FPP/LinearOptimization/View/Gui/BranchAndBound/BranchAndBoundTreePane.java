package FPP.LinearOptimization.View.Gui.BranchAndBound;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;

import javax.swing.JPanel;

import org.abego.treelayout.TreeForTreeLayout;
import org.abego.treelayout.TreeLayout;

import FPP.LinearOptimization.Model.BranchAndBound.BranchAndBoundKnoten;

/**
 * A JComponent displaying a tree of BranchAndBoundKnoten, given by a
 * {@link TreeLayout}.
 * 
 */
public class BranchAndBoundTreePane extends JPanel {
	private final TreeLayout<BranchAndBoundKnoten> treeLayout;

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
	public BranchAndBoundTreePane(TreeLayout<BranchAndBoundKnoten> treeLayout) {
		this.treeLayout = treeLayout;
		this.setBackground(Color.WHITE);
		Dimension size = treeLayout.getBounds().getBounds().getSize();
		setPreferredSize(size);
	}

	// -------------------------------------------------------------------
	// painting

	private final static int ARC_SIZE = 10;
	private final static Color BOX_COLOR = Color.orange;
	private final static Color BORDER_COLOR = Color.darkGray;
	private final static Color BORDER_COLOR_SUCCESS = Color.green;
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
		DecimalFormat df = new DecimalFormat("#.##");

		// draw the box in the background
		
		
		if (!bAndBKnoten.isBesteLoesung()) {
			g.setColor(BOX_COLOR);
		} else {
			g.setColor(BORDER_COLOR_SUCCESS);
		}
		
		Rectangle2D.Double box = getBoundsOfNode(bAndBKnoten);
		g.fillRoundRect((int) box.x, (int) box.y, (int) box.width - 1, (int) box.height - 1, ARC_SIZE, ARC_SIZE);
		if (!bAndBKnoten.isBesteLoesung()) {
			g.setColor(BORDER_COLOR);
		} else {
			g.setColor(BORDER_COLOR_SUCCESS);
		}
		g.drawRoundRect((int) box.x, (int) box.y, (int) box.width - 1, (int) box.height - 1, ARC_SIZE, ARC_SIZE);

		// draw the text on top of the box (possibly multiple lines)
		g.setColor(TEXT_COLOR);
		String ausgabe = "P" + Integer.toString(bAndBKnoten.getNummer()) + "\n";

		if (bAndBKnoten.getBranchAndBound().isMax()) {
			ausgabe = ausgabe + "Obere Schranke: " + df.format(bAndBKnoten.getProblem().getZFWert()) + "\n";
		} else {

			ausgabe = ausgabe + "Lokale Schranke: " + df.format(bAndBKnoten.getProblem().getZFWert()) + "\n";
		}
		//
		// String temp= "";
		// for(int i = 0; i <bAndBKnoten.getProblem().getErgebnisTableau().getQ(); i ++)
		// {
		// String test= df.format( bAndBKnoten.getProblem().getLoesung()[i] ) ;
		// if(i!= bAndBKnoten.getProblem().getErgebnisTableau().getQ()-1) {
		// temp = temp+ "x"+(i+1)+"="+ test+" ";
		// }else {
		// temp = temp+ "x"+(i+1)+"="+ test;
		// }
		// }
		// temp = temp + "\n";
		// ausgabe = ausgabe+ temp;
		if(bAndBKnoten.getProblem().isLoesbar()) {
		String temp = "(";
		for (int i = 0; i < bAndBKnoten.getProblem().getErgebnisTableau().getQ(); i++) {
			String loesungsString = df.format(bAndBKnoten.getProblem().getLoesung()[i]);
			if (i != bAndBKnoten.getProblem().getErgebnisTableau().getQ() - 1) {
				temp = temp + loesungsString + "/ ";
			} else {
				temp = temp + loesungsString + ")";
			}
		}
		 
		temp = temp + "\n";
		ausgabe = ausgabe + temp;
		}
		else {
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
		int y2 = (int) box.y + m.getAscent() + m.getLeading() + 110;
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