
package org.usfirst.frc.team6164.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.usfirst.frc.team6164.robot.commands.ExampleCommand;
import org.usfirst.frc.team6164.robot.subsystems.ExampleSubsystem;
import com.ctre.CANTalon;
import com.analog.adis16448.frc.ADIS16448_IMU;
import edu.wpi.first.wpilibj.CameraServer;


public class Robot extends IterativeRobot {
	
	public static final ExampleSubsystem exampleSubsystem = new ExampleSubsystem();
	public static OI oi;
	//drive motor controllers 
	CANTalon BL = new CANTalon(1);
	CANTalon BR= new CANTalon(2);
	CANTalon FR= new CANTalon(3);
	CANTalon FL= new CANTalon(4);
	RobotDrive drive = new RobotDrive(FL,BL,FR,BR); //2 motor driver
	//---------------------------------------------------------------------------------
	//other
	Victor shooter = new Victor(0);
	Victor climber = new Victor(1);
	Joystick stick = new Joystick(0);
	//ADIS16448_IMU imu = new ADIS16448_IMU();
	double heading;
	boolean setHeading = true;
	int auto = 0;
	Command autonomusCommand;
	SendableChooser autoChooser;
	
	Command autonomousCommand;
	SendableChooser<Command> chooser = new SendableChooser<>();

	@Override
	public void robotInit() {
		oi = new OI();
		chooser.addDefault("Default Auto", new ExampleCommand());
		SmartDashboard.putData("Auto mode", chooser);
		FL.setVoltageRampRate(50);
		FR.setVoltageRampRate(50);
		FR.setInverted(true);
		BL.setVoltageRampRate(50);
		//BL.setInverted(true);
		BR.setVoltageRampRate(50);
		BR.setInverted(true);
		CameraServer.getInstance().startAutomaticCapture("cam0", 0);
		CameraServer.getInstance().startAutomaticCapture("cam1", 1);
		autoChooser = new SendableChooser();
		autoChooser.addDefault("Default Program", new doNothing());
		autoChooser.addObject("Start Mid", new startMid());
		autoChooser.addObject("Start Left", new startLeft());
		autoChooser.addObject("Start Right", new startRight());
				
	}

	@Override
	public void disabledInit() {
		
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		if (autonomousCommand != null)
			autonomousCommand.start();
		auto = 0;
	}
	
	@Override
	public void autonomousPeriodic() {
	/*	Scheduler.getInstance().run();
		
		if (auto<= 150){
			drive.mecanumDrive_Cartesian(0, -.5, 0, 0);
		}
		else{
			drive.mecanumDrive_Cartesian(0, 0, 0, 0);
		}
	*/
	 //DEMO AUTO
	 //**********************************************************
		if (auto<= 150){
			drive.mecanumDrive_Cartesian(0, 0,.5, 0);	
			
		}
		else if (auto>= 150 && auto<= 300){
			drive.mecanumDrive_Cartesian(0, 0, -.5, 0);
		}
		else if(auto>= 300 && auto<= 450){
			drive.mecanumDrive_Cartesian(0, 0, 0, 0);
			climber.setSpeed(-1);
		}
		else if (auto>= 450 && auto<= 600){
			climber.setSpeed(0);
			shooter.setSpeed(.6);
		}
		else{
			shooter.setSpeed(0);
		}
		auto++;
	}

	@Override
	public void teleopInit() {
		if (autonomousCommand != null)
			autonomousCommand.cancel();	
		
		
		drive.setSafetyEnabled(false);
	}
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		drive.mecanumDrive_Cartesian(stick.getX(), stick.getY(), stick.getRawAxis(4), 0); //imu.getAngle()-heading	
		
		if (setHeading){
		//	heading = imu.getAngle();
			setHeading = false;
		}

		if (stick.getRawButton(2)) {
			shooter.setSpeed(.9);
			}
			else {
				shooter.setSpeed(0);
			}
		
		if(stick.getRawButton(1)){
			climber.setSpeed(-1);
			}
			else{
				climber.setSpeed(0);
			}
    	
		Timer.delay(.01);
		
	}
	
	@Override
	public void testPeriodic() {
		LiveWindow.run();
		System.out.println(SmartDashboard.getData("Auto Mode"));
	}
}
