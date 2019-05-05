// Import packages
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.Scanner;
import org.math.plot.*;
import org.math.plot.plotObjects.*;
import java.util.Arrays;

public class IVTheoretical
{
	//Tunnel current constants
	public static double vP = 0.05;		//Peak voltage
	public static double iP = 4.;		//Peak current density

	//Excess current constants
	public static double vV = 0.37;		//Valley voltage
	public static double iV = 0.0037;	//Valley current density
	public static double ecp = 30;		// Excess current prefactor

	//Thermal current constants
	public static double scd = 1e-16;	//Saturation current density
	public static double q = 1.602e-19;	//Electron charge
	public static double k = 1.38e-23;	//Boltzmann's constant
	public static double t = 150;		//Tempeprature

	public static void main(String[] args)
	{
		boolean checkPlotIV = true;

		// Open file and store data (I-V graphs for individual currents and total current)
		PrintWriter outputFile = null;
  	  	try
  	  	{
  	  	  outputFile = new PrintWriter(new FileOutputStream("iTot-V.txt",false));
  	  	}
  	  	catch(FileNotFoundException e)
  	  	{
  	  	  System.out.println("File error.  Program aborted.");
  	  	  System.exit(0);
  	  	}
		
  	  	// Set max index
  	  	double dv = 0.002;	// Time Step
  	  	double vMax = 0.55;	// Final time
  	  	int iMax = (int)((vMax/dv));	

		// Declare arrays
		double[] v = new double[iMax];		// Voltage
		double[] iTD = new double[iMax];	// Total current

		// Set initial conditions
		v[0] = 0;
		iTD[0] = curretnTD(v[0]); // Current across tunnel diode
	
		// Theoreitcal values
		for(int i=1;i<(iMax);i++)
		{
			v[i] = v[i-1] + 0.002;

			iTD[i] = curretnTD(v[i]);
		}

		// Create live plot
		if (checkPlotIV)
			plotIV(v, iTD);

		//Storing info in a file to use later if needed
		for(int i=0;i<iMax;i++)
		{
			outputFile.println(v[i] + "	" + iTD[i]);
		}	

		// Close files
		outputFile.close();
	} // main

		public static double curretnTD (double v)
	{
		double currentTotal = v * iP * Math.pow(Math.E, (1-v/vP))/vP + iV * Math.pow(Math.E, (ecp*(v-vV))) + scd*Math.pow(Math.E, (v*q/k/t)-1);

		return currentTotal;
	} // currentTD

	public static void plotIV(double[] volts, double[] iTOT)
	{
		Plot2DPanel plot = new Plot2DPanel();

		// Add line plots
		plot.addLinePlot("Total current", volts, iTOT);

		// Axes
		plot.setAxisLabel(0, "Volts");
		plot.getAxis(0).setLabelPosition(0.5, -0.1);
		plot.setAxisLabel(1, "Current");

		// Title
		BaseLabel title = new BaseLabel ("Total Current vs Voltage", Color.BLACK, 0.5, 1.1);
		title.setFont(new Font("Courier", Font.BOLD, 14));
		plot.addPlotable(title);

		// Plot panel
		JFrame frame = new JFrame("I-V Curve.java");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	
	} // plotIV
} // termProject