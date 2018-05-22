voronoi-treemap-cli
======
This is part of the voronoi visualisation of hierarchical data. See here for the visualisation: [Voronoi-treemaps-portlet](https://github.com/qbicsoftware/voronoi-treemaps-GUI).

This tool creates a voronoi treemap from data provided by a csv or tsv file and writes it as an html file. This html file can then be viewed in a browser. 

Quick Setup for developers
=====
1. <code>git clone https://github.com/qbicsoftware/voronoi-treemaps-CLI</code>
2. Import the project as a maven project by importing the pom.xml file
3. Allow maven to download all required dependencies and run the project using the VoronoiTreemapStartup class with appropriate runtime parameters -> see Usage below

Usage
=====
Download the jar: [Voronoi-treemaps-cli releases](https://github.com/qbicsoftware/voronoi-treemaps-CLI/releases). 

The example files are provided in the folder [examples](https://github.com/qbicsoftware/voronoi-treemap-cli/tree/development/examples). 

## The CLI - Command Line Interface
```bash
> java -jar voronoi-treemaps-cli -h
usage: voronoi-treemaps-tsv-creator.jar -f <inputfile> -c <columnlist> -o <outputpath> [-t] [-h]
 -h, --help               displays a help menu
 -f, --file       <arg>   tsv-file for treemap creation
 -c, --col        <args>  list of columns for which a treemap should be created 
 -o, --outputpath <arg>   output path for the final treemap
 -t,                      saves the generated html file in /tmp, but cleans it up when the JVM stops!
                          Only use this in conjunction with a portlet/GUI version
```
  
Examples  
=====
Using the example file 'a_24_cancer_pathway_2136_elements.tsv' in the examples folder: [examples](https://github.com/qbicsoftware/voronoi-treemap-cli/tree/development/examples)

Take a look at the supplemented 'cw_indices' file in order to find out which columns of interest should be visualised.

Now we can run the project using our shell of choice:

<code>java -jar voronoi-treemaps-cli-1.0.13-SNAPSHOT.jar -f /inputpath/to/a_24_cancer_pathway_2136_elements.tsv -c title pathway_id ID X0h_dnE47dox0h_vs_dnE47noDox0hlog2FC X0h_dnE47dox0h_vs_RFPdox0hlog2FC X0h_dnE47noDox0h_vs_RFPnoDox0hlog2FC -o /outputpath/to/fancyVoronoiTreemap.html</code>

This will result in a html file called 'fancyVoronoiTreemap.html' in /outputpath/to. This file can now be opened using your modern browser of choice.

Authors
=====
The original work was done by Matthias Raffeiner. It is now maintained by Lukas Heumos. 
