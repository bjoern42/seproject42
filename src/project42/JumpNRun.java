package project42;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JumpNRun extends JFrame implements ActionListener{
GUI gui = null;
EditorGUI egui = null;
JPanel pMenu = new JPanel();
JButton btStart = new JButton("Starten"), btEditor = new JButton("Level Editor");
Image img = null;

	public static void main(String[] args) {
		new JumpNRun(1200, 800, 12);
	}
	
	public JumpNRun(int width, int height, int length){
		super("Jump and Run");
		setVisible(true);
		setResizable(false);
		Insets insets = getInsets();
		setSize(width+insets.left+insets.right, height+insets.bottom+insets.top);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setBackground(Color.BLACK);
		
		btStart.addActionListener(this);
		btEditor.addActionListener(this);
		
		pMenu.setLayout(new BoxLayout(pMenu, BoxLayout.Y_AXIS));
		pMenu.setBorder(BorderFactory.createEmptyBorder(getHeight()*3/5, getWidth()/3, getHeight()/4, getWidth()/3));
		btStart.setAlignmentX(Component.CENTER_ALIGNMENT);
		btEditor.setAlignmentX(Component.CENTER_ALIGNMENT);
		pMenu.add(btStart);
		pMenu.add(btEditor);
		
		gui = new GUI(width, height, length);
		egui = new EditorGUI(width, height, length);
		//img = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/background.png"));
		
		add(BorderLayout.CENTER,pMenu);
		setVisible(true);
	}
	
	public void paint(Graphics g){
		super.paintComponents(g);
		//g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
		g.setColor(Color.BLACK);
		g.fillRect(getWidth(),getHeight(), getWidth(),  getHeight());
		
		btEditor.repaint();
		btStart.repaint();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btStart){
			remove(pMenu);
			add(BorderLayout.CENTER,gui);
			validate();
			repaint();
			gui.start();
		}else if(arg0.getSource() == btEditor){
			remove(pMenu);
			add(BorderLayout.CENTER,egui);
			validate();
			repaint();
			egui.start();
		}	
	}
}
