package de.htwg.project42.view.GUI;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.filechooser.FileFilter;

import de.htwg.project42.model.GameObjects.BlockInterface;
import de.htwg.project42.model.GameObjects.LevelLoaderInterface;

/**
 * EditorGUI for JumpNRun.
 * @author bjeschle,toofterd
 * @version 1.0
 */
@SuppressWarnings("serial")
public class EditorGUI extends JPanel implements ActionListener, AdjustmentListener{
private static final int INDEX_START = 100, OVAL_SIZE = 20, INDEX_X = 5, INDEX_Y = 15, STROKE_SIZE = 5, SCROLLBAR_START_MAX = 3;
private List<BlockButton[]> objects = new LinkedList<BlockButton[]>();
private Map<Integer, BlockButton> blocks = new HashMap<Integer, BlockButton>();
private Image imgBackground = null;
private JPanel pLandscape = new JPanel(), pBlocks = new JPanel(), pSelectableBlocks = new JPanel(), pSettings = new JPanel();
private JLabel lbSelected = new JLabel();
private JButton btSave, btLoad, btQuit, btAddRow;
private JScrollBar scrollBar = new JScrollBar(JScrollBar.HORIZONTAL,1,1,1,SCROLLBAR_START_MAX);
private LevelLoaderInterface levelLoader = null;
private MainMenuGUI main = null;
private EditorGUI instance = null;
private JScrollPane scrollPane = new JScrollPane(pSelectableBlocks,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
private JFileChooser fileChooser = null;
private int size, selected, columns, rows, start, globalIndex = -1, indexCounter = INDEX_START, xStart, yStart, xEnd, yEnd, btWidth, btHeight;

	public EditorGUI(MainMenuGUI pMain, LevelLoaderInterface pLoader, int pWidth, int pLength){
		size = pWidth / pLength;
		main = pMain;
		instance = this;
		levelLoader = pLoader;
		
		setLayout(new BorderLayout());
		add(BorderLayout.SOUTH,pBlocks);
		add(BorderLayout.CENTER,pLandscape);
		pLandscape.setOpaque(false);
		setOpaque(false);
		
		selected = BlockInterface.TYP_GRAS;
		imgBackground = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/editorbg.png"));
		blocks.put(BlockInterface.TYP_ENEMY, new BlockButton(BlockInterface.TYP_ENEMY, false));
		blocks.get(BlockInterface.TYP_ENEMY).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/enemy.gif"))));
		blocks.put(BlockInterface.TYP_GRAS, new BlockButton(BlockInterface.TYP_GRAS, false));
		blocks.get(BlockInterface.TYP_GRAS).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gras.jpg"))));
		blocks.put(BlockInterface.TYP_WATER, new BlockButton(BlockInterface.TYP_WATER, false));
		blocks.get(BlockInterface.TYP_WATER).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/water.png"))));
		blocks.put(BlockInterface.TYP_COIN, new BlockButton(BlockInterface.TYP_COIN, false));
		blocks.get(BlockInterface.TYP_COIN).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/coin.png"))));
		blocks.put(BlockInterface.TYP_GOAL, new BlockButton(BlockInterface.TYP_GOAL, false));
		blocks.get(BlockInterface.TYP_GOAL).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/goal.png"))));
		blocks.put(BlockInterface.TYP_CRATE, new BlockButton(BlockInterface.TYP_CRATE, false));
		blocks.get(BlockInterface.TYP_CRATE).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/crate.png"))));
		blocks.put(BlockInterface.TYP_BUTTON, new BlockButton(BlockInterface.TYP_BUTTON, false));
		blocks.get(BlockInterface.TYP_BUTTON).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/buttonReleased.png"))));
		blocks.put(BlockInterface.TYP_GATE, new BlockButton(BlockInterface.TYP_GATE, false));
		blocks.get(BlockInterface.TYP_GATE).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/gateClosed.png"))));
		blocks.put(BlockInterface.TYP_AIR, new BlockButton(BlockInterface.TYP_AIR, false));
		blocks.get(BlockInterface.TYP_AIR).setIcon(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/delete.png"))));
		btSave = new JButton(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/save.png"))));
		btLoad = new JButton(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/load.png"))));
		btQuit = new JButton(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/quit.png"))));
		btAddRow = new JButton(new ScaledImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("images/add.png"))));
		
		
		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				BlockButton button = (BlockButton)e.getSource();
				selected = button.getType();
				lbSelected.setIcon(button.getIcon());
			}
		};

		Dimension dim = new Dimension(size, size);
		for(Map.Entry<Integer,BlockButton> entry:blocks.entrySet()){
			entry.getValue().setPreferredSize(dim);
			((ScaledImageIcon)entry.getValue().getIcon()).setSize(dim);
			entry.getValue().addActionListener(listener);
			pSelectableBlocks.add(entry.getValue());
		}

		lbSelected.setPreferredSize(dim);
		lbSelected.setIcon(blocks.get(BlockInterface.TYP_GRAS).getIcon());
		dim = new Dimension(size/2, size/2);
		btSave.addActionListener(this);
		btSave.setPreferredSize(dim);
		((ScaledImageIcon)btSave.getIcon()).setSize(dim);
		btLoad.addActionListener(this);
		btLoad.setPreferredSize(dim);
		((ScaledImageIcon)btLoad.getIcon()).setSize(dim);
		btQuit.addActionListener(this);
		btQuit.setPreferredSize(dim);
		((ScaledImageIcon)btQuit.getIcon()).setSize(dim);
		btAddRow.addActionListener(this);
		btAddRow.setPreferredSize(dim);
		((ScaledImageIcon)btAddRow.getIcon()).setSize(dim);
		
		pSettings.setLayout(new GridLayout(2, 2));
		pSettings.add(btSave);
		pSettings.add(btLoad);
		pSettings.add(btQuit);
		pSettings.add(btAddRow);
		scrollBar.addAdjustmentListener(this);
		
		pBlocks.setLayout(new BorderLayout());
		pBlocks.add(BorderLayout.CENTER,scrollPane);
		pBlocks.add(BorderLayout.WEST,lbSelected);
		pBlocks.add(BorderLayout.EAST,pSettings);
		pBlocks.add(BorderLayout.NORTH,scrollBar);
			
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
		indexCounter = INDEX_START;
		rows = pLandscape.getHeight()/size+2;
		columns = pLandscape.getWidth()/size+1;
		start = 0;
		scrollBar.setValue(1);
		scrollBar.setMaximum(SCROLLBAR_START_MAX);
		objects.clear();
		
		pLandscape.setLayout(new GridLayout(rows, columns));
		for(int i=0; i<columns+2; i++){
			addEmptyRow();
		}
		loadLandscape(objects);
		btWidth = objects.get(0)[0].getWidth();
		btHeight = objects.get(0)[0].getHeight();
		validate();
	}
	
	/**
	 * Adds an empty Row to the landscape.
	 */
	private void addEmptyRow(){
		BlockButton buttonBlocks[] = new BlockButton[rows];
		for(int j=0; j<rows; j++){
			buttonBlocks[j] = new BlockButton(BlockInterface.TYP_AIR,true);
			buttonBlocks[j].setPreferredSize(new Dimension(size, size));
			buttonBlocks[j].addActionListener(this);
			buttonBlocks[j].setContentAreaFilled(false);
			buttonBlocks[j].setOpaque(false);
		}
		objects.add(buttonBlocks);
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
	private boolean loadMap(){
		objects.clear();
		start = 0;
		indexCounter = INDEX_START;
		fileChooser.showOpenDialog(this);
		File file = fileChooser.getSelectedFile();
		if(file != null){
			levelLoader.setInputFile(file);
			int buffer[] = null;
			objects.clear();
			while((buffer = levelLoader.readNext()) != null){
				LinkedList<BlockButton> buttons = new LinkedList<BlockButton>();
				for(int i=0; i<buffer.length; i++){
					buttons.add(new BlockButton(buffer[i],true));
					buttons.getLast().setPreferredSize(new Dimension(size, size));
					buttons.getLast().addActionListener(this);
					buttons.getLast().setContentAreaFilled(false);
					buttons.getLast().setOpaque(false);
					if(buffer[i] != BlockInterface.TYP_AIR){
						ScaledImageIcon icon = new ScaledImageIcon((ImageIcon)blocks.get(buffer[i]).getIcon(), btWidth, btHeight);
						buttons.getLast().setIcon(icon);
					}
					
					if(buffer[i] == BlockInterface.TYP_BUTTON || buffer[i] == BlockInterface.TYP_GATE){
						if(buffer[i] == BlockInterface.TYP_BUTTON && buffer[i+1] > indexCounter){
							indexCounter = buffer[i+1];
						}
						buttons.getLast().setIndex(buffer[++i]);
					}
				}
				objects.add(buttons.toArray(new BlockButton[buttons.size()]));
			}
			scrollBar.setMaximum(objects.size()-columns);
			scrollBar.setValue(scrollBar.getMaximum());
			return true;
		}
		return false;
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
			file = new File(file.getAbsoluteFile()+".lvl");
		}
		levelLoader.setOutputFile(file);
		for(int i=0; i<objects.size(); i++){
			BlockButton buttonBlocks[] = objects.get(i);
			LinkedList<Integer> line = new LinkedList<Integer>();
			for(int j=0; j<buttonBlocks.length; j++){
				line.add(buttonBlocks[j].getType());
				if(buttonBlocks[j].getIndex() != -1){
					line.add(buttonBlocks[j].getIndex());
				}
			}
			levelLoader.writeNext(line.toArray(new Integer[line.size()]));
		}
		levelLoader.closeStreams();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btSave){
			saveMap();
		}else if(e.getSource() == btLoad){
			if(loadMap()){
				start = 0;
				loadLandscape(objects.subList(start, start+columns));
			}
		}else if(e.getSource() == btQuit){
			main.reset();
		}else if(e.getSource() == btAddRow){
			addEmptyRow();
			scrollBar.setMaximum(objects.size()-columns);
		}else{
			BlockButton button = (BlockButton)e.getSource();
			button.setType(selected);
			if(selected == BlockInterface.TYP_AIR) {
				button.setIcon(null);
			}else{
				ScaledImageIcon icon = new ScaledImageIcon((ImageIcon)blocks.get(selected).getIcon(), button.getWidth(), button.getHeight());
				button.setIcon(icon);
				if(selected == BlockInterface.TYP_BUTTON){
					button.setIndex(++indexCounter);
				}
			}
		}
	}
	
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		start = scrollBar.getValue()-1;
		loadLandscape(objects.subList(start, start+columns));
	}
	
	@Override
	public void paint(Graphics g){
		g.drawImage(imgBackground, pLandscape.getX(), pLandscape.getY(), pLandscape.getWidth(), pLandscape.getHeight(), this);
		super.paint(g);

		if(globalIndex != -1 && xEnd != -1){
			Graphics2D graphics = (Graphics2D)g;
			graphics.setColor(Color.GREEN);
			graphics.setStroke(new BasicStroke(STROKE_SIZE));
			graphics.drawLine(pLandscape.getX()+xStart, pLandscape.getY()+yStart, pLandscape.getX()+xEnd, pLandscape.getY()+yEnd);
		}
	}
	
	/**
	 * Button for EditorGUI
	 * @author bjeschle,toofterd
	 * @version 1.0
	 */
	private class BlockButton extends JButton implements MouseListener{
		private int blockType, index;
		
		public BlockButton(int pBlockType, boolean landscape){
			super();
			blockType = pBlockType;
			index = -1;
			if(landscape){
				addMouseListener(this);
			}
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
			index = -1;
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
				g.setColor(Color.GREEN);
				g.fillOval(0, 0, OVAL_SIZE, OVAL_SIZE);
				g.setColor(Color.BLACK);
				g.drawString(String.valueOf(index-INDEX_START), INDEX_X, INDEX_Y);
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if(arg0.getButton() == MouseEvent.BUTTON3){
				if(blockType == BlockInterface.TYP_BUTTON){
					globalIndex = getIndex();
					xStart = getX()+getWidth()/2;
					yStart = getY()+getHeight()-STROKE_SIZE/2;
				}else if(blockType == BlockInterface.TYP_GATE  && globalIndex != -1){
					setIndex(globalIndex);
				}else{
					globalIndex = -1;
				}
			}else{
				globalIndex = -1;
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			if(blockType == BlockInterface.TYP_GATE  && globalIndex != -1){
				xEnd = getX()+getWidth()/2;
				yEnd = getY()+getHeight()/2;
			}else{
				xEnd = -1;
			}
			instance.repaint();
		}
		
		@Override
		public void mouseExited(MouseEvent arg0) {}
		@Override
		public void mousePressed(MouseEvent arg0) {}
		@Override
		public void mouseReleased(MouseEvent arg0) {}
	}
	
	private class ScaledImageIcon extends ImageIcon{
		private Image image = null;
		private int width, height;
		
		public ScaledImageIcon(Image pImage){
			super(pImage);
			image = pImage;
			width = 0;
			height = 0;
		}
		
		public ScaledImageIcon(ImageIcon pImage, int pWidth, int pHeight){
			image = pImage.getImage();
			width = pWidth;
			height = pHeight;
		}
		
		public void setSize(Dimension dim){
			width = (int) dim.getWidth();
			height = (int) dim.getHeight();
		}
		
		@Override
		public void paintIcon(Component c, Graphics g, int pX, int pY){
			g.drawImage(image, 0, 0, width, height, c);
		}
	}
}
