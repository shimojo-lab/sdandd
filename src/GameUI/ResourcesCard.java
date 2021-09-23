package GameUI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import Entity.ItemCard;
import Entity.Room;


public class ResourcesCard extends ItemCard 
{


	

	public ResourcesCard(Room aRoom, int x,int y, String cardId, String decriptionFunction,int digitalPoint, String pathImgBg, String pathImgForeground)
	{

		super(aRoom,  x,  y, cardId,decriptionFunction,digitalPoint,pathImgBg,pathImgForeground);
		


	}




}

