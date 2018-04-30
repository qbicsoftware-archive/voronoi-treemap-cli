package life.qbic.voronoi.io.parser;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import kn.uni.voronoitreemap.j2d.PolygonSimple;
import life.qbic.voronoi.model.PolygonData;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PolygonDataParser {

    /**
     * read all the data of each polygon in the given text file and return it as PolygonData object.
     *
     * @param txtFile
     * @return complete data of each polygon
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static List<PolygonData> readPolygonData(String txtFile) throws FileNotFoundException, IOException {
        List<PolygonData> polygonData = new ArrayList<>();

        String name = null;
        int level = 0;
        PolygonSimple poly = null;

        CSVParser parser = new CSVParserBuilder()
                .withSeparator(';')
                .build();

        CSVReader reader = new CSVReaderBuilder(new FileReader(txtFile))
                .withCSVParser(parser)
                .build();

        String[] nextLine;
        String[] header = reader.readNext();

        while ((nextLine = reader.readNext()) != null) {
            for (int i = 0; i < nextLine.length; i++) {
                switch (header[i]) {
                    case "name":
                        //remove duplicate tags
                        if (nextLine[i].contains("(")) {
                            name = nextLine[i].substring(0, nextLine[i].indexOf("("));
                            break;
                        }
                        name = nextLine[i];
                        break;

                    case "hierarchyLevel":
                        level = Integer.parseInt(nextLine[i]);
                        break;

                    case "polygonPoints x1,y2,x2,y2":
                        String[] points = nextLine[i].split(",");
                        double[] x = new double[points.length / 2];
                        double[] y = new double[points.length / 2];

                        int count = 1;
                        for (int j = 0; j < points.length; j++) {
                            if (count % 2 != 0) {
                                x[j / 2] = Double.parseDouble(points[j]);
                            } else {
                                y[(j - 1) / 2] = Double.parseDouble(points[j]);
                            }
                            count++;
                        }
                        poly = new PolygonSimple(x, y);
                        break;

                    default:
                        break;
                }
            }
            polygonData.add(new PolygonData(name, level, poly));
        }

        reader.close();
        return polygonData;
    }
}
