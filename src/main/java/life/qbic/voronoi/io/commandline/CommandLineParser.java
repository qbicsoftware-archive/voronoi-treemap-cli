package life.qbic.voronoi.io.commandline;

import picocli.CommandLine;

public class CommandLineParser {

    /**
     * parses all required parameters and save them into a CommandlineOptions object
     *
     * @param args always has to be a String[] or portlet compatibility breaks!
     */
    public static CommandlineOptions parseCommandlineParameters(String[] args) {
        //no input -> display help
        if (args.length == 0) {
            CommandLine.usage(new CommandlineOptions(), System.out);
            System.exit(0);
        }

        CommandlineOptions commandlineOptions = CommandLine.populateCommand(new CommandlineOptions(), args);

        if (commandlineOptions.isHelpRequested()) {
            CommandLine.usage(new CommandlineOptions(), System.out);
            System.exit(0);
        }

        return commandlineOptions;
    }
}
