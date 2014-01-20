


/*/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.frc.rmr;

import edu.wpi.first.wpilibj.DigitalSource;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Encoder;

/*
 *
 * @author jackson
 */

public class Drive {
    //jaguar input from -1 to 1
    private static final double MAX_SPEED = .4;
    private static final double WHEEL_RADIUS = 3.0;
    private static final int TICKS_PER_ROTATION = 360;
    
    private static final double KP = .3;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = .3;
    
    private static final int RIGHT_MOTOR_PORT = 2;
    private static final int[] RIGHT_ENCODER_CHANNELS = {1, 2};
    private static final int LEFT_MOTOR_PORT = 1;
    private static final int[] LEFT_ENCODER_CHANNELS = {3, 4};
    
    private static final double WHEEL_SIZE = 2*WHEEL_RADIUS*Math.PI;
    private static final double DISTANCE_PER_PULSE = WHEEL_SIZE / TICKS_PER_ROTATION;
    
    private double pTuning = 0;
    private double iTuning = 0;
    private double dTuning = 0;
    private double fTuning = 0;
    private Joystick joy;
    private Watchdog watch;
    private PIDController leftController, rightController;
    private boolean[] buttonWasPressed;
    private Jaguar rightMotor;
    private Jaguar leftMotor;
    
    public Drive(Watchdog watch, Joystick joy) {
	this.watch = watch;
	
	this.joy = joy;
	buttonWasPressed = new boolean[10];
	for (int i = 0; i < 10; ++i) { buttonWasPressed[i] = false; }
	
	// create the right pid motor and controller
	rightMotor = new Jaguar(RIGHT_MOTOR_PORT);
	Encoder rightEncoder = new Encoder(RIGHT_ENCODER_CHANNELS[0], RIGHT_ENCODER_CHANNELS[1]);
	rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
	rightEncoder.start();
	rightEncoder.reset();
	// changing this and leftEncoder's setPIDSource will make it run on teleop or auto
	rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
	
	rightController = new PIDController(KP, KI, KD, KF, rightEncoder, rightMotor);
	rightController.enable();
	rightController.setInputRange(-MAX_SPEED, MAX_SPEED);
	rightController.setOutputRange(-MAX_SPEED, MAX_SPEED);
	
	// create the left pid motor and controller
	leftMotor = new Jaguar(LEFT_MOTOR_PORT);
	Encoder leftEncoder = new Encoder(LEFT_ENCODER_CHANNELS[0], LEFT_ENCODER_CHANNELS[1]);
	leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
	leftEncoder.start();
	leftEncoder.reset();
	// changing this and rightEncoder's setPIDSource will make it run on teleop or auto
	leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kRate);
	
