package life.qbic.voronoi.io.commandline;

import picocli.CommandLine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandLineParser {

    private final static Logger LOG = LogManager.getLogger(CommandLineParser.class);
    /**
     * parses all required parameters and save them into a CommandlineOptions object
     *
     * @param args always has to be a String[] or portlet compatibility breaks!
     */
    public static CommandlineOptions parseCommandlineParameters(String[] args) {
        LOG.debug("Parsing commandline parameters");
        //no input -> display help
        if (args.length == 0) {
            LOG.info("No commandline parameters passed -> displaying help");
            CommandLine.usage(new CommandlineOptions(), System.out);
            System.exit(0);
        }

        CommandlineOptions commandlineOptions = CommandLine.populateCommand(new CommandlineOptions(), args);

        if (commandlineOptions.isHelpRequested()) {
            LOG.debug("Help requested");
            CommandLine.usage(new CommandlineOptions(), System.out);
            System.exit(0);
        }

        LOG.debug("Successfully parsed commandline parameters");

        return commandlineOptions;
    }
}
