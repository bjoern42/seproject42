package project42;

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
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class JumpNRun extends JFrame implements ActionListener{
GUI gui = null;
EditorGUI egui = null;
JPanel pMenu, pButtons = new JPanel(),pList = new JPanel(),pCurrent;
JButton btStart = new JButton("Starten"), btEditor = new JButton("Level Editor");
Image img = null;
JList<File> list = null;
JScrollPane scroll = null;
int width, height, length;

	public static void main(String[] args) {
		new JumpNRun(1200, 800, 12);
	}
	
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
		
		img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/mainframe.png"));
		pMenu = new ImgPanel(img);
		
		egui = new EditorGUI(width, height, length);
		
		pMenu.setLayout(new BorderLayout());
		GridLayout layout = new GridLayout(2, 1);
		layout.setVgap(5);
		pButtons.setLayout(layout);
		pButtons.setBorder(BorderFactory.createEmptyBorder(getHeight()*3/5, getWidth()/3, getHeight()/4, getWidth()/3));
		pButtons.add(btStart);
		pButtons.add(btEditor);
		
		File[] files = new File(System.getProperty("user.dir")).listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		        return name.toLowerCase().endsWith(".lvl");
		    }
		});
		list = new JList<File>(files);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		
		pList.setBorder(BorderFactory.createEmptyBorder(getHeight()/3, getWidth()/3, getHeight()/5, getWidth()/3));
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
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btStart){
			gui = new GUI(this, list.getSelectedValue(),width, height, length);
			changePanel(gui);
			gui.start();
		}else if(arg0.getSource() == btEditor){
			changePanel(egui);
		}
	}
	
	public void reset(){
		changePanel(pMenu);
	}
	
	private void changePanel(JPanel panel){
		remove(pCurrent);
		pCurrent = panel;
		add(BorderLayout.CENTER,panel);
		validate();
		repaint();
	}
	
	private class ImgPanel extends JPanel{
		Image img;
		
		public ImgPanel(Image pImg){
			super();
			img = pImg;
		}
		
		@Override
		public void paint(Graphics g){
			g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
			g.setColor(Color.GRAY);
			g.fillRect(scroll.getX()-20, scroll.getY()-20, scroll.getWidth()+40, btEditor.getY()+btEditor.getHeight()-scroll.getY()+100);
			btEditor.repaint();
			btStart.repaint();
			list.repaint();
		}
	}
}