	leftController = new PIDController(KP, KI, KD, KF, leftEncoder, leftMotor);
	leftController.enable();
	leftController.setInputRange(-MAX_SPEED, MAX_SPEED);
	leftController.setOutputRange(-MAX_SPEED, MAX_SPEED);
    }
    // you must check in with the watchdog during your operations in auto mode

    public void autoDrive() {
	/*
	// create the right pid motor and controller
	Jaguar rightMotor = new Jaguar(RIGHT_MOTOR_PORT);
	Encoder rightEncoder = new Encoder(RIGHT_ENCODER_CHANNELS[0], RIGHT_ENCODER_CHANNELS[1]);
	
	rightEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
	rightEncoder.start();
	rightEncoder.reset();
	rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
	PIDController rightController = new PIDController(KP, KI, KD, rightEncoder, rightMotor);
	rightController.enable();
	
	// create the left pid motor and controller
	Jaguar leftMotor = new Jaguar(LEFT_MOTOR_PORT);
	Encoder leftEncoder = new Encoder(LEFT_ENCODER_CHANNELS[0], LEFT_ENCODER_CHANNELS[1]);
	leftEncoder.setDistancePerPulse(DISTANCE_PER_PULSE);
	leftEncoder.start();
	leftEncoder.reset();
	leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
	PIDController leftController = new PIDController(KP, KI, KD, leftEncoder, leftMotor);
	leftController.enable();
	
	// TODO: missing actual distance code and control
	
	// TODO: remove
*/
	
	// leftController.setSetpoint(1000);
	// rightController.setSetpoint(1000);
	
	watch.feed();
    }
    
    private boolean getButtonPressed(int btn)
    {
	if (joy.getRawButton(btn))
	{
	    if (!buttonWasPressed[btn - 1])
	    {
		buttonWasPressed[btn - 1] = true;
		return true;
	    }
	}
	else
	    buttonWasPressed[btn - 1] = false;
	return false;
    }
    
    // called repeatedly, don't call the watchdog
    public void run() {
	
	if (Math.abs(joy.getRawAxis(XboxMap.LeftJoyVert)) >= .05) {
	    leftController.setSetpoint(.5); //joy.getRawAxis(XboxMap.LeftJoyVert));
	    rightController.setSetpoint(.5);  //joy.getRawAxis(XboxMap.LeftJoyVert));
	} else {
	    leftController.setSetpoint(0);
	    rightController.setSetpoint(0);
	}
	
	double tuningSize = .1;
	
	double tuningAmount = tuningSize;
	
	// clear if the left joystick is clicked down
	if(getButtonPressed(XboxMap.LJC)) {
	    pTuning = 0;
	    iTuning = 0;
	    dTuning = 0;
	    fTuning = 0;
	}
	
	// if the right joystick is clicked then it's tuned down
	if(joy.getRawButton(XboxMap.RJC)) {
	    tuningAmount = -tuningSize;
	} else {
	    tuningAmount = tuningSize;
	}
	
	// check all the buttons
	if(getButtonPressed(XboxMap.A)) {
	    pTuning += tuningAmount;
	    double pValue = pTuning + KP;
	    System.out.println("P " + pValue);
	}
	if(getButtonPressed(XboxMap.B)) {
	    iTuning += tuningAmount;
	    double iValue = iTuning + KI;
	    System.out.println("I " + iValue);
	}
	if(getButtonPressed(XboxMap.Y)) {
	    dTuning += tuningAmount;
	    double dValue = dTuning + KD;
	    System.out.println("D " + dValue);
	}
	if(getButtonPressed(XboxMap.X)) {
	    fTuning += tuningAmount;
	    double fValue = fTuning + KF;
	    System.out.println("F " + fValue);
	}
	
	
	leftController.setPID(KP+pTuning, KI+iTuning, KD+dTuning, KF+fTuning);
	rightController.setPID(KP+pTuning, KI+iTuning, KD+dTuning, KF+fTuning);
	
	// TODO: missing input from Joystick
	
	// TODO: tuning from the controller
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/*
package com.frc.rmr;

import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.Encoder;

/**
 *
 * @author jackson
public class Drive {
    
	public static final double DISTANCE_PP = 6.0 * Math.PI / 360.0;
	public static final int JAGL = 1;
	public static final int JAGR = 2;
	public static final int ENL1 = 3;
	public static final int ENL2 = 4;
	public static final int ENR1 = 1;
	public static final int ENR2 = 2; 
	
	private double p = .1;
	private double i = 0;
	private double d = 0;
	private PIDController pidL, pidR;
	private Jaguar leftD, rightD;
	private Encoder leftE, rightE;
	private Joystick joy;
	private Watchdog watch;

	public Drive(Watchdog watch, Joystick joy) {
		this.watch = watch;
		this.joy = joy;
		leftD = new Jaguar(JAGL);
		rightD = new Jaguar(JAGR);
		leftE = new Encoder(ENL1, ENL2);
		rightE = new Encoder(ENR1, ENR2);
		leftE.setDistancePerPulse(DISTANCE_PP);
		rightE.setDistancePerPulse(DISTANCE_PP);
		leftE.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
		rightE.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
		pidL = new PIDController(p, i, d, leftE, leftD);
		pidL.setPercentTolerance(.02);
		pidL.setContinuous(true);
		pidR = new PIDController(p, i, d, rightE, rightD);
		pidR.setPercentTolerance(.02);
		pidR.setContinuous(true);
	}
	// you must check in with the watchdog during your operations in auto mode
	/*
	public void autoDrive() {
		System.out.println("entered");
		   /*
		pidL.enable();
		pidR.enable();
		pidL.setSetpoint(100.0);
		pidR.setSetpoint(100.0);
			   
		rightD.set(.3);
		leftD.set(.3);
		while (true) {
		    watch.feed();
		}
	}
	// called repeatedly, don't call the watchdog
	public void run() {

	}
}
*/
