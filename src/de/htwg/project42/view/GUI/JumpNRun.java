package de.htwg.project42.view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FilenameFilter;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.htwg.project42.controller.iLandscape;
import de.htwg.project42.model.GameObjects.iLevel;
import de.htwg.project42.model.GameObjects.iLevelLoader;
import de.htwg.project42.model.GameObjects.iPlayer;
import de.htwg.project42.view.EditorGUI.EditorGUI;

import de.htwg.project42.model.GameObjects.Implementation.Level;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;
import de.htwg.project42.model.GameObjects.Implementation.Player;
import de.htwg.project42.controller.Implementation.Landscape;

/**
 * Main class for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public class JumpNRun extends JFrame implements ActionListener{
private GUI gui = null;
private EditorGUI egui = null;
private JPanel pMenu, pButtons = new JPanel(),pList = new JPanel(),pCurrent;
private JButton btStart = new JButton("Starten"), btEditor = new JButton("Level Editor");
private JCheckBox cbTUI = new JCheckBox("Enable TUI output",false);
@SuppressWarnings("rawtypes")
private JList list = null;
private JScrollPane scroll = null;
private int width, height, length;
private static final int LANDSCAPE_SIZE_X = 1200, LANDSCAPE_SIZE_Y = 800, LANDSCAPE_LENGTH = 12, GAP = 5, FACTOR_1 = 3, FACTOR_2 = 5, RECT_BORDER = 20, RECT_BORDER_BOTTOM = 100;

	public static void main(String[] args) {
		new JumpNRun(LANDSCAPE_SIZE_X, LANDSCAPE_SIZE_Y, LANDSCAPE_LENGTH);
	}
	
	/**
	 * Initialises main screen.
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param pLength - Visible blocks
	 */
	//JList<File> was not accepted in sonar.
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public JumpNRun(int pWidth, int pHeight, int pLength){
		super("Jump and Run");
		width = pWidth;
		height = pHeight;
		length = pLength;
		setResizable(false);
		Insets insets = getInsets();
		setSize(width+insets.left+insets.right, height+insets.bottom+insets.top);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		btStart.addActionListener(this);
		btEditor.addActionListener(this);
		
		Image img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/mainframe.png"));
		pMenu = new ImgPanel(img);
		
		egui = new EditorGUI(width, height, length);
		
		pMenu.setLayout(new BorderLayout());
		GridLayout layout = new GridLayout(3, 1);
		layout.setVgap(GAP);
		pButtons.setLayout(layout);
		pButtons.setBorder(BorderFactory.createEmptyBorder(getHeight()*FACTOR_1/FACTOR_2, getWidth()/FACTOR_1, getHeight()/FACTOR_2, getWidth()/FACTOR_1));
		
		pButtons.add(btStart);
		pButtons.add(btEditor);
		pButtons.add(cbTUI);
		
		File[] files = new File(System.getProperty("user.dir")).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".lvl");
		    }
		});
		list = new JList(files);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		
		pList.setBorder(BorderFactory.createEmptyBorder(getHeight()/FACTOR_1, getWidth()/FACTOR_1, getHeight()/FACTOR_2, getWidth()/FACTOR_1));
		scroll = new JScrollPane(list);
		
		pList.add(scroll);
		
		pButtons.setOpaque(false);
		pList.setOpaque(false);
		pMenu.add(BorderLayout.NORTH,pList);
		JPanel tmp = new JPanel();
		tmp.add(pButtons);
		pMenu.add(BorderLayout.SOUTH,tmp);
		add(BorderLayout.CENTER,pMenu);
		
		
		setVisible(true);
		
		pCurrent = pMenu;
		scroll.setPreferredSize(new Dimension(scroll.getWidth(), btStart.getY()-scroll.getY()));
	}
	
	/**
	 * Button handling.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btStart){
			iPlayer player = new Player(length*width/length/2, 0, width/length, width/length*2);
			iLevelLoader loader = new LevelLoader((File)list.getSelectedValue());
			iLevel level = new Level(loader, player, width/length ,length+2);
			iLandscape landscape = new Landscape(player, level, width, height);
			gui = new GUI(this, landscape, cbTUI.isSelected());
			landscape.addAnObserver(gui);
			
			changePanel(gui);
			gui.start();
		}else if(arg0.getSource() == btEditor){
			changePanel(egui);
		}
	}
	
	/**
	 * Changes to main panel.
	 */
	public void reset(){
		changePanel(pMenu);
	}
	
	/**
	 * Changes visible panel.
	 * @param panel - panel to be visible
	 */
	private void changePanel(JPanel panel){
		remove(pCurrent);
		pCurrent = panel;
		add(BorderLayout.CENTER,panel);
		validate();
		repaint();
	}
	
	/**
	 * JPanel with background image.
	 * @author bjeschle,toofterd
	 * @version 1.0
	 */
	private class ImgPanel extends JPanel{
		private Image img;
		
		/**
		 * Creates an ImgPanel
		 * @param pImg - Background image
		 */
		public ImgPanel(Image pImg){
			super();
			img = pImg;
		}
		
		/**
		 * Draws background.
		 */
		@Override
		public void paint(Graphics g){
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			g.setColor(Color.GRAY);
			g.fillRect(scroll.getX()-RECT_BORDER, scroll.getY()-RECT_BORDER, scroll.getWidth()+RECT_BORDER*2, btEditor.getY()+btEditor.getHeight()-scroll.getY()+RECT_BORDER_BOTTOM);
			btEditor.repaint();
			btStart.repaint();
			cbTUI.repaint();
			list.repaint();
		}
	}
}
