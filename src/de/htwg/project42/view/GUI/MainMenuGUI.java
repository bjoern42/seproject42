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
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.htwg.project42.controller.LandscapeInterface;

/**
 * Main class for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public class MainMenuGUI extends JFrame implements ActionListener{
private GUI gui = null;
private EditorGUI egui = null;
private JPanel pMenu, pButtons = new JPanel(),pList = new JPanel(),pCurrent;
private JButton btStart = new JButton("Starten"), btEditor = new JButton("Level Editor");
private JCheckBox cbTUI = new JCheckBox("Enable TUI output",false);

private DefaultListModel<File> model = new DefaultListModel<File>();
private JList<File> list = new JList<File>(model);
private JScrollPane scroll = null;
private int width, height, length;
private static final int GAP = 5, FACTOR_1 = 3, FACTOR_2 = 5, RECT_BORDER = 20, RECT_BORDER_BOTTOM = 100, ROWS = 3;
private LandscapeInterface landscape = null;
	
	/**
	 * Initialises main screen.
	 * @param pWidth - Width
	 * @param pHeight - Height
	 * @param pLength - Visible blocks
	 */
	public MainMenuGUI(LandscapeInterface pLandscape, int pWidth, int pHeight, int pLength){
		super("Jump and Run");
		landscape = pLandscape;
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
		
		egui = new EditorGUI(this, width, height, length);
		
		pMenu.setLayout(new BorderLayout());
		GridLayout layout = new GridLayout(ROWS, 1);
		layout.setVgap(GAP);
		pButtons.setLayout(layout);
		pButtons.setBorder(BorderFactory.createEmptyBorder(getHeight()*FACTOR_1/FACTOR_2, getWidth()/FACTOR_1, getHeight()/FACTOR_2, getWidth()/FACTOR_1));
		
		pButtons.add(btStart);
		pButtons.add(btEditor);
		pButtons.add(cbTUI);
		
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listMaps();
		
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
	 * Lists maps in current directory.
	 */
	private void listMaps(){
		model.clear();
		File[] files = new File(System.getProperty("user.dir")).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".lvl");
		    }
		});
		for(int i=0; i<files.length; i++){
			model.add(i,files[i]);
		}
		list.setSelectedIndex(0);
	}
	
	/**
	 * Button handling.
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btStart){
			if(landscape.loadLevel((File)list.getSelectedValue())){
				gui = new GUI(this, landscape, cbTUI.isSelected());
				
				changePanel(gui);
				gui.start();
			}else{
				JOptionPane.showMessageDialog(this, "Selected map has failures!", "Error loading map", JOptionPane.ERROR_MESSAGE);
			}
		}else if(arg0.getSource() == btEditor){
			changePanel(egui);
			egui.init();
		}
	}
	
	/**
	 * Changes to main panel.
	 */
	public void reset(){
		changePanel(pMenu);
		listMaps();
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
