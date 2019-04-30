// Import packages
import java.io.*;
import java.awt.*;
import javax.swing.*;
import java.util.Scanner;
import org.math.plot.*;
import org.math.plot.plotObjects.*;
import java.util.Arrays;

public class termProject
{
	public static void main(String[] args)
	{
		boolean checkPlotIV = true;

		// Open file and store data for I-V graphs
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
		double[] iTot = new double[iMax];	// Total current

		// Set initial conditions
		v[0] = 0;
		iTot[0] = calcItot(v[0]);
	
		// Theoreitcal values
		for(int i=1;i<(iMax);i++)
		{
			v[i] = v[i-1] + 0.002;

			iTot[i] = calcItot(v[i]);
		}

		// Create live plot
		if (checkPlotIV)
			plotIV(v, iTot);

		//Storing info in a file to use later if needed
		for(int i=0;i<iMax;i++)
		{
			outputFile.println(v[i] + "	" + iTot[i]);
		}	

		// Close files
		outputFile.close();
	} // main

		public static double calcItot (double v)
	{
		double vP = 0.05;

		double currentTotal = (v*calcIt(v)/vP) + calcIx(v) + calcIth(v);

		return currentTotal;
	} // calcItot

	public static double calcIt (double v)
	{
		double vP = 0.05;
		double iP = 4.;

		double iT = iP * Math.pow(Math.E, (1-(v/vP)));

		return iT;
	} // calcIt

	public static double calcIx (double v)
	{
		double vV = 0.37;
		double iV = 0.0037;
		double ecp = 30;

		double iX = iV * Math.pow(Math.E, (ecp*(v-vV)));

		return iX;
	} // calcIx

	public static double calcIth (double v)
	{
		double scd = 1e-16;
		double q = 1.602e-19;
		double k = 1.38e-23;
		double t = 150;

		double iTH = scd * Math.pow(Math.E, ((v*q)/(k*t))-1);

		return iTH;
	} // calcIth

	public static void plotIV(double[] volts, double[] iTOT)
	{
		Plot2DPanel plot = new Plot2DPanel();

		// Add line plots
		plot.addLinePlot("Total current", volts, iTOT);

		// Axes
		plot.setAxisLabel(0, "Volts (millivolts)");
		plot.getAxis(0).setLabelPosition(0.5, -0.1);
		plot.setAxisLabel(1, "Current (milliamps)");

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