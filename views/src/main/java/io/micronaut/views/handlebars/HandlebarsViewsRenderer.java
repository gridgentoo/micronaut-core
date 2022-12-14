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

package io.micronaut.views.handlebars;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import io.micronaut.context.annotation.Requires;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.io.Writable;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.core.util.ArgumentUtils;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Produces;
import io.micronaut.views.ViewsConfiguration;
import io.micronaut.views.ViewsRenderer;
import io.micronaut.views.exceptions.ViewRenderingException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.inject.Singleton;

/**
 * Renders Views with with Handlebars.java.
 *
 * @author Sergio del Amo
 * @see <a href="http://jknack.github.io/handlebars.java/">http://jknack.github.io/handlebars.java/</a>
 * @since 1.0
 */
@Produces(MediaType.TEXT_HTML)
@Requires(property = HandlebarsViewsRendererConfigurationProperties.PREFIX + ".enabled", notEquals = "false")
@Requires(classes = Handlebars.class)
@Singleton
public class HandlebarsViewsRenderer implements ViewsRenderer {

    protected final ViewsConfiguration viewsConfiguration;
    protected final ResourceLoader resourceLoader;
    protected HandlebarsViewsRendererConfiguration handlebarsViewsRendererConfiguration;
    protected Handlebars handlebars = new Handlebars();
    protected String folder;


    /**
     * @param viewsConfiguration                   Views Configuration.
     * @param resourceLoader                       Resource Loader
     * @param handlebarsViewsRendererConfiguration Handlebars ViewRenderer Configuration.
     */
    public HandlebarsViewsRenderer(ViewsConfiguration viewsConfiguration,
                                   ClassPathResourceLoader resourceLoader,
                                   HandlebarsViewsRendererConfiguration handlebarsViewsRendererConfiguration) {
        this.viewsConfiguration = viewsConfiguration;
        this.resourceLoader = resourceLoader;
        this.handlebarsViewsRendererConfiguration = handlebarsViewsRendererConfiguration;
        this.folder = normalizeFolder(viewsConfiguration.getFolder());
    }

    @Override
    @Nonnull public Writable render(@Nonnull String viewName, @Nullable Object data) {
        ArgumentUtils.requireNonNull("viewName", viewName);
        return (writer) -> {
            String location = viewLocation(viewName);
            try {
                Template template = handlebars.compile(location);
                template.apply(data, writer);
            } catch (Throwable e) {
                throw new ViewRenderingException("Error rendering Handlebars view [" + viewName + "]: " + e.getMessage(), e);
            }
        };
    }

    @Override
    public boolean exists(@Nonnull String viewName) {
        //noinspection ConstantConditions
        if (viewName == null) {
            return false;
        }
        String location = viewLocation(viewName) + EXTENSION_SEPARATOR + extension();
        return resourceLoader.getResource(location).isPresent();
    }

    private String viewLocation(final String name) {
        return folder +
                normalizeFile(name, extension());
    }

    private String extension() {
        return handlebarsViewsRendererConfiguration.getDefaultExtension();
    }

}
