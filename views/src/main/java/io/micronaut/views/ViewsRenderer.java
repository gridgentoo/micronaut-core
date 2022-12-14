/*
 * Copyright 2017-2018 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.micronaut.views;

import io.micronaut.core.io.Writable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;

/**
 * Interface to be implemented by View Engines implementations.
 *
 * @author Sergio del Amo
 * @since 1.0
 */
public interface ViewsRenderer {

    /**
     * The file separator to use.
     *
     * @deprecated Use {@link File#separator} directly
     */
    @Deprecated
    String FILE_SEPARATOR = File.separator;

    /**
     * The extension separator.
     */
    String EXTENSION_SEPARATOR = ".";

    /**
     * @param viewName view name to be render
     * @param data     response body to render it with a view
     * @return A writable where the view will be written to.
     */
    @Nonnull Writable render(@Nonnull String viewName, @Nullable Object data);

    /**
     * @param viewName view name to be render
     * @return true if a template can be found for the supplied view name.
     */
    boolean exists(@Nonnull String viewName);

    /**
     * Returns a path with unix style folder
     * separators that starts and ends with a "\".
     *
     * @param path The path to normalizeFile
     * @return The normalized path
     */
    @Nonnull
    default String normalizeFolder(@Nullable String path) {
        if (path == null) {
            path = "";
        } else {
            path = normalizeFile(path, null);
        }
        if (!path.endsWith("/")) {
            path = path + "/";
        }
        return path;
    }

    /**
     * Returns a path that is converted to unix style file separators
     * and never starts with a "\". If an extension is provided and the
     * path ends with the extension, the extension will be stripped.
     * The extension parameter supports extensions that do and do not
     * begin with a ".".
     *
     * @param path The path to normalizeFile
     * @param extension The file extension
     * @return The normalized path
     */
    @Nonnull
    default String normalizeFile(@Nonnull String path, String extension) {
        path = path.replace("\\", "/");
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        if (extension != null && !extension.startsWith(".")) {
            extension = "." + extension;
            if (path.endsWith(extension)) {
                int idx = path.indexOf(extension);
                path = path.substring(0, idx);
            }
        }
        return path;
    }
}
