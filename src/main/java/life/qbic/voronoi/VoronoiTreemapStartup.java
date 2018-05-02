package life.qbic.voronoi;

import java.io.*;

import life.qbic.voronoi.algorithms.TreemapCreator;
import life.qbic.voronoi.io.commandline.CommandLineParser;
import life.qbic.voronoi.io.commandline.CommandlineOptions;

public class VoronoiTreemapStartup {

    private static String outputFilePath;

    /**
     * reads a csv or tsv file and creates a voronoi treemap from the given data
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        createTreemap(args);
    }

    public static void createTreemap(String[] parameters) throws IOException {
        CommandlineOptions commandlineOptions = CommandLineParser.parseCommandlineParameters(parameters);
        outputFilePath = commandlineOptions.getOutputFile();

        String writtenHTMLFilePath = TreemapCreator.startTreemapCreation(commandlineOptions.getInFile(),
                commandlineOptions.getOutputFile(),
                commandlineOptions.isTemporaryHtml(),
                commandlineOptions.getColumnNames());

        setOutputFilePath(writtenHTMLFilePath);
    }

    // required for anything GUI related!
    public static String getOutputFilePath() {
        return outputFilePath;
    }

    public static void setOutputFilePath(String outputFilePath) {
        VoronoiTreemapStartup.outputFilePath = outputFilePath;
    }
}
