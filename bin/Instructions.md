# Code
### By Oliver Miller

Place the code you developped for the term project in this folder. Add any instructions/documentation required to run the code, and what results can be expected in this *README* file.

## Instructions
How to reproduce my results:
- For just the I-V Characteristic of a tunnel diode, run the IVTheoretical.java file
    - This uses a formula determined with the SPICE method
    - Running this code will plot an I-V graph, and will store the data in a file called "iTot-V.txt"
- For the voltage across the tunnel diode in a relaxation oscillator, run the termProject.java file (it will take a while to run since it has to iterate through nearly 1 million data points, but this is only to create very precise results)
    - This solves a set of ODEs (derived using Kirchoff's laws and the SPICE model equations for current across a tunnel diode) and plots the voltage vs time graph of the tunnel diode
        - It will plot one period of oscillation of the circuit
        - The data to plot this graph is also stored in the file "vTD-time" if needed
    
* Copies of the text files filled with the data are already in the data folder, but other copies will also be created and placed in the bin folder when either of the .java files are run
