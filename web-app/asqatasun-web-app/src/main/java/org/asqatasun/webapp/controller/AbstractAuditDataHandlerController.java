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
package org.asqatasun.webapp.controller;

import java.util.*;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;
import org.displaytag.pagination.PaginatedList;
import org.asqatasun.entity.audit.Audit;
import org.asqatasun.entity.audit.AuditStatus;
import org.asqatasun.entity.parameterization.Parameter;
import org.asqatasun.entity.reference.Scope;
import org.asqatasun.entity.service.audit.AuditDataService;
import org.asqatasun.entity.service.audit.ContentDataService;
import org.asqatasun.entity.service.parameterization.ParameterDataService;
import org.asqatasun.entity.service.reference.ScopeDataService;
import org.asqatasun.entity.service.reference.TestDataService;
import org.asqatasun.entity.service.subject.WebResourceDataService;
import org.asqatasun.entity.subject.WebResource;
import org.asqatasun.webapp.entity.contract.Act;
import org.asqatasun.webapp.entity.contract.Contract;
import org.asqatasun.webapp.entity.decorator.asqatasun.parameterization.ParameterDataServiceDecorator;
import org.asqatasun.webapp.entity.service.contract.ActDataService;
import org.asqatasun.webapp.entity.service.statistics.StatisticsDataService;
import org.asqatasun.webapp.entity.user.User;
import org.asqatasun.webapp.exception.ForbiddenPageException;
import org.asqatasun.webapp.exception.ForbiddenUserException;
import org.asqatasun.webapp.exception.OrphanWebResourceException;
import org.asqatasun.webapp.dto.data.AuditStatistics;
import org.asqatasun.webapp.dto.factory.AuditStatisticsFactory;
import org.asqatasun.webapp.report.pagination.factory.TgolPaginatedListFactory;
import org.asqatasun.webapp.util.HttpStatusCodeFamily;
import org.asqatasun.webapp.util.TgolKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.LocaleResolver;


/**
 * This abstract controller handles methods to retrieve and format audit data
 * @author jkowalczyk
 */
@Controller
public abstract class AbstractAuditDataHandlerController extends AbstractController {

    private int pageScopeId = 1;
    public void setPageScopeId(int pageScopeId) {
        this.pageScopeId = pageScopeId;
    }

    private int siteScopeId = 2;
    public void setSiteScopeId(int siteScopeId) {
        this.siteScopeId = siteScopeId;
    }

    private Scope siteScope;
    /**
     *
     * @return the scope instance
     */
    public Scope getSiteScope() {
        return siteScope;
    }

    private Scope pageScope;
    public Scope getPageScope() {
        return pageScope;
    }

    /*
     * Displaying bounds
     */
    protected static final String FROM_VALUE = "fromValue";
    protected static final String TO_VALUE = "toValue";

    /*
     * Authorized elements depending on the context.
     */
    @Value("${authorizedPageSize:10,25,50,100,250,1000,-1}")
    private String authorizedPageSizeStr;
    private List<Integer> authorizedPageSize = new ArrayList<>();


    private final Set<String> authorizedSortCriterion = new LinkedHashSet<>();
    public Set<String> getAuthorizedSortCriterion() {
        return authorizedSortCriterion;
    }

    /**
     * This method initializes the siteScope and the pageScope instances through
     * their persistence Id.
     */
    @PostConstruct
    public final void init() {
        siteScope = scopeDataService.read(Long.valueOf(siteScopeId));
        pageScope = scopeDataService.read(Long.valueOf(pageScopeId));
        this.defaultParamSet = parameterDataService.getDefaultParameterSet();
        for (String size :authorizedPageSizeStr.split(",")) {
            authorizedPageSize.add(Integer.valueOf(size));
        }
    }
    @Autowired
    private ScopeDataService scopeDataService;
    @Autowired
    protected WebResourceDataService webResourceDataService;
    @Autowired
    protected StatisticsDataService statisticsDataService;
    @Autowired
    protected AuditDataService auditDataService;
    @Autowired
    protected ActDataService actDataService;
    @Autowired
    protected ContentDataService contentDataService;
    @Autowired
    protected TestDataService testDataService;
    @Autowired
    protected ParameterDataServiceDecorator parameterDataService;
    @Autowired
    protected LocaleResolver localeResolver;

