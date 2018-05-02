package life.qbic.voronoi.io.writer;

import kn.uni.voronoitreemap.j2d.PolygonSimple;
import life.qbic.voronoi.VoronoiTreemapStartup;
import life.qbic.voronoi.model.PolygonData;
import life.qbic.voronoi.util.NumberUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.List;

public class HTMLWriter {

    private static final Logger LOG = LogManager.getLogger(HTMLWriter.class);

    /**
     * Write several information and svg content that shows the finished Voronoi treemap into existing html file.
     *
     * @param polygonData
     * @throws IOException
     */
    public static String writeToHtml(List<PolygonData> polygonData, boolean writeTemporaryFile, double size, int border, String outputFilePath) {
        LOG.info("Writing the finished html file into the HTML file");
        StringBuilder contentBuilder = new StringBuilder();
        LOG.info("Fetching template file");
        InputStream inputStream = VoronoiTreemapStartup.class.getClassLoader().getResourceAsStream("VoroTreemapTemplate.html");
        LOG.info("Received template file");

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
            String str;
            while ((str = in.readLine()) != null) {
                if (str.contains("~TreemapDepth~")) {
                    str = str.replace("~TreemapDepth~", "var depth = 3" + ";");
//		    	} else if(str.contains("~LevelNames~")) {
//	    			str = str.replace("~LevelNames~", "var levelNames = " + ";");
                } else if (str.contains("~RatioCount~")) {
                    str = str.replace("~RatioCount~", "var ratioCount = " + polygonData.get(2).getRatios().size() + ";");
//	    		} else if(str.contains("~RatioNames~")) {
//	    			str = str.replace("~RatioNames~", "var ratioNames = " + ";");
                } else if (str.contains("~SVG~")) {
                    str = str.replace("~SVG~", "var svgContent = ");

                    //head and opening tags
                    str += "\"<svg class='treemap' width='" + size + "' height='" + size + "' viewBox='0 0 " + (size + border * 2) + " " + (size + border * 2) + "' "
                            + "xmlns='http://www.w3.org/2000/svg' xmlns:xlink='http://www.w3.org/1999/xlink'>";

                    // polygon settings
                    str += "<g id='polygons'>";
                    for (PolygonData pDat : polygonData) {
                        str += "<polygon class='lvl" + pDat.getLevel() + "' name='" + pDat.getName() + "' ";

                        // ratios
                        if (pDat.getRatios() != null) {
                            for (int i = 0; i < pDat.getRatios().size(); i++) {
                                str += "ratio" + (i + 1) + "='" + pDat.getRatios().get(i) + "' ";
                            }
                        }
                        //polygon points
                        str += "points='";

                        PolygonSimple p = pDat.getPolygon();
                        for (int i = 0; i < p.length; i++) {
                            str += p.getXPoints()[i] + "," + p.getYPoints()[i] + " ";
                        }

                        double strokeWidth = 8 - NumberUtil.normalize(Math.log(pDat.getLevel()), 0, 1.5, 2, 8);

                        String hexColor = String.format("#%02x%02x%02x", pDat.getColor().getRed(), pDat.getColor().getGreen(), pDat.getColor().getBlue());

                        str += "' style='fill:" + hexColor + ";stroke:black;stroke-width:" + strokeWidth + "' />";
                    }
                    str += "</g>";

                    // text settings
                    str += "<g id='names'>";
                    String name;

                    float fontSize = 12;

                    Rectangle2D bounds;
                    double width;
                    double height;

                    for (PolygonData pDat : polygonData) {
                        name = pDat.getName().replace(" ", "\n");

                        Font font = new Font("Helvetica", Font.PLAIN, (int) fontSize);
                        fontSize = fitTextIntoPolygon(name, font, pDat);
                        font = font.deriveFont(fontSize);

                        bounds = font.getStringBounds(name, new FontRenderContext(font.getTransform(), true, true));
                        width = bounds.getWidth();
                        height = bounds.getHeight();

                        double posX = pDat.getPolygon().getCentroid().getX();
                        double posY = pDat.getPolygon().getCentroid().getY();

                        // line break adjustment
                        if (name.contains("\n")) {
                            int count = name.length() - name.replace("\n", "").length() + 1;

                            posY -= bounds.getHeight() * count / 1.5 / 2;

                            for (String line : name.split("\n")) {
                                bounds = font.getStringBounds(line, new FontRenderContext(font.getTransform(), true, true));

                                posX = (pDat.getPolygon().getCentroid().getX() - bounds.getWidth() / 2.0);
                                posY += bounds.getHeight() / 1.5;

                                str += "<text class='lvl" + pDat.getLevel() + "' x='" + posX + "' y='" + posY + "' style='font-size:" + fontSize + "px;fill:white;'>" + line + "</text>";
                            }
                        } else
                            str += "<text class='lvl" + pDat.getLevel() + "' x='" + (posX - width / 2.0) + "' y='" + (posY + height / 2.0) + "' style='font-size:" + fontSize + "px;fill:white;'>" + name + "</text>";
                    }
                    str += "</g>";

                    // closing tags
                    str += "</svg>\";";
                }
                contentBuilder.append(str + "\n");
            }

            in.close();
        } catch (IOException e) {
            LOG.error("Error while writing HTML file");
        }

