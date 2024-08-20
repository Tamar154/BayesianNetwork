# Bayesian Network

## Overview

This project implements probabilistic inference in Bayesian networks using the **Bayes Ball** and **Variable Elimination** algorithms.

## Project Structure

- **`Ex1.java`**: Main class to execute the Bayesian network inference.
- **`BayesianNetwork.java`**: Class representing the Bayesian network, including XML parsing and network construction.
- **`BayesBall.java`**: Implementation of the Bayes Ball algorithm.
- **`Factor.java`**: Class representing factors in the Variable Elimination algorithm.
- **`Variable.java`**: Class representing variables in the Bayesian network, including their CPTs and relationships.
- **`VariableElimination.java`**: Implementation of the Variable Elimination algorithm.
- **`CPT.java`**: Class representing the Conditional Probability Table for variables.
- **`InputParser.java`**: Class for parsing input files and initializing the Bayesian network.


## Input Format
* **Bayes Ball Query:** A-B|E1=e1,E2=e2,...,Ek=ek
* **Variable Elimination Query:** P(Q=q|E1=e1, E2=e2, ..., Ek=ek) H1-H2-...-Hj

## Output Format
* **Bayes Ball:** yes if independent, no otherwise.
* **Variable Elimination:** Result, #Additions, #Multiplications

## How to Run

1. Compile the Java file:
   ```bash
   javac Ex1.java
   ```
2. Run the program:
   ```bash
   java Ex1
   ```
