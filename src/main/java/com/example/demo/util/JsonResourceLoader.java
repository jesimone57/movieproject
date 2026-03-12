package com.example.demo.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;
import org.slf4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * Generic utility for scanning a classpath folder and deserializing JSON resources.
 * <p>
 * Each file is first attempted as a single object of type {@code T}; on failure it is
 * retried as a {@code List<T>}.  Malformed files are skipped with a warning.
 * <p>
 * Extracted from the near-identical folder-loading logic in
 * {@code MovieService} and {@code ActorFilmographyService}.
 */
public final class JsonResourceLoader {

    private JsonResourceLoader() {}

    /**
     * Loads all {@code *.json} files found under {@code classpath*:<folder>/**} and
     * deserializes each as either a single {@code T} or a {@code List<T>}.
     *
     * @param folder            classpath folder to scan (e.g. {@code "movies-by-year"})
     * @param clazz             target type for single-object deserialization
     * @param mapper            configured {@link ObjectMapper} to use
     * @param listPostProcessor applied to each successfully parsed list before merging
     *                          (useful for per-file deduplication); pass {@code list -> list} for identity
     * @param logger            caller's logger for diagnostic messages
     * @param <T>               target domain type
     * @return mutable list of all deserialized objects across all files
     */
    public static <T> List<T> loadFromFolder(
            String folder,
            Class<T> clazz,
            ObjectMapper mapper,
            UnaryOperator<List<T>> listPostProcessor,
            Logger logger) {

        List<T> results = new ArrayList<>();
        JavaType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        int addedFileCount = 0;

        try {
            Resource[] resources = resolver.getResources("classpath*:" + folder + "/**/*.json");
            for (Resource resource : resources) {
                if (resource == null || !resource.exists()) {
                    continue;
                }
                String name = resource.getFile().getName();

                try (InputStream is = resource.getInputStream()) {
                    // Try as a single object first
                    try {
                        T single = mapper.readValue(is, clazz);
                        if (single != null) {
                            results.add(single);
                            addedFileCount++;
                            logger.info("\t{} added as single entry: {}", addedFileCount, name);
                        }
                    } catch (IOException singleEx) {
                        // Fall back to array/list
                        try (InputStream is2 = resource.getInputStream()) {
                            List<T> list = mapper.readValue(is2, listType);
                            if (list != null && !list.isEmpty()) {
                                list = listPostProcessor.apply(list);
                                results.addAll(list);
                                addedFileCount++;
                                logger.info("\t{} added as list: {}", addedFileCount, name);
                            }
                        } catch (IOException listEx) {
                            logger.warn("\tSkipping malformed resource {}: {}", name, listEx.getMessage());
                        }
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to scan folder {}: {}", folder, e.getMessage());
        }

        logger.info("Loaded {} file(s) from folder: {}", addedFileCount, folder);
        return results;
    }
}

