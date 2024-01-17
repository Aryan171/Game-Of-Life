/*press esc to escape
 r to generate a random image and
 use left and right buttons to traverse through different rules
 press space bar to run the simulation
 press enter to go through the frames one by one*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class gameoflife extends JPanel implements KeyListener{

private static final long serialVersionUID = 1L;
private Image image;
private int width = Toolkit.getDefaultToolkit().getScreenSize().width,
			height = Toolkit.getDefaultToolkit().getScreenSize().height,
			rule = 18, 
			an/*alive neighbor*/;
private boolean first = true, play = false;
private boolean[][] imgArray;

public static void main(String[] args) {
	new gameoflife();
}
	
public gameoflife() {
	//setting up the window
	JFrame frame = new JFrame("Cellular autmata");
	
	frame.addKeyListener(this);
	frame.add(this);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setSize(this.width, this.height);
	frame.setUndecorated(true);
	frame.setVisible(true);
	frame.setFocusable(true);
	super.setBackground(Color.BLACK);
}
	
@Override
public void paintComponent(Graphics g) {
	if (this.first) {	
		// initializing the width and height for later use
		this.width = this.getWidth(); 
		this.height = this.getHeight();
		// making an 2d array which represents a ruled image
		boolean[] boolRule = ToBoolArray(this.rule); 
		GenerateRuledImageArray(boolRule);
		// initializing the image and drawing the data in image array on the image
		this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);  
		ToImage();
		
		this.first = false;
	}
	
	g.drawImage(image, 0, 0, this); // displaying the image
	
	UpdateImageArray(); // updating the current image array
	ToImage();	
	
	if(this.play)
		this.repaint();
}

private boolean[] ToBoolArray(int rule) {
	boolean[] result = new boolean[8];
	
	for (int i = 0; i < 8; i++) {
		if (rule >= 1)
			if (rule % 2 == 0) {
				result[i] = false;
				rule = rule / 2;
			}
			
			else {
				result[i] = true;
				rule = (rule - 1) / 2;
			}
		else
			result[i] = false;
	}
	
	return result;
}

private void UpdateImageArray() {
	boolean[][] ImgArray = new boolean[this.width][this.height];
	
	for (int y = 0; y < this.height; y++)
		for (int x = 0; x < this.width; x++) {
			this.an = 0;
			UpdateANCounter(x - 1, y - 1);
			UpdateANCounter(x - 1, y);
			UpdateANCounter(x - 1, y + 1);
			UpdateANCounter(x, y - 1);
			UpdateANCounter(x, y + 1);
			UpdateANCounter(x + 1, y - 1);
			UpdateANCounter(x + 1, y);
			UpdateANCounter(x + 1, y + 1);
			
			if (this.an != 3 && this.an != 2)
				ImgArray[x][y] = false;
			
			else 
				if(this.an == 3)
				ImgArray[x][y] = true;
				
				else
					ImgArray[x][y] = this.imgArray[x][y];
		}
	this.imgArray = ImgArray;
}

private void GenerateRuledImageArray(boolean[] boolRule) {
	boolean a = false, b = false, c = false;
	this.imgArray = new boolean[this.width][this.height];
	this.imgArray[this.width / 2][0] = true;
	for (int y = 1; y < height; y++)
		for (int x = 0; x < width; x++) {
			a = GetState(x - 1, y - 1);
			b = GetState(x, y - 1);
			c = GetState(x + 1, y - 1);
			
			if (!a && !b && !c)
				this.imgArray[x][y] = boolRule[0];
			
			else if (!a && !b && c)
				this.imgArray[x][y] = boolRule[1];
			
			else if (!a && b && !c)
				this.imgArray[x][y] = boolRule[2];
			
			else if (!a && b && c)
				this.imgArray[x][y] = boolRule[3];
			
			else if (a && !b && !c)
				this.imgArray[x][y] = boolRule[4];
			
			else if (a && !b && c)
				this.imgArray[x][y] = boolRule[5];
			
			else if (a && b && !c)
				this.imgArray[x][y] = boolRule[6];
			
			else if (a && b && c)
				this.imgArray[x][y] = boolRule[7];
		}
}

private boolean GetState(int x, int y) {
	if (!(x < 0 || x > this.width - 1 || y < 0 || y > this.height - 1) && this.imgArray[x][y]) 
		return true;
	return false;
}

private void UpdateANCounter(int x, int y) {
	if (!(x < 0 || x > this.width - 1 || y < 0 || y > this.height - 1) && this.imgArray[x][y]) 
		this.an += 1;
}

private void ToImage() {
	Graphics buffer = image.getGraphics();
	
	buffer.clearRect(0, 0, this.width, this.height);

		for (int ya = 0; ya < this.height; ya++)
			for (int xa = 0; xa < this.width; xa++)
				if(this.imgArray[xa][ya]) 
					buffer.drawLine(xa, ya, xa, ya);
	
	buffer.dispose();
}

private void GenerateRandomImageArray(double chance) {
	Random random = new Random();

	this.imgArray = new boolean[this.width][this.height];
	
	for (int y = 0; y < height; y++)
		for (int x = 0; x < width; x++) 
			this.imgArray[x][y] = random.nextDouble() * 100.0 < chance ? true : false;
}

@Override
public void keyReleased(KeyEvent e) 
{
	int keyCode = e.getExtendedKeyCode();
	
	if (keyCode == 10) // 10 is the ExtendedKeyCode of  enter key
		this.repaint();
		
	else if (keyCode == 27) // 27 is the ExtendedKeyCode of escape key 
		System.exit(0);
	
	else if (keyCode == 32) {// 32 is the ExtendedKeyCode of space bar
		this.play = this.play ? false : true;
		if (this.play)
			this.repaint();
	}
	
	else if (keyCode == 39) {// 39 is the ExtendedKeyCode of right key
		this.play = false;
		this.rule = this.rule == 255 ? 0 : this.rule + 1; 
		GenerateRuledImageArray(ToBoolArray(rule));
		ToImage();
		this.repaint();
	}
	
	else if (keyCode == 37) {// 37 is the ExtendedKeyCode of left key
		this.play = false;
		this.rule = this.rule == 0 ? 255 : this.rule - 1;
		GenerateRuledImageArray(ToBoolArray(rule));
		ToImage();
		this.repaint();
	}
	
	else if (keyCode == 82) {// 82 is the ExtendedKeyCode of R key
		this.play = false;
		GenerateRandomImageArray(50.0);
		ToImage();
		this.repaint();
	}
}

@Override
public void keyTyped(KeyEvent e) {}

@Override
public void keyPressed(KeyEvent e) {}
}
