package geometry.gui;

import java.awt.*;
import javax.swing.*;

public class Fenetre extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Fenetre(String title, JPanel panneau) {
		// operation de fermeture
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// extraire les dimensions
		Toolkit kit = Toolkit.getDefaultToolkit();
		Dimension screenSize = kit.getScreenSize();

		// centrer l'image
		setSize(screenSize.width / 2, screenSize.height / 2);
		setLocationRelativeTo(null);

		setTitle(title);

		// ajouter le panneau au cadre
		this.setContentPane(panneau);
		this.setVisible(true);
	}
}
