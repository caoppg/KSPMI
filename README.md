# KSPMI

A Knowledge-based System for Predictive Maintenance in Industry 4.0 (KSPMI). The work is funded by the European Interreg HALFBACK Project (http://halfback.in.hs-furtwangen.de/home/).

## Getting Started

To run this software, the following steps should be followed:

* Download the project as a zip file.
* Unzip the file.
* Import the unzipped folder as a maven project, into a Java integrated development environment (IDE), such as Eclipse, NetBeans, IntelliJ IDEA, or BlueJ.
* Navigate to the "SoftwareAPP" class under the "halfback.prototype" package
* Run with command line arguments. The arguments should follow a format such as: -p -if D:\XX\XX\XX\KSPMI-master\data\dataset -of D:\XX\XX\XX\KSPMI-master\result.txt -m 0.8. In the arguments, local dictionary after "-if" indicates the path of the input dataset, and the path after "-of" should be the local dictionary of the project. The last float number "0.8" gives a pre-defined support value for the chronicle mining algorithm. The support value is recommended to be set above 0.7 to avoid the OutOfMemoryError exception in Java.

### Prerequisites

The following APIs and libraries should be installed before running the software:

* [The OWL API](http://owlapi.sourceforge.net/) - Creating, manipulating and serialising OWL Ontologies.

* [The SWRL API](https://github.com/protegeproject/swrlapi) - Working with the OWL-based SWRL rule and SQWRL query languages.

* [SPMF: Source code version](https://github.com/jacksonpradolima/SPMF) - Running sequential pattern mining algorithms from source code.

## Running the tests

Some input data sets are in the "data" folder under the project root dictionary. They can be passed to the command line arguments for running the software. For example, to run on the SECOM data set with support value of 0.853653: -p -if D:\XX\XX\XX\KSPMI-master\data\SECOM.csv -of D:\XX\XX\XX\KSPMI-master\result.txt -m 0.853653

## Built With

* [Maven](https://maven.apache.org/) - Dependency Management
* [Protégé](https://protege.stanford.edu/) - Ontology editor and framework for building intelligent systems
* [The OWL API](http://owlapi.sourceforge.net/) - An API for OWL 2 and an efficient in-memory reference implementation.
* [The SWRL API](https://github.com/protegeproject/swrlapi) - A Java API for working with the OWL-based SWRL rule and SQWRL query languages.
* [SPMF](https://www.philippe-fournier-viger.com/spmf/index.php?link=download.php) - A Java Open-Source Data Mining Library.
* [Weka](https://www.cs.waikato.ac.nz/ml/weka/) - An open source machine learning software.

## Authors

* **Qiushi Cao** - *Ph.D. student at INSA Rouen Normandie* 
* Email: qiushi.cao09@gmail.com

## License
This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments
This work has received funding from INTERREG Upper Rhine (European Regional Development Fund) and the
Ministries for Research of Baden-Wrttemberg, Rheinland-Pfalz (Germany) and from the Grand Est French Region
in the framework of the Science Offensive Upper Rhine HALFBACK project.
