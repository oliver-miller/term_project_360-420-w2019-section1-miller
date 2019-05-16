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
	// Values of circuit elements
	public static double r2 = 80;		// Ohms
	public static double r1 = 24;		// Ohms
	public static double l = 0.7;		// Henry
	public static double dt = 5e-6;		// Time step
	public static double tTotal = 5;	// Total time
	// Max index
	public static int iMax = (int)(tTotal/dt);

	public static void main(String[] args)
	{

		// Max index for all 3 loops
		int loops;
		int maxIndex = iMax + 1;
		// Arrays
		double[] t = new double[maxIndex];
		double[] i1 = new double[maxIndex];
		double[] i2 = new double[maxIndex];
		double[] itot = new double[maxIndex];
		double[] vTD = new double[maxIndex];
		
		// Initialize
		t[0] = 0;
		vTD[0] = 0;
		i1[0] = 0;
		i2[0] = 0;

			// Outermost loop is for 1 period of oscillation loop
			for(int i=1;i<iMax;i++)
			{
				// Initialize values for each iteration
				boolean check = false;
				double tempi2 = 0;
				double tempi1 = 0;
				double tempvTD = 0.1;
				t[i] = t[i-1] + dt;

				while(!(check))
				{
					// Assign values for this iteration
					vTD[i] = tempvTD;
					i1[i] = current1(vTD[i], i1[i-1]);
					i2[i] = current2(vTD[i], i2[i-1], i1[i], i1[i-1]);
					itot[i] = i1[i] + i2[i];

					// Loop through tunnel diode i(v) function to find corresponding voltage for a given i2
					while(!(check))
					{
						tempvTD += 0.001;
						tempi2 = currentTD(tempvTD);

						// Test for convergence of current through tunnel diode by comparing estimated and calculated i2
						if(Math.abs(tempi2-i2[i]) < 0.1)
						{
							check = true;
						}
					}
					// Re-assign boolean for outer while loop
					check = false;

					// Calculate temporary values for current given the estimated voltage
					double checki1 = current1(tempvTD, i1[i-1]);
					double checki2 = current2(tempvTD, i2[i-1], checki1, i1[i-1]);
					double checkitot = checki1 + checki2;

					// Test for convergence of voltage across tunnel diode by comparing estimated and calculated itot
					// This value should be constant based on Kirchoff's current law
					if(Math.abs(checkitot - itot[i]) < 0.000001)
					{
						vTD[i] = tempvTD;
						check = true;
					}
				System.out.println(i);
				}
			}
			// Reset loop
			t[iMax] = t[iMax-1] + 0.01;
			vTD[iMax] = 0;
		
		store(vTD, t);
		plot(vTD, t);
	}

	public static double current1 (double v, double previouscurrent) // Derived with Kirchoff's voltage law
	{
		double current = previouscurrent + (r1*v*dt)/(r1*r2 + r1*l + r2*l);
		return current;
	}//current1

	public static double current2 (double v, double previouscurrent, double currentone, double previouscurrentone) // Derived with Kirchoff's voltage law
	{
		double current = previouscurrent + (r2*(currentone-previouscurrentone)/l) - v*dt;
		return current;
	}//current2

	public static double currentTD (double v)	// Already known (derived from SPICE model)
	{
		//Tunnel current constants
		double vP = 0.05;		//Peak voltage
		double iP = 4.;			//Peak current density

		//Excess current constants
		double vV = 0.37;		//Valley voltage
		double iV = 0.0037;		//Valley current density
		double ecp = 30;		// Excess current prefactor

		//Thermal current constants
		double scd = 1e-16;		//Saturation current density
		double q = 1.602e-19;	//Electron charge
		double k = 1.38e-23;	//Boltzmann's constant
		double t = 150;			//Tempeprature

		double current = v * iP * Math.pow(Math.E, (1-v/vP))/vP + iV * Math.pow(Math.E, (ecp*(v-vV))) + scd*Math.pow(Math.E, (v*q/k/t)-1);
		return current;
	}//currentTD

	public static void store(double[] v, double[] t)
	{
		// Open file and store data (I-V graphs for individual currents and total current)
		PrintWriter outputFile = null;
  	  	try
  	  	{
  	  	  outputFile = new PrintWriter(new FileOutputStream("vTD-time",false));
  	  	}
  	  	catch(FileNotFoundException e)
  	  	{
  	  	  System.out.println("File error.  Program aborted.");
  	  	  System.exit(0);
  	  	}

  	  	//Storing info in a file to use later if needed
		for(int i=0;i<iMax;i++)
		{
			outputFile.println(t[i] + "	" + v[i]);
		}	

		// Close files
		outputFile.close();
	}//store

	public static void plot(double[] volts, double[] time)
	{
		Plot2DPanel plot = new Plot2DPanel();

		// Add line plots
		plot.addLinePlot("V-vs-t", time, volts);

		// Axes
		plot.setAxisLabel(0, "Time (s)");
		plot.getAxis(0).setLabelPosition(0.5, -0.1);
		plot.setAxisLabel(1, "Volts (mv)");

		// Title
		BaseLabel title = new BaseLabel ("Voltage across the tunnel diode over time", Color.BLACK, 0.5, 1.1);
		title.setFont(new Font("Courier", Font.BOLD, 14));
		plot.addPlotable(title);

		// Plot panel
		JFrame frame = new JFrame("V-vs-t of tunnel diode");
		frame.setSize(600, 600);
		frame.setContentPane(plot);
		frame.setVisible(true);
	
	} //plot
}