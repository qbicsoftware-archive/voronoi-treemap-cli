package life.qbic.voronoi;

import java.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import life.qbic.voronoi.algorithms.TreemapCreator;
import life.qbic.voronoi.io.commandline.CommandLineParser;
import life.qbic.voronoi.io.commandline.CommandlineOptions;
import picocli.CommandLine;

public class VoronoiTreemapStartup {

    private static final Logger LOG = LogManager.getLogger(VoronoiTreemapStartup.class);

    private static String outputFilePath;

    /**
     * reads a csv or tsv file and creates a voronoi treemap from the given data
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        try {
            createTreemap(args);
        } catch (CommandLine.PicocliException e) {
            LOG.error("Error parsing commandline arguments: " + e.getMessage());
        }
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

    // required for anything GUI/porlet related!
    public static String getOutputFilePath() {
        return outputFilePath;
    }

    public static void setOutputFilePath(String outputFilePath) {
        VoronoiTreemapStartup.outputFilePath = outputFilePath;
    }
}
