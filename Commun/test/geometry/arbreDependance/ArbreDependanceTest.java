package geometry.arbreDependance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import geometry.arbreDependance.ArbreDependance.Callback;
import geometry.gui.Panneau;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

import org.junit.Before;
import org.junit.Test;

import data.model.Node;
import data.model.structure.Building;

public class ArbreDependanceTest {

	Panneau panneau;
	List<Building> s;

	@Before
	public void setUp() throws Exception {
		panneau = new Panneau();
		s = new ArrayList<Building>();
	}

	private void test() {
		ArbreDependance.create(s, new Callback() {
			public void action(ArbreDependance ad) {
				StringBuffer sb = new StringBuffer();
				ad.getMap().toXML(sb, "");
				ad.toXML(sb, "");
				System.out.println(sb.toString());
				if (!Dialog.isCorrect(ad)) {
					fail();
				}
			}
		});
	}

	private Building b(Node... nodes) {
		return new Building(1, "Test", nodes, null);
	}

	private Node n(double x, double y) {
		return new Node(0, y, x);
	}

	@Test
	public void testCreate21() {
		s.add(b(n(100, 200), n(190, 100), n(300, 150), n(210, 250)));
		test();
	}

	public static class Dialog extends JDialog {

		private static final long serialVersionUID = 1L;

		public static boolean isCorrect(ArbreDependance ad) {
			Dialog d = new Dialog(ad);
			d.setVisible(true);
			return d.result;
		}

		private boolean result;

		public Dialog(ArbreDependance ad) {
			super((Frame) null, true);
			Panneau panneau = new Panneau();
			panneau.add(ad);
			JButton incorrect = new JButton("Incorrect");
			JButton correct = new JButton("Correct");
			JPanel boutons = new JPanel();
			boutons.setLayout(new FlowLayout(FlowLayout.RIGHT));
			boutons.add(incorrect);
			boutons.add(correct);
			JPanel panel = new JPanel(new BorderLayout());
			panel.add(panneau, BorderLayout.CENTER);
			panel.add(boutons, BorderLayout.SOUTH);
			setContentPane(panel);
			Toolkit kit = Toolkit.getDefaultToolkit();
			Dimension screenSize = kit.getScreenSize();
			setSize(screenSize.width / 2, screenSize.height / 2);
			setLocationRelativeTo(null);

			incorrect.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					result = false;
					setVisible(false);
				}

			});

			correct.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					result = true;
					setVisible(false);
				}

			});
		}

	}

}
