 /**
Term Project by Oliver Miller

Tunnel Diode (negative resistance) simulation:

*/

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

		// Open file and store data (I-V graphs for individual currents and total current)
		PrintWriter outputFile1 = null;
  	  	try
  	  	{
  	  	  outputFile1 = new PrintWriter(new FileOutputStream("iTot-V.txt",false));
  	  	}
  	  	catch(FileNotFoundException e)
  	  	{
  	  	  System.out.println("File error.  Program aborted.");
  	  	  System.exit(0);
  	  	}

  	  	PrintWriter outputFile2 = null;
  	  	try
  	  	{
  	  	  outputFile2 = new PrintWriter(new FileOutputStream("iTN-V.txt",false));
  	  	}
  	  	catch(FileNotFoundException e)
  	  	{
  	  	  System.out.println("File error.  Program aborted.");
  	  	  System.exit(0);
  	  	}

  	  	PrintWriter outputFile3 = null;
  	  	try
  	  	{
  	  	  outputFile3 = new PrintWriter(new FileOutputStream("iXS-V.txt",false));
  	  	}
  	  	catch(FileNotFoundException e)
  	  	{
  	  	  System.out.println("File error.  Program aborted.");
  	  	  System.exit(0);
  	  	}

  	  	PrintWriter outputFile4 = null;
  	  	try
  	  	{
  	  	  outputFile4 = new PrintWriter(new FileOutputStream("iTH-V.txt",false));
  	  	}
  	  	catch(FileNotFoundException e)
  	  	{
  	  	  System.out.println("File error.  Program aborted.");
  	  	  System.exit(0);
  	  	}
		
  	  	// Set max index
  	  	int iMax = (int)(0.55/0.002);	

		// Declare arrays
		double[] v = new double[iMax];	// Voltage
		double[] iTot = new double[iMax];	// Total current
		double[] iTN = new double[iMax];	// Tunnel current
		double[] iXS = new double[iMax];	// Excess current
		double[] iTH = new double[iMax];	// Thermal current

		// Set initial conditions
		v[0] = 0;
		iTot[0] = 0;
		iTN[0] = calcIt(v[0]);
		iXS[0] = calcIx(v[0]);
		iTH[0] = calcIth(v[0]);
		
		// Calculate values
		for(int i=1;i<iMax;i++)
		{
			v[i] = v[i-1] + 0.002;

			iTN[i] = calcIt(v[i]);
			iXS[i] = calcIx(v[i]);
			iTH[i] = calcIth(v[i]);

			iTot[i] = calcItot(v[i], iTN[i], iXS[i], iTH[i]);
		}

		// Create live plot
		if (checkPlotIV)
			plotIV(v, iTot, iTN, iXS, iTH);

		//Storing info in a file to use later if needed
		for(int i=0;i<iMax;i++)
		{
			outputFile1.println(v[i] + "	" + iTot[i]);
		}

		for(int i=0;i<iMax;i++)
		{
			outputFile2.println(v[i] + "	" + iTN[i]);
		}

		for(int i=0;i<iMax;i++)
		{
			outputFile3.println(v[i] + "	" + iXS[i]);
		}

		for(int i=0;i<iMax;i++)
		{
			outputFile4.println(v[i] + "	" + iTH[i]);
		}	

		// Close files
		outputFile1.close();
		outputFile2.close();
		outputFile3.close();
		outputFile4.close();

	
	} // main

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
		double t = 300;

		double iTH = scd * Math.pow(Math.E, ((v*q)/(k*t))-1);

		return iTH;
	} // calcIth

	public static double calcItot (double v, double iTN, double iXS, double iTH)
	{
		double vP = 0.05;

		double currentTotal = (v*iTN/vP) + iXS + iTH;

		return currentTotal;
	} // calcItot

	public static void plotIV(double[] volts, double[] iTOT, double[] iTN, double[] iXS, double[] iTH)
	{
		Plot2DPanel plot = new Plot2DPanel();

		// Legend position
		plot.addLegend("SOUTH");

		// Add line plots
		plot.addLinePlot("Total current", volts, iTOT);
		plot.addLinePlot("Tunnel current", volts, iTN);
		plot.addLinePlot("Excess current", volts, iXS);
		plot.addLinePlot("Thermal current", volts, iTH);

		// Axes
		plot.setAxisLabel(0, "Volts (millivolts)");
		plot.getAxis(0).setLabelPosition(0.5, -0.1);
		plot.setAxisLabel(1, "Current (milliamps)");

		// Title
		BaseLabel title = new BaseLabel ("I-V Graph", Color.BLACK, 0.5, 1.1);
		title.setFont(new Font("Courier", Font.BOLD, 14));
		plot.addPlotable(title);

		// Plot panel
		JFrame frame = new JFrame("I-V Curves.java");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	
	} // plotGraph
} // termProject