    private Set<Parameter> defaultParamSet;
    public Set<Parameter> getDefaultParamSet() {
        return ((Set) ((HashSet) defaultParamSet).clone());
    }

    //    @Value("${defaultParametersToDisplay}")
    private Map<String, String> parametersToDisplay;
    public void setParametersToDisplay(Map<String, String> parametersToDisplay) {
        this.parametersToDisplay = parametersToDisplay;
    }
    @Value("${authorizedScopeForPageList:SCENARIO,DOMAIN}")
    private List<String> authorizedScopeForPageList;
    public void setAuthorizedScopeForPageList(List<String> authorizedScopeForPageList) {
        this.authorizedScopeForPageList.addAll(authorizedScopeForPageList);
    }
    private static final String INVALID_TEST_VALUE_CHECKER_REGEXP = "\\d\\d?\\.\\d\\d?\\.?\\d?\\d?";
    private final Pattern invalidTestValueCheckerPattern = Pattern.compile(INVALID_TEST_VALUE_CHECKER_REGEXP);
    
    public AbstractAuditDataHandlerController() {}

    protected boolean isAuthorizedScopeForPageList(Audit audit) {
        String scope = actDataService.getActFromAudit(audit).getScope().getCode().name();
        return authorizedScopeForPageList.contains(scope);
    }

    /**
     * Add a populated auditStatistics instance to the model
     * 
     * @param webResource
     * @param model
     * @param displayScope
     */
    protected void addAuditStatisticsToModel(WebResource webResource, Model model, String displayScope) {
        model.addAttribute(
                TgolKeyStore.STATISTICS_KEY,
                getAuditStatistics(webResource, model, displayScope, false)); // default is false for manual audit
    }

    /**
     * 
     * @param webResource
     * @param model
     * @param displayScope
     * @param isAuditManual 
     * @return
     */
    protected AuditStatistics getAuditStatistics(WebResource webResource, Model model, String displayScope, boolean isAuditManual){
        return AuditStatisticsFactory.getInstance().getAuditStatistics(
                webResource, 
                parametersToDisplay,
                displayScope,
                isAuditManual);
    }

    /**
     * This methods checks whether the current user is allowed to display the
     * audit result of a given audit. To do so, we verify that the act
     * associated with the audit belongs to the current user and
     * that the current contract is not expired
     * 
     * @param audit
     * @return
     *      true if the user is allowed to display the result, false otherwise.
     */
        protected boolean isUserAllowedToDisplayResult(Audit audit) {
        if (audit == null) {
            throw new ForbiddenPageException();
        }
        User user = getCurrentUser();
        Contract contract = actDataService.getActFromAudit(audit).getContract();
        if (isAdminUser() || (!isContractExpired(contract) && user.getId().compareTo(
                contract.getUser().getId()) == 0)) {
            return true;
        }
        throw new ForbiddenUserException();
    }
        
    /**
     * 
     * @param webResource
     * @return an audit for a given webResource
     */
    protected Audit getAuditFromWebResource(WebResource webResource) {
        if (webResource.getAudit() != null) {
            return webResource.getAudit();
        } else if (webResource.getParent() != null) {
            return webResource.getParent().getAudit();
        }
        throw new OrphanWebResourceException();
    }
    
    /**
     * @param audit
     * @return The Contract associated with the given audit (through the 
     * Act associated with the given audit).
     *
     */
    protected Contract retrieveContractFromAudit(Audit audit) {
        Act act = null;
        try {
            act = actDataService.getActFromAudit(audit);
        } catch (NoResultException e) {}
        if (act!= null && act.getContract() != null) {
            return act.getContract();
        }
        return null;
    }

    /**
     * 
     * @param audit
     * @return
     */
    protected String computeAuditStatus(Audit audit) {
        if (audit.getStatus().equals(AuditStatus.COMPLETED)) {
            return TgolKeyStore.COMPLETED_KEY;
        } else if (!contentDataService.hasContent(audit)) {
            return TgolKeyStore.ERROR_LOADING_KEY;
        } else if (!contentDataService.hasAdaptedSSP(audit)) {
            return TgolKeyStore.ERROR_ADAPTING_KEY;
        } else {
            return TgolKeyStore.ERROR_UNKNOWN_KEY;
        }
    }

