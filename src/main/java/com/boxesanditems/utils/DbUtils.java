package com.boxesanditems.utils;

import com.boxesanditems.entities.BoxEntity;
import com.boxesanditems.entities.ItemEntity;
import com.boxesanditems.service.BoxService;
import com.boxesanditems.service.ItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.h2.tools.Csv;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class DbUtils {
    private final static String BOX_CSV_FILE_NAME = "box.csv";
    private final static String ITEM_CSV_FILE_NAME = "item.csv";

    private final DataSource dataSource;
    private final BoxService boxService;
    private final ItemService itemService;

    public void parsePathAndSaveEntities(String path) {
        try {
            File file = getFileFromPath(path);

            if (file != null) {
                Document document = getDocumentFromFile(file);

                List<BoxEntity> boxes = new ArrayList<>();
                fillBoxesFromDocument(boxes, document);
                boxService.saveAll(boxes);

                List<ItemEntity> items = new ArrayList<>();
                fillItemsFromDocument(items, boxes, document);
                itemService.saveAll(items);
            }
        } catch (Exception e) {
            log.error("An error has occurred: " + e);
        }
    }

    public void saveTablesToCSVFile() throws SQLException {
        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            saveTable("box", BOX_CSV_FILE_NAME, conn);
            saveTable("item", ITEM_CSV_FILE_NAME, conn);
        } catch (SQLException e) {
            log.error("An error occurred while saving the database to a file: " + e);
        } finally {
            if (ofNullable(conn).isPresent()) {
                conn.close();
            }
        }
    }

    private void saveTable(String table, String csvFileName, Connection conn) throws SQLException {
        File csvFile = new File(csvFileName);
        if (csvFile.isFile() && !csvFile.delete()) {
            log.error("Can't delete next file: " + csvFileName);
        } else {
            try (Statement statement = conn.createStatement()) {
                String sql = String.format("select * from %s", table);
                ResultSet result = statement.executeQuery(sql);
                new Csv().write(csvFileName, result, StandardCharsets.UTF_8.toString());
            }
        }
    }

    private File getFileFromPath(String link) throws IOException {
        File res;
        String[] partsOfPath = link.split(":");
        if (partsOfPath.length < 2) {
            throw new IOException("Incorrect link: " + link);
        } else {
            String type = partsOfPath[0];
            String filePath = link.replaceAll(partsOfPath[0] + ":", "");

            res = switch (type) {
                case "file" -> new FileSystemResource(filePath).getFile();
                case "classpath" -> new ClassPathResource(filePath).getFile();
                case "url" -> new UrlResource(filePath).getFile();
                default -> null;
            };
        }
        return res;
    }

    private void fillItemsFromDocument(List<ItemEntity> itemEntities,
                                       List<BoxEntity> boxEntities,
                                       Document document) {
        NodeList itemElements = document.getDocumentElement().getElementsByTagName("Item");

        IntStream.range(0, itemElements.getLength()).mapToObj(itemElements::item).forEach(item -> {
            NamedNodeMap attributes = item.getAttributes();
            String color = attributes.getNamedItem("color") != null
                    ? attributes.getNamedItem("color").getNodeValue() :
                    null;
            Integer containedIn = item.getParentNode().getAttributes().getNamedItem("id") != null
                    ? Integer.valueOf(item.getParentNode().getAttributes().getNamedItem("id").getNodeValue())
                    : null;
            itemEntities.add(new ItemEntity(Integer.valueOf(attributes.getNamedItem("id").getNodeValue()),
                    color,
                    getBoxById(boxEntities, containedIn)));
        });
    }

    private void fillBoxesFromDocument(List<BoxEntity> boxEntities,
                                       Document document) {
        NodeList boxElements = document.getDocumentElement().getElementsByTagName("Box");

        IntStream.range(0, boxElements.getLength()).mapToObj(boxElements::item).forEach(box -> {
            NamedNodeMap attributes = box.getAttributes();
            Integer containedIn = box.getParentNode().getAttributes().getNamedItem("id") != null
                    ? Integer.valueOf(box.getParentNode().getAttributes().getNamedItem("id").getNodeValue())
                    : null;
            boxEntities.add(new BoxEntity(Integer.valueOf(attributes.getNamedItem("id").getNodeValue()), containedIn));
        });
    }

    private BoxEntity getBoxById(List<BoxEntity> boxEntities, Integer id) {
        return boxEntities.stream()
                .filter(boxEntity -> Objects.equals(boxEntity.getId(), id))
                .findFirst()
                .orElse(null);
    }

    private Document getDocumentFromFile(File file) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(file);
    }
}
