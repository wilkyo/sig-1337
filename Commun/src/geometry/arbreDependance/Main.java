package geometry.arbreDependance;

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

import data.model.Node;
import data.model.structure.Building;

public class Main {

	static Panneau panneau = new Panneau();
	static List<Building> s = new ArrayList<Building>();

	public static void test() {
		ArbreDependance.create(s, new Callback() {
			public void action(ArbreDependance ad) {
				System.out.println(ad.toString());
				if (!Dialog.isCorrect(ad)) {
					System.exit(0);
				}
			}
		});
	}

	public static Building b(Node... nodes) {
		return new Building(1, "Test", nodes, null);
	}

	public static Node n(double x, double y) {
		return new Node(0, y, x);
	}

	public static void main(String[] args) {
		s.add(b(n(100, 200), n(190, 100), n(300, 150), n(210, 250)));
		s.add(b(n(120, 70), n(130, 90), n(170, 80), n(140, 50)));
		//s.add(b(n(10, 100), n(110, 110), n(200, 20), n(100, 10)));
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