    /**
     * This method determines which page to display when an error occured
     * while processing
     * @param audit
     * @param model
     * @return
     */
    protected String prepareFailedAuditData(Audit audit, Model model) {
        String returnViewName = TgolKeyStore.OUPS_VIEW_NAME;
        model.addAttribute(TgolKeyStore.AUDIT_URL_KEY,
                audit.getSubject().getURL());
        model.addAttribute(TgolKeyStore.AUDIT_DATE_KEY,
                audit.getDateOfCreation());
        String status = this.computeAuditStatus(audit);
        if (status.equalsIgnoreCase(TgolKeyStore.ERROR_LOADING_KEY)) {
            returnViewName = TgolKeyStore.LOADING_ERROR_VIEW_NAME;
        } else if (status.equalsIgnoreCase(TgolKeyStore.ERROR_ADAPTING_KEY)) {
            returnViewName = TgolKeyStore.ADAPTING_ERROR_VIEW_NAME;
        }
        return returnViewName;
    }

    /**
     * 
     * @param audit
     * @param model
     * @param httpStatusCode
     * @param request
     * @param returnRedirectView
     * @return
     * @throws ServletRequestBindingException 
     */
    protected String preparePageListStatsByHttpStatusCode(
            Audit audit,
            Model model,
            HttpStatusCodeFamily httpStatusCode,
            HttpServletRequest request,
            boolean returnRedirectView) throws ServletRequestBindingException {
        
        String invalidTest = ServletRequestUtils.getStringParameter(request, TgolPaginatedListFactory.INVALID_TEST_PARAM);
        
        if (invalidTest != null && !this.invalidTestValueCheckerPattern.matcher(invalidTest).matches()) {
            throw new ForbiddenPageException();
        }

        PaginatedList paginatedList = TgolPaginatedListFactory.getInstance().getPaginatedList(
                httpStatusCode,
                ServletRequestUtils.getStringParameter(request, TgolPaginatedListFactory.PAGE_SIZE_PARAM),
                ServletRequestUtils.getStringParameter(request, TgolPaginatedListFactory.SORT_DIRECTION_PARAM),
                ServletRequestUtils.getStringParameter(request, TgolPaginatedListFactory.SORT_CRITERION_PARAM),
                ServletRequestUtils.getStringParameter(request, TgolPaginatedListFactory.PAGE_PARAM),
                ServletRequestUtils.getStringParameter(request, TgolPaginatedListFactory.SORT_CONTAINING_URL_PARAM),
                invalidTest,
                authorizedPageSize,
                authorizedSortCriterion,
                audit.getId());

        model.addAttribute(TgolKeyStore.PAGE_LIST_KEY, paginatedList);
        model.addAttribute(TgolKeyStore.AUTHORIZED_PAGE_SIZE_KEY, authorizedPageSize);
        model.addAttribute(TgolKeyStore.AUTHORIZED_SORT_CRITERION_KEY, authorizedSortCriterion);
        setFromToValues(paginatedList, model);
        
        // don't forge to add audit statistics to model
//        addAuditStatisticsToModel(audit, model, TgolKeyStore.TEST_DISPLAY_SCOPE_VALUE);
        return (returnRedirectView) ? TgolKeyStore.PAGE_LIST_XXX_VIEW_REDIRECT_NAME : TgolKeyStore.PAGE_LIST_XXX_VIEW_NAME;
    }

    /**
     *
     * @param pageResultList
     * @param model
     * @return
     */
    private void setFromToValues(PaginatedList pageResultList, Model model) {
        model.addAttribute(FROM_VALUE,
                (pageResultList.getPageNumber()-1) * pageResultList.getObjectsPerPage() +1);
        if (pageResultList.getList().size() < pageResultList.getObjectsPerPage()) {
            model.addAttribute(TO_VALUE,
                    (pageResultList.getPageNumber()-1) * pageResultList.getObjectsPerPage() + pageResultList.getList().size());
        } else {
            model.addAttribute(TO_VALUE,
                    (pageResultList.getPageNumber()) * pageResultList.getObjectsPerPage());
        }
    }

}