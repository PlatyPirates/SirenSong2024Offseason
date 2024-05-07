package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.RobotController;
import frc.robot.PlatyPiratesLib.Logging.*;

public class Robot extends TimedRobot {
    private Command m_autonomousCommand;

    private ReadableLog log = new ReadableLog();

    @Override
    public void robotInit() {
        log.print("~~~~ ROBOT INIT STARTING ~~~~");
        try{
            setUpLoggingTriggers();
        } catch(Throwable t){
            log.print("Error in Robot/robotInit(): " + t.getMessage());
            throw t;
        }
        log.print("~~~~ ROBOT INIT FINISHED ~~~~");
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledInit() {
        log.print("----Disabled Init----");
    }

    @Override
    public void disabledPeriodic() {}

    @Override
    public void disabledExit() {
        log.print("----Disabled Exit----");
    }

    @Override
    public void autonomousInit() {
        log.print("----Autonomous Init----");
        log.print("Starting match/auto test with initial battery voltage of " + RobotController.getBatteryVoltage() + "V.");
    }

    @Override
    public void autonomousPeriodic() {}

    @Override
    public void autonomousExit() {
        log.print("----Autonomous Exit----");
    }

    @Override
    public void teleopInit() {
        log.print("----Teleop Init----");
        if (m_autonomousCommand != null) {
            m_autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopPeriodic() {}

    @Override
    public void teleopExit() {
        log.print("----Teleop Exit----");
    }

    @Override
    public void testInit() {
        log.print("----Test Init----");
        CommandScheduler.getInstance().cancelAll();
    }

    @Override
    public void testPeriodic() {}

    @Override
    public void testExit() {
        log.print("----Test Exit----");
    }

    public Command log(String input){
        return new RunCommand(() -> {
            log.print(input);
        });
    }

    public ReadableLog getLog(){
        return log;
    }

    public void setUpLoggingTriggers(){
        Trigger brownOut = new Trigger(() -> RobotController.isBrownedOut());
        brownOut.onTrue(log("!!! WARNING: BROWNOUT !!!"));

        Trigger outputDrop = new Trigger(() -> (RobotController.getBatteryVoltage() <= 6.8));
        outputDrop.onTrue(log("WARNING: Battery voltage has dropped to " + RobotController.getBatteryVoltage() + "V, so 6V outputs have dropped!"));

        Trigger outputsDisabled = new Trigger(() -> (RobotController.getBatteryVoltage() <= 6.3));
        outputsDisabled.onTrue(log("! WARNING: Battery voltage is very low (" + RobotController.getBatteryVoltage() + "V), and the robot has entered brownout protection mode. Most outputs have been disabled!"));

        Trigger blackOut = new Trigger(() -> (RobotController.getBatteryVoltage() <= 4.5));
        blackOut.onTrue(log("!!! WARNING: DEVICE BLACKOUT RANGE! The battery voltage has dropped extremely low (" + RobotController.getBatteryVoltage() + "V), and device blackout may occur! !!!"));
    }
}
