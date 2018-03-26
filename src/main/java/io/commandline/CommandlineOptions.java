package io.commandline;

import com.beust.jcommander.Parameter;

import java.util.ArrayList;
import java.util.List;

public class CommandlineOptions {

    @Parameter
    private List<String> parameters = new ArrayList<>();

    @Parameter(names = {"-f, --file"}, description = "csv or tsv file to generate a Treemap from", required = true) //.f is often times used for "force", maybe should not provide this option?
    private String infile;

    @Parameter(names = {"-s, --size"}, description = "size of the root polygon - default: 800", required = true)
    private int size = 800;

    @Parameter(names = {"-c, --col"}, description = "names of the colums in the provided file", required = true)
    private List<String> columnNames = new ArrayList<>();

    @Parameter(names = {"-h, --help"}, description = "show all possible commands with an explanation")
    private boolean help = false;

    @Parameter(names = {"-v, --version"}, description = "current version")
    private boolean version = false;

    public List<String> getParameters() {
        return parameters;
    }

    public void setParameters(List<String> parameters) {
        this.parameters = parameters;
    }

    public String getInfile() {
        return infile;
    }

    public void setInfile(String infile) {
        this.infile = infile;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }

    public boolean isHelp() {
        return help;
    }

    public void setHelp(boolean help) {
        this.help = help;
    }

    public boolean isVersion() {
        return version;
    }

    public void setVersion(boolean version) {
        this.version = version;
    }
}
