package cz.muni.fi.pv168.project.business.service.export.format;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Mapping from an extension to the instances deriving from {@link FileFormat}.
 *
 * @param <T> The interface deriving from {@link FileFormat}.
 */
public final class FormatMapping<T extends FileFormat> {

    private final Collection<T> fileFormats;
    private final Map<String, T> extensionMapping;

    /**
     * Creates a mapping.
     *
     * @param fileFormats The {@link Collection} of derives from {@link FileFormat}
     */
    public FormatMapping(Collection<T> fileFormats) {
        this.fileFormats = fileFormats;
        extensionMapping = fileFormats.stream()
            .map(f -> f.getFormat().extensions().stream()
                    .collect(Collectors.toMap(
                            Function.identity(),
                            e -> f
                    )))
            .flatMap(m -> m.entrySet().stream())
            .collect(Collectors.toMap(
                    Map.Entry::getKey,
                    Map.Entry::getValue
            ));
    }

    /**
     * Gets an item find by the extension.
     *
     * @return The item for which exists a mapping from the extension, {@code null} otherwise.
     */
    public T findByExtension(String extension) {
        return extensionMapping.get(extension);
    }

    /**
     * Gets all available formats in the mapping.
     *
     * @return The {@link Collection} of {@link Format}
     */
    public Collection<Format> getFormats() {
        return fileFormats.stream()
                .map(T::getFormat)
                .toList();
    }
}