        String content = contentBuilder.toString();

        try {
            if (writeTemporaryFile) {
                LOG.info("");
                File temp = File.createTempFile("voroTreemap", ".html");
                temp.deleteOnExit();
                FileWriter htmlWriter = new FileWriter(temp);
                htmlWriter.write(content);
                htmlWriter.close();
                LOG.info("Treemap written to temporary filepath: " + temp.getAbsolutePath());
                return temp.getAbsolutePath();
            } else {
                FileWriter htmlWriter = new FileWriter(outputFilePath);
                htmlWriter.write(content);
                htmlWriter.close();
                LOG.info("Treemap written to: " + outputFilePath);
                return outputFilePath;
            }
        } catch (IOException e) {
            LOG.error("Unable to save the finished HTML file in: " + outputFilePath);
            System.exit(1);
        }

        return null;
    }

    /**
     * find a size for the given text to fit into the given polygon and return it
     *
     * @param name
     * @param font
     * @param pDat
     * @return fitting size for the given text
     */
    private static float fitTextIntoPolygon(String name, Font font, PolygonData pDat) {
        Font f = font;
        float size = 1000;
        f = f.deriveFont(size);

        double cx = pDat.getPolygon().getCentroid().getX();
        double cy = pDat.getPolygon().getCentroid().getY();

        Rectangle2D bounds = f.getStringBounds(name, new FontRenderContext(f.getTransform(), true, true));
        double width = bounds.getWidth();
        double height = bounds.getHeight();

        Rectangle2D rect = new Rectangle2D.Double(cx - width / 2, cy - height / 2, width, height);

        while (!pDat.getPolygon().contains(rect)) {
            size *= 0.9;
            f = f.deriveFont(size);

            bounds = f.getStringBounds(name, new FontRenderContext(f.getTransform(), true, true));
            width = bounds.getWidth();
            height = bounds.getHeight();

            // adjust size of text with line breaks
            if (name.contains("\n")) {
                String longest = "";
                int count = 0;

                for (String line : name.split("\n")) {
                    if (longest.length() < line.length())
                        longest = line;
                    count++;
                }

                bounds = f.getStringBounds(longest, new FontRenderContext(f.getTransform(), true, true));
                width = bounds.getWidth();
                height = bounds.getHeight() * count;

                rect = new Rectangle2D.Double(cx - width / 2, cy - height / 2, bounds.getWidth(), bounds.getHeight() * count);
            }

            rect = new Rectangle2D.Double(cx - width / 2, cy - height / 2, width, height);
        }
        size *= 0.7;
        return size;
    }
}
