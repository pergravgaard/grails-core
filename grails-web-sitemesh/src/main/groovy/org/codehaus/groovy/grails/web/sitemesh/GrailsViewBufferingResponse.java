/*
 * Copyright 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.groovy.grails.web.sitemesh;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opensymphony.sitemesh.Content;
import com.opensymphony.sitemesh.ContentProcessor;
import com.opensymphony.sitemesh.SiteMeshContext;
import com.opensymphony.sitemesh.webapp.SiteMeshWebAppContext;

public class GrailsViewBufferingResponse extends GrailsContentBufferingResponse{
    private static class SimpleWebAppContext extends SiteMeshWebAppContext {
        public SimpleWebAppContext(HttpServletRequest request, HttpServletResponse response) {
            super(request, response, request.getServletContext());
			setContentType(response.getContentType() != null ? response.getContentType() : "text/html;charset=utf-8");
		}
    }
    
    private static class SimpleHtmlOnlyContentProcessor implements ContentProcessor {
        @Override
        public Content build(final char[] data, SiteMeshContext context) throws IOException {
            return new GrailsHTMLPageParser().parseContent(data);
        }

        @Override
        public boolean handles(SiteMeshContext context) {
            return handles(context.getContentType());
        }

        @Override
        public boolean handles(String contentType) {
            return contentType != null && contentType.contains("html");
        }
    }

    public GrailsViewBufferingResponse(HttpServletRequest request, HttpServletResponse response) {
        super(response, new SimpleHtmlOnlyContentProcessor(), new SimpleWebAppContext(request, response));
    }
}
