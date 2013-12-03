package geometry.arbreDependance;

import static org.junit.Assert.fail;
import static org.junit.Assert.assertEquals;
import geometry.arbreDependance.ArbreDependance;
import geometry.gui.Panneau;
import geometry.model.Point;
import geometry.model.Segment;

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

public class ArbreDependanceTest {

	Panneau panneau;
	List<Segment> s;

	@Before
	public void setUp() throws Exception {
		panneau = new Panneau();
		s = new ArrayList<Segment>();
	}

	private void test(int nbTrapezoid) {
		ArbreDependance ad = ArbreDependance.create2(s);
		panneau.add(ad);
		if (!Dialog.isCorrect(panneau)) {
			fail();
		}
		if (nbTrapezoid > 0) {
			assertEquals(nbTrapezoid, ad.getMap().getTrapezoids().size());
		}
	}

	@Test
	public void testCreate21() {
		s.add(new Segment(new Point(50, 100), new Point(150, 100)));
		test(4);
	}

	@Test
	public void testCreate22() {
		s.add(new Segment(new Point(50, 100), new Point(150, 100)));
		s.add(new Segment(new Point(250, 100), new Point(350, 100)));
		test(7);
	}

	@Test
	public void testCreate23() {
		s.add(new Segment(new Point(50, 100), new Point(150, 100)));
		s.add(new Segment(new Point(250, 100), new Point(350, 100)));
		s.add(new Segment(new Point(100, 200), new Point(300, 200)));
		test(0);
	}

	@Test
	public void testCreate24() {
		s.add(new Segment(new Point(50, 100), new Point(150, 100)));
		s.add(new Segment(new Point(250, 100), new Point(350, 100)));
		s.add(new Segment(new Point(100, 200), new Point(300, 200)));
		s.add(new Segment(new Point(125, 240), new Point(325, 240)));
		test(0);
	}

	@Test
	public void testCreate25() {
		s.add(new Segment(new Point(50, 100), new Point(150, 100)));
		s.add(new Segment(new Point(250, 100), new Point(350, 100)));
		s.add(new Segment(new Point(100, 50), new Point(300, 200)));
		test(0);
	}

	@Test
	public void testCreate26() {
		s.add(new Segment(new Point(50, 200), new Point(100, 150)));
		s.add(new Segment(new Point(50, 200), new Point(75, 225)));
		s.add(new Segment(new Point(75, 225), new Point(300, 212)));
		s.add(new Segment(new Point(300, 212), new Point(150, 120)));
		test(0);
	}

	@Test
	public void testCreate27() {
		s.add(new Segment(new Point(50, 200), new Point(100, 150)));
		s.add(new Segment(new Point(50, 200), new Point(75, 225)));
		s.add(new Segment(new Point(75, 225), new Point(300, 212)));
		s.add(new Segment(new Point(300, 212), new Point(150, 120)));
		s.add(new Segment(new Point(50, 200), new Point(200, 175)));
		test(0);
	}

	public static class Dialog extends JDialog {

		private static final long serialVersionUID = 1L;

		public static boolean isCorrect(Panneau panneau) {
			Dialog d = new Dialog(panneau);
			d.setVisible(true);
			return d.result;
		}

		private boolean result;

		public Dialog(Panneau panneau) {
			super((Frame) null, true);
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
