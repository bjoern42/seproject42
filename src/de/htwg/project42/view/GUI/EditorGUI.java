package de.htwg.project42.view.GUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.LevelLoaderInterface;
import de.htwg.project42.model.GameObjects.Implementation.LevelLoader;

/**
 * EditorGUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EditorGUI extends JPanel implements ActionListener{
private List<BlockButton[]> objects = new LinkedList<BlockButton[]>();
private HashMap<Integer, BlockButton> blocks = new HashMap<Integer, BlockButton>();
private JPanel pLandscape = new JPanel(), pBlocks = new JPanel(), pSelectableBlocks = new JPanel(), pSettings = new JPanel();
private JLabel lbSelected = new JLabel();
private JButton btRight = new JButton(">"), btLeft = new JButton("<"), btSave, btLoad, btQuit;
private LevelLoaderInterface levelLoader = new LevelLoader();
private int size, selected, columns, rows, start, index=-1;
private EditorGUI instance = null;
private MainMenuGUI main = null;
private JScrollPane scrollPane = new JScrollPane(pSelectableBlocks,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
private JFileChooser fileChooser = null;

	public EditorGUI(MainMenuGUI pMain, int pWidth, int pHeight, int pLength){
		size = pWidth / pLength;
		instance = this;
		main = pMain;
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH,pBlocks);
		add(BorderLayout.CENTER,pLandscape);
		
		selected = BlockInterface.TYP_GRAS;
		blocks.put(BlockInterface.TYP_ENEMY, new BlockButton(BlockInterface.TYP_ENEMY));
		blocks.get(BlockInterface.TYP_ENEMY).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy.gif"))));
		blocks.put(BlockInterface.TYP_GRAS, new BlockButton(BlockInterface.TYP_GRAS));
		blocks.get(BlockInterface.TYP_GRAS).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"))));
		blocks.put(BlockInterface.TYP_WATER, new BlockButton(BlockInterface.TYP_WATER));
		blocks.get(BlockInterface.TYP_WATER).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/water.png"))));
		blocks.put(BlockInterface.TYP_COIN, new BlockButton(BlockInterface.TYP_COIN));
		blocks.get(BlockInterface.TYP_COIN).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coin.png"))));
		blocks.put(BlockInterface.TYP_GOAL, new BlockButton(BlockInterface.TYP_GOAL));
		blocks.get(BlockInterface.TYP_GOAL).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/goal.png"))));
		blocks.put(BlockInterface.TYP_CRATE, new BlockButton(BlockInterface.TYP_CRATE));
		blocks.get(BlockInterface.TYP_CRATE).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/crate.png"))));
		blocks.put(BlockInterface.TYP_BUTTON, new BlockButton(BlockInterface.TYP_BUTTON));
		blocks.get(BlockInterface.TYP_BUTTON).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/buttonReleased.png"))));
		blocks.put(BlockInterface.TYP_GATE, new BlockButton(BlockInterface.TYP_GATE));
		blocks.get(BlockInterface.TYP_GATE).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gateClosed.png"))));
		blocks.put(BlockInterface.TYP_AIR, new BlockButton(BlockInterface.TYP_AIR));
		blocks.get(BlockInterface.TYP_AIR).setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/delete.png"))));
		btSave = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/save.png"))));
		btLoad = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/load.png"))));
		btQuit = new JButton(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/quit.png"))));
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockButton button = (BlockButton)e.getSource();
				selected = button.getType();
				if(selected == BlockInterface.TYP_BUTTON || selected == BlockInterface.TYP_GATE){
					try{
						index = Integer.parseInt(JOptionPane.showInputDialog(instance,"Please enter an index","Enter index", JOptionPane.OK_OPTION));
					}catch(Exception e1){
						index = -1;
					}
				}else{
					index = -1;
				}
				lbSelected.setIcon(button.getIcon());
			}
		};
		for(Map.Entry<Integer,BlockButton> entry:blocks.entrySet()){
			entry.getValue().setPreferredSize(new Dimension(size, size));
			entry.getValue().addActionListener(listener);
			pSelectableBlocks.add(entry.getValue());
		}

		lbSelected.setPreferredSize(new Dimension(size, size));
		lbSelected.setIcon(blocks.get(BlockInterface.TYP_GRAS).getIcon());
		btSave.addActionListener(this);
		btSave.setPreferredSize(new Dimension(size/2, size/2));
		btLoad.addActionListener(this);
		btLoad.setPreferredSize(new Dimension(size/2, size/2));
		btQuit.addActionListener(this);
		btQuit.setPreferredSize(new Dimension(size/2, size/2));
		
		pSettings.setLayout(new GridLayout(2, 2));
		pSettings.add(btSave);
		pSettings.add(btLoad);
		pSettings.add(btQuit);
		
		pBlocks.setLayout(new BorderLayout());
		pBlocks.add(BorderLayout.CENTER,scrollPane);
		pBlocks.add(BorderLayout.WEST,lbSelected);
		pBlocks.add(BorderLayout.EAST,pSettings);
		
		btLeft.addActionListener(this);
		btRight.addActionListener(this);
		add(BorderLayout.WEST,btLeft);
		add(BorderLayout.EAST,btRight);
		
		fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setFileFilter(new FileFilter() {
			@Override
			public String getDescription() {
				return "Level files";
			}
			@Override
			public boolean accept(File arg0) {
				if(arg0.getName().toLowerCase().endsWith(".lvl")){
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Initializes the level editor.
	 */
	public void init(){
		objects.clear();
		rows = pLandscape.getHeight()/size+2;
		columns = pLandscape.getWidth()/size+1;
		start = 0;
		
		pLandscape.setLayout(new GridLayout(rows, columns));
		for(int i=0; i<columns+2; i++){
			addEmptyRow();
		}
		loadLandscape(objects);

		validate();
	}
	
	/**
	 * Adds an empty Row to the landscape.
	 */
	private void addEmptyRow(){
		BlockButton blocks[] = new BlockButton[rows];
		for(int j=0; j<rows; j++){
			blocks[j] = new BlockButton(BlockInterface.TYP_AIR);
			blocks[j].setPreferredSize(new Dimension(size, size));
			blocks[j].addActionListener(this);
		}
		objects.add(blocks);
	}
	
	/**
	 * Loads the landscape with the given blocks.
	 * @param visibleObjects - blocks
	 */
	private void loadLandscape(List<BlockButton[]> visibleObjects){
		pLandscape.removeAll();
		for(int i=0; i<rows; i++){
			for(int j=0; j<columns; j++){
				pLandscape.add(visibleObjects.get(j)[i]);
			}
		}
		validate();
		repaint();
	}
	
	/**
	 * Loads a map.
	 */
	private void loadMap(){
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if(file != null){
			levelLoader.setInputFile(file);
			int buffer[] = null;
			objects.clear();
			while((buffer = levelLoader.readNext()) != null){
				LinkedList<BlockButton> buttons = new LinkedList<BlockButton>();
				for(int i=0; i<buffer.length; i++){
					buttons.add(new BlockButton(buffer[i]));
					if(buffer[i] != BlockInterface.TYP_AIR){
						buttons.getLast().setIcon(blocks.get(buffer[i]).getIcon());
					}
					buttons.getLast().setPreferredSize(new Dimension(size, size));
					buttons.getLast().addActionListener(this);
					
					if(buffer[i] == BlockInterface.TYP_BUTTON || buffer[i] == BlockInterface.TYP_GATE){
						buttons.getLast().setIndex(buffer[++i]);
					}
				}
				objects.add(buttons.toArray(new BlockButton[buttons.size()]));
			}
		}
	}
	
	/**
	 * Saves the map.
	 */
	private void saveMap(){
		fileChooser.showSaveDialog(this);
		File file = fileChooser.getSelectedFile();
		if(file == null){
			return;
		}
		if(!file.getName().endsWith(".lvl")){
			System.out.println(file.getName());
			file = new File(file.getAbsoluteFile()+".lvl");
		}
		levelLoader.setOutputFile(file);
		for(int i=0; i<objects.size(); i++){
			BlockButton blocks[] = objects.get(i);
			LinkedList<Integer> line = new LinkedList<Integer>();
			for(int j=0; j<blocks.length; j++){
				line.add(blocks[j].getType());
				if(blocks[j].getIndex() != -1){
					line.add(blocks[j].getIndex());
				}
			}
			levelLoader.writeNext(line.toArray(new Integer[line.size()]));
		}
		levelLoader.closeStreams();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btRight){
			addEmptyRow();
			start++;
			loadLandscape(objects.subList(start, start+columns));
		}else if(e.getSource() == btLeft){
			if(start>0){
				start--;
				loadLandscape(objects.subList(start, start+columns));
			}
		}else if(e.getSource() == btSave){
			saveMap();
		}else if(e.getSource() == btLoad){
			loadMap();
			start = 0;	
			loadLandscape(objects.subList(start, start+columns));
		}else if(e.getSource() == btQuit){
			main.reset();
		}else{
			BlockButton button = (BlockButton)e.getSource();
			if(selected == BlockInterface.TYP_AIR) {
				button.setIcon(null);
			}else{
				button.setIcon(blocks.get(selected).getIcon());
			}
			button.setType(selected);
			button.setIndex(index);
		}
	}
	
	/**
	 * Button for EditorGUI
	 * @author bjeschle,toofterd
	 * @version 1.0
	 */
	private class BlockButton extends JButton{
		int blockType, index;
		
		public BlockButton(int pBlockType){
			super();
			blockType = pBlockType;
			index = -1;
			setOpaque(true);
		}
		
		/**
		 * Returns the blocktype.
		 * @return blocktype
		 */
		public int getType(){
			return blockType;
		}
		
		/**
		 * Sets the blocktype.
		 * @param pBlockType
		 */
		public void setType(int pBlockType){
			blockType = pBlockType;
		}
		
		/**
		 * Returns the index.
		 * @return index
		 */
		public int getIndex(){
			return index;
		}
		
		/**
		 * Sets the index.
		 * @param pIndex
		 */
		public void setIndex(int pIndex){
			index = pIndex;
		}
		
		@Override
		public void paint(Graphics g){
			super.paint(g);
			if(index != -1){
				g.setColor(Color.RED);
				g.drawString(String.valueOf(index), 10, 20);
			}
		}
	}
}
