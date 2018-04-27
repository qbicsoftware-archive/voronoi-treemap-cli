package io.commandline;

import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.List;

public class CommandlineOptions {

    @Option(names = {"-f", "--file", "-i", "--input"}, description = "csv or tsv file to generate a Treemap from", required = true)
    private String infile;

    @Option(names = {"-o", "--output"}, description = "output file path for the generated treemap html file - default: /tmp/VoroTreemap.html")
    private String output = "/tmp/VoroTreemap.html";

    @Option(names = {"-c", "--col"}, description = "names of the columns in the provided file", arity = "1..*")
    private List<String> columnNames = new ArrayList<>();

    @Option(names = {"-h", "--help"}, description = "display a help message", usageHelp = true)
    private boolean helpRequested;

    public String getInfile() {
        return infile;
    }

    public String getOutput() {
        return output;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public boolean isHelpRequested() {
        return helpRequested;
    }
}
