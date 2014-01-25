/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com.frc.rmr;


import edu.wpi.first.wpilibj.SimpleRobot;
import edu.wpi.first.wpilibj.Watchdog;
import edu.wpi.first.wpilibj.Joystick;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SimpleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class MainRobot extends SimpleRobot {
	public static final int JOY_PORT = 3;
	public static final double WATCHDOG_EXPIRATION = .5;
	Watchdog watchdog;
	Joystick joy;
	Vision vision;
	Drive drive;
	Shooter shooter;
	protected void robotInit() {
		watchdog = Watchdog.getInstance();
		watchdog.setExpiration(WATCHDOG_EXPIRATION);
		joy = new Joystick(JOY_PORT);
		vision = new Vision(joy);
		drive = new Drive(watchdog, joy);
		shooter = new Shooter(watchdog, joy, vision);
	}
	/**
	* This function is called once each time the robot enters autonomous mode.
	*/
	public void autonomous() {
		// Distance array might not be necessary
		int[] distances = new int[2];
		boolean isHot = vision.isHot(distances);
		watchdog.feed();
		if (isHot) {
			shooter.autoShoot(distances[0], distances[1]);
			drive.autoDrive();
		}
		else {
			// autoDrive should take 5 seconds
			// shooter should take as little time as possible
			drive.autoDrive();
			shooter.autoShoot(distances[0], distances[1]);
		}
	}

	/**
	* This function is called once each time the robot enters operator control.
	*/
	public void operatorControl() {
		while (isEnabled() && isOperatorControl()) {
			drive.run();
			shooter.run();
			vision.run();
			watchdog.feed();
		}
	}
}
