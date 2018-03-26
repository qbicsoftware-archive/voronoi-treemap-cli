package io.commandline;

public class HelpOption {

    public HelpOption() {

    }

    public static void printHelpOption() {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("Welcome to the Voronoi treemaps CLI component!" + System.lineSeparator());
        stringBuilder.append("The following options are mandatory:" + System.lineSeparator());
        stringBuilder.append("-f or --file : csv or tsv file path" + System.lineSeparator());
        stringBuilder.append("-s or --size : size of the root polygon - default: 800" + System.lineSeparator());
        stringBuilder.append("-c or --colNames  : column names" + System.lineSeparator());
        stringBuilder.append("The following options are optional:" + System.lineSeparator());
        stringBuilder.append("-h or --help : shows help" + System.lineSeparator());
        stringBuilder.append("-v or --version : curent version" + System.lineSeparator());

        System.out.println(stringBuilder.toString());
    }
}
