/*
 * Asqatasun - Automated webpage assessment
 * Copyright (C) 2008-2015  Asqatasun.org
 *
 * This file is part of Asqatasun.
 *
 * Asqatasun is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Contact us by mail: asqatasun AT asqatasun DOT org
 */
package org.asqatasun.service;

import java.util.List;
import org.asqatasun.entity.audit.Audit;
import org.asqatasun.entity.audit.Content;
import org.asqatasun.entity.service.audit.ContentDataService;
import org.asqatasun.entity.service.subject.WebResourceDataService;
import org.asqatasun.entity.subject.WebResource;
import org.asqatasun.scenarioloader.ScenarioLoader;
import org.asqatasun.scenarioloader.ScenarioLoaderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 
 * @author jkowalczyk
 */
@Service("scenarioLoaderService")
public class ScenarioLoaderServiceImpl implements ScenarioLoaderService {

    @Autowired
    private ScenarioLoaderFactory scenarioLoaderFactory;
    @Autowired
    private ContentDataService contentDataService;
    @Autowired
    private WebResourceDataService webResourceDataService;

    public ScenarioLoaderServiceImpl() {
        super();
    }

    @Override
    public List<Content> loadScenario(WebResource webResource, String scenarioFile) {
        Audit audit = webResource.getAudit();
        ScenarioLoader scenarioLoader = scenarioLoaderFactory.create(webResource, scenarioFile);
        scenarioLoader.run();
        List<Content> contentList = scenarioLoader.getResult();

        for (Content content : contentList) {
            contentDataService.saveAuditToContent(content.getId(),audit.getId());
        }

        // Before returning the list of content we save the webResource
        webResourceDataService.saveOrUpdate(webResource);
        return contentList;
    }

}