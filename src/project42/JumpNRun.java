package project42;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class JumpNRun extends JFrame implements ActionListener{
GUI gui = null;
JPanel pMenu = new JPanel();
JButton btStart = new JButton("Starten"), btEditor = new JButton("Level Editor");

	public static void main(String[] args) {
		new JumpNRun(1200, 800, 12);
	}
	
	public JumpNRun(int width, int height, int length){
		super("Jump and Run");
		setVisible(true);
		Insets insets = getInsets();
		setSize(width+insets.left+insets.right, height+insets.bottom+insets.top);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		btStart.addActionListener(this);
		btEditor.addActionListener(this);
		
		pMenu.add(btStart);
		pMenu.add(btEditor);
		gui = new GUI(width, height, length);
		addKeyListener(gui);
		add(BorderLayout.CENTER,pMenu);
		setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource() == btStart){
			remove(pMenu);
			add(BorderLayout.CENTER,gui);
			validate();
			repaint();
			new Thread(){
				public void run(){
					gui.start();
				}
			}.start();
		}else{
			//Level Editor
		}
	}
}
