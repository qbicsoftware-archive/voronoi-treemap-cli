package life.qbic.voronoi;

import java.io.*;

import life.qbic.voronoi.algorithms.TreemapCreator;
import life.qbic.voronoi.io.commandline.CommandLineParser;
import life.qbic.voronoi.io.commandline.CommandlineOptions;

//TODO add logging, maybe new template?
public class VoronoiTreemapStartup {

    /**
     * reads a csv or tsv file and creates a voronoi treemap from the given data
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        CommandlineOptions commandlineOptions = CommandLineParser.parseCommandlineParameters(args);

        TreemapCreator.startTreemapCreation(commandlineOptions.getInFile(),
                                            commandlineOptions.getOutputFile(),
                                            commandlineOptions.isTemporaryHtml(),
                                            commandlineOptions.getColumnNames());
    }

}
