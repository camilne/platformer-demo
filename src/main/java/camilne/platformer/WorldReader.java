package camilne.platformer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

public class WorldReader {

    public static World readWorld(String path) throws WorldLoadingException {
        Document doc;
        try (var input = WorldReader.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                throw new WorldLoadingException("File does not exist: " + path);
            }

            doc = getDocument(input);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new WorldLoadingException(e);
        }

        var world = new World();
        var tileSize = getTileSize(doc);

        var tileNodes = doc.getElementsByTagName("tile");
        for (int i = 0; i < tileNodes.getLength(); i++) {
            var node = tileNodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                var element = (Element) node;

                var x = getAttributeAsInteger(element, "x");
                var y = getAttributeAsInteger(element, "y");
                var id = getAttributeAsString(element, "id").toUpperCase();

                var type = TileType.valueOf(id);
                try {
                    world.addTile(new Tile(type, x * tileSize, y * tileSize, tileSize));
                } catch (IOException e) {
                    throw new WorldLoadingException(e);
                }
            }
        }
        return world;
    }

    private static Document getDocument(InputStream input) throws ParserConfigurationException, IOException, SAXException {
        var factory = DocumentBuilderFactory.newInstance();
        var builder = factory.newDocumentBuilder();
        var doc = builder.parse(input);
        doc.getDocumentElement().normalize();
        return doc;
    }

    private static int getTileSize(Document document) throws WorldLoadingException {
        return getElementAsInteger(document, "tile-size");
    }

    private static int getElementAsInteger(Document document, String elementName) throws WorldLoadingException {
        try {
            return Integer.parseInt(getElementAsString(document, elementName));
        } catch (NumberFormatException e) {
            throw new WorldLoadingException(e);
        }
    }

    private static String getElementAsString(Document document, String elementName) throws WorldLoadingException {
        var nodes = document.getElementsByTagName(elementName);
        if (nodes.getLength() != 1) {
            throw new WorldLoadingException("World file has " + nodes.getLength() + " tags with name " + elementName);
        }
        return nodes.item(0).getTextContent();
    }

    private static String getAttributeAsString(Element element, String name) throws WorldLoadingException {
        var string = element.getAttribute(name);
        if (string == null || string.isEmpty()) {
            throw new WorldLoadingException("Element " + element.getNodeName() + " does not have attribute " + name);
        }
        return string;
    }

    private static int getAttributeAsInteger(Element element, String name) throws WorldLoadingException {
        try {
            return Integer.parseInt(getAttributeAsString(element, name));
        } catch (NumberFormatException e) {
            throw new WorldLoadingException(e);
        }
    }

}
