# KSPMI

A Knowledge-based System for Predictive Maintenance in Industry 4.0 (KSPMI). The work is funded by the European Interreg HALFBACK Project (http://halfback.in.hs-furtwangen.de/home/). A demonstration video can be found at: https://sites.google.com/view/qiushi-phd-thesis/home.

## Getting started

To run this software, the following steps should be followed:

* Download the project as a zip file.
* Unzip the file.
* Import the unzipped folder as a maven project, into a Java integrated development environment (IDE), such as Eclipse, NetBeans, IntelliJ IDEA, or BlueJ.
* Navigate to the "SoftwareAPP" class under the "halfback.prototype" package
* Run with command line arguments. The arguments should follow a format such as: -p -if D:\XX\XX\XX\KSPMI-master\data\dataset -of D:\XX\XX\XX\KSPMI-master\result.txt -m 0.8. In the arguments, local dictionary after "-if" indicates the path of the input dataset, and the path after "-of" should be the root dictionary of the project. The last float number "0.8" gives a pre-defined support value for the chronicle mining algorithm. The support value is recommended to be set above 0.7 to avoid the OutOfMemoryError exception in Java.

### Prerequisites

The following APIs and libraries should be installed before running the software:

* [Java WindowBuilder Pro](https://www.eclipse.org/windowbuilder/download.php) - Developing and building Java graphical user interfaces.

* [The OWL API](http://owlapi.sourceforge.net/) - Creating, manipulating and serialising OWL Ontologies.

* [The SWRL API](https://github.com/protegeproject/swrlapi) - Working with the OWL-based SWRL rule and SQWRL query languages.

* [The SQWRL API](https://github.com/protegeproject/swrlapi/wiki/SQWRL) - A SWRL-based query language providing SQL-like operators for extracting information from OWL ontologies.

## Running the tests

Some input data sets are in the "data" folder under the project root dictionary. They can be passed to the command line arguments for running the software. For example, to run on the SECOM data set with support value of 0.853653: -p -if D:\XX\XX\XX\KSPMI-master\data\SECOM.csv -of D:\XX\XX\XX\KSPMI-master\result.txt -m 0.853653.

The [SECOM data set](https://archive.ics.uci.edu/ml/datasets/SECOM) contains measurements of features of semi-conductor productions within a semi-conductor manufacturing process. In the SECOM data set, 1567 recordings and 590 attributes are collected, with each recording being characterized by a time stamp referring to the time that the data is recorded. Each recording is also associated with a label, which is either 1 or -1. The label of every recording explains the correctness of the event, with -1 corresponding to a non-failure event, and 1 refers to a failure. Timestamps are associated with all the records indicating the moment of each specific test point. In total, 104 pieces of records represent the failures of production. The data is stored in a raw text file, within which each line represents an individual example of recording with its timestamp. The features are separated by spaces.

## GUIs

![Alt text](https://github.com/caoppg/KSPMI/blob/master/Screenshots/chroniclemininginterface.PNG?raw=true "Optional Title")

## Built with

* [Maven](https://maven.apache.org/) - Dependency Management
* [Protégé](https://protege.stanford.edu/) - An ontology editor and framework for building intelligent systems
* [The OWL API](http://owlapi.sourceforge.net/) - An API for OWL 2 and an efficient in-memory reference implementation.
* [The SWRL API](https://github.com/protegeproject/swrlapi) - A Java API for working with the OWL-based SWRL rule and SQWRL query languages.
* [The SQWRL API](https://github.com/protegeproject/swrlapi/wiki/SQWRL) - A SWRL-based query language providing SQL-like operators for extracting information from OWL ontologies.
* [SPMF](https://www.philippe-fournier-viger.com/spmf/index.php?link=download.php) - A Java Open-Source Data Mining Library.
* [Weka](https://www.cs.waikato.ac.nz/ml/weka/) - An open source machine learning software.

## Author

* **Qiushi Cao** - *Ph.D. student at INSA Rouen Normandie* 
* Email: qiushi.cao09@gmail.com

## License
This project is licensed under the MIT License - see the [License.md](License) file for details

## Acknowledgments
This work has received funding from INTERREG Upper Rhine (European Regional Development Fund) and the
Ministries for Research of Baden-Württemberg, Rheinland-Pfalz (Germany) and from the Grand Est French Region
in the framework of the Science Offensive Upper Rhine HALFBACK project.
