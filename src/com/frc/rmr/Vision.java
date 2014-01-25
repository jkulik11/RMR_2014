/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.rmr;

import com.sun.squawk.util.StringTokenizer;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 * @author jackson
 */

// Small class to classify the points
class Point {
    // X, Y. So complex
    int x, y;
    
    // Constructor. Oh so complex.
    public Point(int x, int y)
    {
	this.x = x;
	this.y = y;
    }
    
    // toString method, only used in testing. 
    public String toString()
    {
	return "(" + x + ", " + y + ")";
    }
    
} // End class Point

public class Vision {
    
    final int MIN_CORNERS_FOR_HOT = 6;
    final int MAX_DISTANCE = 7;
    final int MIN_DISTANCE = 5;
    
    final double GOAL_WIDTH = 29.0;
    final double SCREEN_RES_WIDTH = 640.0;
    final double CAM_VIEW_TANGENT = 0.434812375;
    
    Point [] points;
    
    NetworkTable server;
    
    public Vision(Joystick joy) {
	server = NetworkTable.getTable("rmr662");
    }
    
    // the distance array will have the first coordinate as the horizontal distance and the vertical distance as the second coordinate
    // just put the values into this array once the values are determined.
    // Not currently needed
    public boolean isHot(int[] distances) {
        
	// Instanciates the points
	getPoints();
	
	// Returns true if there are MIN_CORNERS_FOR_HOT or greater points in the array
	return points.length >= MIN_CORNERS_FOR_HOT;
	
    } // End method isHot
    
    // update the display with whatever we need it to say, do not update the watchdog
    public void run() {
	
	try {
	    
	    // Instanciates all the points
	    getPoints();
		
	    // Outputs to the smart dashboard whether or not the goal is seen
	    SmartDashboard.putBoolean("Target Seen", isHot(null));
		
	    // Sorts the points for later on
	    sortPoints();
		
	    // tan(47 / 2) = 0.434812375
	    // Inaccurate at back of room-13in.
		
	    // tan (43 / 2) = 0.3939104756
	    // Inaccurate at front of the room-7in.
		
	    // Calculates the horizontal distance to the goal
	    double distance = ((GOAL_WIDTH / 2 * SCREEN_RES_WIDTH / (points[points.length-1].x - points[0].x) ) / CAM_VIEW_TANGENT);
		
	    // Outputs that to the smart dashboard
	    SmartDashboard.putBoolean("In Range", distance >= MIN_DISTANCE && distance <= MAX_DISTANCE);
	    
	}
	catch (Exception e)
	{
	    e.printStackTrace();
	}
	
    } // End run method
    
    // Sorts the array of points using a bubbleSort method.
    public void sortPoints()
    {
	
	boolean exchangeMade = true;
	
	while (exchangeMade)
	{
	    exchangeMade = false;
	    
	    for (int i = 0; i < points.length - 1; i++)
	    {
		if (points[i].x > points[i + 1].x || (points[i].x == points[i + 1].x && points[i].y > points[i + 1].y) )
		{
		    swap(points, i, i + 1);
		    exchangeMade = true;
		}
	    }
	    
	}
	
    } // End method sortPoints
    
    public void swap(Point [] p, int from, int to)
    {
	
	Point temp = p[from];
	p[from] = p[to];
	p[to] = temp;
	
    } // End swap method
    
    public void getPoints()
    {
	String numbers="";
	/*
	Manipulater team eited the code to work with smartdashboard.
	We also have the corners saved as a string. 
	The string above this comment an dthe stuff below were added/adjusted by us.
	*/
	//gets value from RoboRealm as String
	numbers=server.getValue("RING_CORNER").toString();
		
	//creates corner array list
	int [] corner;
	
	//String Tokenizer used to take string to int array
	StringTokenizer token = new StringTokenizer(numbers, ", ");
		
	corner = new int[token.countTokens()];
	int count = 0;
	while (token.hasMoreTokens()){
		    
	    corner[count] = Integer.parseInt(token.nextToken());
	    count++;
	}
		
	// Array of points
	points = new Point[corner.length / 2];
		
	// Creates the point array and prints it to standard output.
	// Tested and works
	for (int i = 0; i < points.length; i++)
	{
	    points[i] = new Point(corner[i * 2], corner[i * 2 + 1]);
	}
	
    } // End method getPoints
    
} // End class Vision