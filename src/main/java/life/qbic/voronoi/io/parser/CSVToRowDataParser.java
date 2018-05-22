package life.qbic.voronoi.io.parser;

import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import life.qbic.voronoi.model.RowData;
import life.qbic.voronoi.util.NumberUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVToRowDataParser {

    private static final Logger LOG = LogManager.getLogger(CSVToRowDataParser.class);

    /**
     * reads a .csv file by given column names and returns a list consisting of VoroCell objects that hold
     * the given data.
     *
     * @param csvFilePath
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<RowData> parseCSV(String csvFilePath, List<String> columns) throws FileNotFoundException, IOException {
        LOG.info("Parsing csv/tsv data");
        List<RowData> voroCells = new ArrayList<>();

        com.opencsv.CSVParser parser = new CSVParserBuilder()
                .withSeparator('\t')
                .build();
        CSVReader reader = new CSVReaderBuilder(new FileReader(csvFilePath))
                .withCSVParser(parser)
                .build();

        String[] header = reader.readNext();
        // current line which is being read
        String[] nextLine;

        List<String> levels = new ArrayList<>();
        List<Double> ratios = new ArrayList<>();

        while ((nextLine = reader.readNext()) != null) {
            // search for given columns
            for (String c : columns) {
                for (int i = 0; i < nextLine.length; i++) {
                    if (header[i].equals(c)) {
                        String s = nextLine[i];
                        if (s.contains("_"))
                            s = s.replace("_", " ");
//						if (s.contains("GO:"))
//							s = s.substring(s.indexOf("~")+1, s.length());
                        if (NumberUtil.isNumeric(s)) {
                            double d = Double.parseDouble(s);
                            ratios.add(d);
                        } else
                            levels.add(s);
                    }
                }
            }

            voroCells.add(new RowData(levels, ratios));
            levels.clear();
            ratios.clear();
        }
        reader.close();

        LOG.info("Finished parsing csv/tsv data");
        return voroCells;
    }

}
