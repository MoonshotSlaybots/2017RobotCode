package org.usfirst.frc.team6164.robot.commands;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Command;

public class ShootCommand extends Command {
	
	private Victor shooter;
	
	public ShootCommand(Victor shooter) {
		this.shooter = shooter;
	}

	@Override
	protected void initialize() {
		if (this.shooter == null) { // This should never happen but just a precaution
			this.shooter = new Victor(0);
		}
	}
	
	@Override
	protected void execute() {
		this.shooter.setSpeed(-0.9D);
	}
	
	@Override
	protected boolean isFinished() {
		return true;
	}
	
	@Override
	protected void end() {
		this.shooter.setSpeed(0D);
	}
}
