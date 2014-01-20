/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.frc.drive;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDController;
/**
 *
 * @author ROBOTics
 */


/*
public class MainDrive {

private Watchdog watch;
private Joystick joy;
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
	rightEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
	
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
	leftEncoder.setPIDSourceParameter(Encoder.PIDSourceParameter.kDistance);
	
	leftController = new PIDController(KP, KI, KD, KF, leftEncoder, leftMotor);
	leftController.enable();
	leftController.setInputRange(-MAX_SPEED, MAX_SPEED);
	leftController.setOutputRange(-MAX_SPEED, MAX_SPEED);
}
*/