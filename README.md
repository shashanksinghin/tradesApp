Write a Java program using springboot and streaming tool, SQL/NoSQL DB along with all the JUNIT cases. TDD approach will be preferred. Please provide a pipeline to deploy the application preferably using github actions if not then jenkins. The pipeline must have automation regression, OSS vulnerability check (If critical or blocker found then fail the build).
Problem Statement
There is a scenario where thousands of trades are flowing into one store, assume any way of transmission of trades. We need to create a one trade store, which stores the trade in the following order

**Trade Id**	**Version**	**Counter-Party Id**	**Book-Id**	**Maturity Date**	**Created Date**	**Expired** <br>
T1			&emsp;&emsp; 1		&emsp;&emsp;CP-1				&emsp;&emsp;B1		&emsp;&emsp;20/05/2020		&emsp;&emsp;<today date>	&emsp;&emsp;N<br>
T2			&emsp;&emsp; 2		&emsp;&emsp;CP-2				&emsp;&emsp;B1		&emsp;&emsp;20/05/2021		&emsp;&emsp;<today date>	&emsp;&emsp;N<br>
T2			&emsp;&emsp; 1		&emsp;&emsp;CP-1				&emsp;&emsp;B1		&emsp;&emsp;20/05/2021		&emsp;&emsp;14/03/2015		&emsp;&emsp;N<br>
T3			&emsp;&emsp; 3		&emsp;&emsp;CP-3				&emsp;&emsp;B2		&emsp;&emsp;20/05/2014		&emsp;&emsp;<today date>	&emsp;&emsp;Y<br>

There are couples of validation, we need to provide in the above assignment
1.	During transmission if the lower version is being received by the store it will reject the trade and throw an exception. If the version is same it will override the existing record.
2.	Store should not allow the trade which has less maturity date then today date.
3.	Store should automatically update the expire flag if in a store the trade crosses the maturity date.

