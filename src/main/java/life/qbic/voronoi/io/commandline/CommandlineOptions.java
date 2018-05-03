package life.qbic.voronoi.io.commandline;

import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;

public class CommandlineOptions {

    @Option(names = {"-f", "--file", "-i", "--input"}, description = "csv or tsv file to generate a Treemap from", required = true)
    private String inFile;

    @Option(names = {"-o", "--outputFile"}, description = "outputFile file path for the generated treemap html file - default: /tmp/VoroTreemap.html")
    private String outputFile = "/tmp/VoroTreemap.html";

    @Option(names = {"-c", "--col"}, description = "names of the columns in the provided file", arity = "1..*", required = true)
    private List<String> columnNames = new ArrayList<>();

    @Option(names = {"-t", "temporary"}, description = "save result in /tmp and delete afterwards, @|fg(red) only|@ use this with GUI versions!")
    private boolean temporaryHtml = false;

    @Option(names = {"-h", "--help"}, description = "display a help message", usageHelp = true)
    private boolean helpRequested;

    public String getInFile() {
        return inFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public boolean isTemporaryHtml() {
        return temporaryHtml;
    }

    public boolean isHelpRequested() {
        return helpRequested;
    }
}
