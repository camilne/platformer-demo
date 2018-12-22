package camilne.engine.graphics.font;

import camilne.engine.graphics.TextureFactory;
import camilne.engine.graphics.TextureRegion;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.*;

public class AngelFontLoader {

    public static Font load(String path) throws IOException {
        var resource = AngelFontLoader.class.getClassLoader().getResourceAsStream(path);
        if (resource == null) {
            throw new IOException("Font file does not exist: " + path);
        }

        try (var scanner = new Scanner(new BufferedInputStream(resource))) {
            var info = loadInfo(scanner);
            var common = loadCommon(scanner);
            var pages = loadPages(scanner, common.pages);
            var chars = loadChars(scanner);
            var kernings = loadKernings(scanner);
            return loadFont(info, common, pages, chars, kernings);
        }
    }

    private static Info loadInfo(Scanner scanner) {
        var info = new Info();
        var values = parseLine(scanner.nextLine());

        info.name = getString(values, "face");
        info.size = getInt(values, "size");
        info.bold = getBoolean(values, "bold");
        info.italic = getBoolean(values, "italic");

        var top = Integer.parseInt(values.get("padding").get(0));
        var right = Integer.parseInt(values.get("padding").get(1));
        var bottom = Integer.parseInt(values.get("padding").get(2));
        var left = Integer.parseInt(values.get("padding").get(3));
        info.padding = new Insets(top, right, bottom, left);

        info.spacingX = Integer.parseInt(values.get("spacing").get(0));
        info.spacingY = Integer.parseInt(values.get("spacing").get(1));
        return info;
    }

    private static Common loadCommon(Scanner scanner) {
        var common = new Common();
        var values = parseLine(scanner.nextLine());

        common.lineHeight = getInt(values, "lineHeight");
        common.base = getInt(values, "base");
        common.scaleW = getInt(values, "scaleW");
        common.scaleH = getInt(values, "scaleH");
        common.pages = getInt(values, "pages");

        return common;
    }

    private static Map<Integer, String> loadPages(Scanner scanner, int pageCount) {
        var pages = new HashMap<Integer, String>();
        for (var i = 0; i < pageCount; i++) {
            var values = parseLine(scanner.nextLine());
            var id = getInt(values, "id");
            var file = getString(values, "file");
            pages.put(id, file);
        }
        return pages;
    }

    private static List<Char> loadChars(Scanner scanner) {
        var count = getInt(parseLine(scanner.nextLine()), "count");
        var chars = new ArrayList<Char>();
        for (var i = 0; i < count; i++) {
            var values = parseLine(scanner.nextLine());
            var c = new Char();

            c.id = getInt(values, "id");
            c.x = getInt(values, "x");
            c.y = getInt(values, "y");
            c.width = getInt(values, "width");
            c.height = getInt(values, "height");
            c.offsetX = getInt(values, "xoffset");
            c.offsetY = getInt(values, "yoffset");
            c.advance = getInt(values, "xadvance");
            c.page = getInt(values, "page");

            chars.add(c);
        }
        return chars;
    }

    private static List<Kerning> loadKernings(Scanner scanner) {
        var count = getInt(parseLine(scanner.nextLine()), "count");
        var kernings = new ArrayList<Kerning>();
        for (var i = 0; i < count; i++) {
            var values = parseLine(scanner.nextLine());

            var first = getInt(values, "first");
            var second = getInt(values, "second");
            var amount = getInt(values, "amount");

            kernings.add(new Kerning(first, second, amount));
        }
        return kernings;
    }

    private static Font loadFont(Info info, Common common, Map<Integer, String> pages, List<Char> chars, List<Kerning> kernings) {
        var font = new BitmapFont(common.lineHeight, common.base, info.spacingX, info.spacingY);
        for (var c : chars) {
            var texture = TextureFactory.create(pages.get(c.page));
            var region = new TextureRegion(texture,
                    c.x + info.padding.getLeft(),
                    c.y + info.padding.getTop(),
                    c.width - (info.padding.getLeft() + info.padding.getRight()),
                    c.height - (info.padding.getTop() + info.padding.getBottom()));
            font.addGlyph(new Glyph(c.id, region, c.offsetX, c.offsetY, c.advance));
        }
        for (var k : kernings) {
            font.addKerning(k);
        }
        return font;
    }

    private static Map<String, List<String>> parseLine(String line) {
        var tokens = line.split("\\s+");
        var values = new HashMap<String, List<String>>();
        for (var i = 1; i < tokens.length; i++) {
            var keyValue = tokens[i].split("=");
            if (keyValue.length == 2) {
                var items = keyValue[1].trim().split(",");
                values.put(keyValue[0].trim(), Arrays.asList(items));
            }
        }
        return values;
    }

    private static String get(Map<String, List<String>> map, String name) {
        return map.get(name).get(0);
    }

    private static String getString(Map<String, List<String>> map, String name) {
        var value = get(map, name);
        return value.substring(1, value.length() - 1);
    }

    private static int getInt(Map<String, List<String>> map, String name) {
        return Integer.parseInt(get(map, name));
    }

    private static boolean getBoolean(Map<String, List<String>> map, String name) {
        return "1".equals(get(map, name));
    }

    private static final class Info {
        String name;
        int size;
        boolean bold;
        boolean italic;
        Insets padding;
        int spacingX;
        int spacingY;
    }

    private static final class Common {
        int lineHeight;
        int base;
        int scaleW;
        int scaleH;
        int pages;
    }

    private static final class Char {
        int id;
        int x;
        int y;
        int width;
        int height;
        int offsetX;
        int offsetY;
        int advance;
        int page;
    }

}
