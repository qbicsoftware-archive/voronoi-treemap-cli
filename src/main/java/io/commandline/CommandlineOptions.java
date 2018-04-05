package io.commandline;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandlineOptions {

    @Parameter(names = {"-f", "-file"}, description = "csv or tsv file to generate a Treemap from", required = true)
    private String infile;

    @Parameter(names = {"-s", "-size"}, description = "size of the root polygon - default: 800")
    private int size = 800;

    @Parameter(names = {"-h", "-help"}, description = "show all possible commands with an explanation")
    private boolean help = false;

    @Parameter(names = {"-c", "-col"}, description = "names of the columns in the provided file")
    private List<String> columnNames = new ArrayList<>();

    @Parameter(names = {"-v", "-version"}, description = "current version")
    private boolean version = false;

    public int getSize() {
        return size;
    }

    public String getInfile() {
        return infile;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public boolean isHelp() {
        return help;
    }

    public boolean isVersion() {
        return version;
    }

}
