public class termProject
{
	public static void main(String[] args)
	{
		boolean runTDTheo = true;
		// Plot graphic for typical tunnel diode I-V Characterisitc
		// Created using pre-determined formulas developped with the SPICE method
		
		// Set class for oscillator
		RelaxationOscillator oscillator = new RelaxationOscillator();

		// Set max index
		double dt = 5e-6;		// Time step (s)
		double tTotal = 200e-3;	// Total time (s)
		int iMax = (int)(tTotal/dt);

		// Declare arrays
		double[] t = new double[iMax];		// Time
		double[] v = new double[iMax];		// Voltage
		double[] iTD = new double[iMax];	// Total current

		//Set initial conditions
		t[0] = 0;

		for(int i=1;i<iMax;i++)
		{
			t[i] = t[i-1] + dt;
			v[i] = oscillator.calcV();
		}
		
		// Plot Tunnel Diode I-V characterisitc
		if (runTDTheo)
		{	
			IVTheoretical theoreticalTD = new IVTheoretical();
			theoreticalTD.main();
		}
	}
}